package org.micromall.order.modules.order;

import org.micromall.order.modules.customer.CustomerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/api/v1/order/customer/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable String id) {
        return ResponseEntity.ok(orderService.testCustomerMicroservice(id));
    }

    @PostMapping("/api/v1/order")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody @Valid OrderRequest request) {
        OrderDTO order = orderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

}
