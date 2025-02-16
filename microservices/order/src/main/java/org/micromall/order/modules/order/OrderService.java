package org.micromall.order.modules.order;

import java.util.List;
import java.util.stream.Collectors;

import org.micromall.order.exception.MyNotDeleteException;
import org.micromall.order.exception.MyNotFoundException;
import org.micromall.order.exception.MyNotSaveException;
import org.micromall.order.interfaces.IDaoService;
import org.micromall.order.kafka.OrderConfirmation;
import org.micromall.order.kafka.OrderProducer;
import org.micromall.order.modules.customer.CustomerDTO;
import org.micromall.order.modules.customer.CustomerService;
import org.micromall.order.modules.item.Item;
import org.micromall.order.modules.item.ItemDTO;
import org.micromall.order.modules.product.ProductDTO;
import org.micromall.order.modules.product.ProductService;
import org.micromall.order.modules.product.PurchaseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IDaoService<Order, OrderDTO, OrderRequest, OrderRequest, Long> {

    // Logger
    Logger logger = LoggerFactory.getLogger(getClass().getName());

    // Repository
    private final OrderRepository orderRepository;

    // Service
    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderProducer orderProducer;

    // Mapper
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO create(OrderRequest request) throws MyNotSaveException {
        // Get customer information from customer microservices
        CustomerDTO customer = customerService.findById(request.customerId());
        if (customer == null) {
            logger.info("Customer with id {} not found", request.customerId());
            throw new MyNotFoundException("Customer not found");
        }

        // Purchase product form the catalog microservice
        List<Long> productIds = request.products().stream()
                .map(PurchaseRequest::productId)
                .collect(Collectors.toList());
        List<ProductDTO> products = productService.fetchPurchaseProducts(productIds);
        if (products.isEmpty()) {
            logger.info("Product with id {} not found", productIds);
            throw new MyNotFoundException("Product not found");
        }

        // Create items
        List<Item> items = request.products().stream()
                .map(item -> {
                    // Filter the corresponding product by ID
                    ProductDTO product = products.stream()
                            .filter(p -> p.getId().equals(item.productId()))
                            .findFirst()
                            .orElseThrow(() -> new MyNotFoundException("Product not found"));

                    // Calcul the items amount
                    Double itemAmount = item.quantity() * product.getPrice();

                    return Item.builder()
                            .productId(item.productId())
                            .quantity(item.quantity())
                            .itemAmount(itemAmount)
                            .build();
                })
                .collect(Collectors.toList());

        // Calcul the total order amount
        Double orderAmount = items.stream()
                .mapToDouble(Item::getItemAmount)
                .sum();

        if (orderAmount.equals(request.amount())) {
            logger.info("The amount sent by request is not equal to the total amount of the order");
        }

        // Create a order
        Order order = Order.builder()
                .items(items)
                .customerId(request.customerId())
                .amount(orderAmount)
                .status(OrderStatus.PENDING)
                .build();
        order = orderRepository.save(order);

        // Create List of ItemDTO
        List<ItemDTO> itemDTOs = items.stream()
                .map(item -> {
                    // Find the corresponding product by ID
                    ProductDTO product = products.stream()
                            .filter(p -> p.getId().equals(item.getProductId()))
                            .findFirst()
                            .orElseThrow(() -> new MyNotFoundException("Product not found"));

                    // Build ItemDTO with title, price, etc.
                    return ItemDTO.builder()
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .title(product.getTitle()) // Set product title from ProductDTO
                            .price(product.getPrice()) // Set product price from ProductDTO
                            .build();
                })
                .collect(Collectors.toList());

        // Build the Order DTO
        OrderDTO orderDTO = orderMapper.toDTO(order);
        orderDTO.setCustomerId(customer.getId());
        orderDTO.setItems(itemDTOs);

        // Sent order confirmation to Kafka
        orderProducer.sendOrderConfirmation(OrderConfirmation.builder()
                .orderId(order.getId())
                .amount(order.getAmount())
                .status(order.getStatus().name())
                .customer(customer)
                .products(products)
                .build());
        return orderDTO;
    }

    @Override
    public OrderDTO update(OrderRequest request, Long id) throws MyNotSaveException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public OrderDTO fetchById(Long id) throws MyNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fetchById'");
    }

    @Override
    public void delete(Long id) throws MyNotDeleteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public List<OrderDTO> fetchList() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fetchList'");
    }

    @Override
    public Page<OrderDTO> fetchAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fetchAll'");
    }

    @Override
    public Page<OrderDTO> search(String keyword, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }

    public CustomerDTO testCustomerMicroservice(String customerID) throws MyNotSaveException {
        // Get customer information from customer microservices
        return customerService.findById(customerID);
    }

}
