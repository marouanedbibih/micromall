package org.micromall.catalog.modules.product;

import java.util.List;
import java.util.stream.Collectors;

import org.micromall.catalog.exception.MyNotDeleteException;
import org.micromall.catalog.exception.MyNotFoundException;
import org.micromall.catalog.exception.MyNotSaveException;
import org.micromall.catalog.interfaces.IDaoService;
import org.micromall.catalog.modules.category.Category;
import org.micromall.catalog.modules.category.CategoryRepository;
import org.micromall.catalog.modules.product.DTO.PurchaseProducts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IDaoService<Product, ProductDTO, ProductRequest, ProductRequest, Long> {

    Logger logger = LoggerFactory.getLogger(getClass().getName());

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductDTO create(ProductRequest request) throws MyNotSaveException {

        // Check if the category exists
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> {
                    logger.error("Category with id {} not found", request.categoryId());
                    return new MyNotFoundException("Category not found");
                });
        // Save the
        Product product = productMapper.fromRequestToEntity(request);
        product.setCategory(category);
        product = productRepository.save(product);
        return productMapper.toDTO(product);
    }

    @Override
    public ProductDTO update(ProductRequest request, Long id) throws MyNotSaveException {

        Product product = productRepository.findById(id).orElseThrow(() -> {
            logger.error("Product with id {} not found", id);
            return new MyNotFoundException("Product not found");
        });

        if (!product.getCategory().getId().equals(request.categoryId())) {
            Category category = categoryRepository.findById(request.categoryId()).orElseThrow(() -> {
                logger.error("Category with id {} not found", request.categoryId());
                return new MyNotFoundException("Category not found");
            });
            product.setCategory(category);
        }

        product.setTitle(request.title());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product = productRepository.save(product);
        return productMapper.toDTO(product);
    }

    @Override
    public ProductDTO fetchById(Long id) throws MyNotFoundException {

        Product product = productRepository.findById(id).orElseThrow(() -> {
            logger.error("Product with id {} not found", id);
            return new MyNotFoundException("Product not found");
        });

        return productMapper.fromEntityToDTOWithCategoryDetails(product);
    }

    @Override
    public void delete(Long id) throws MyNotDeleteException {
        Product product = productRepository.findById(id).orElseThrow(() -> {
            logger.error("Product with id {} not found", id);
            return new MyNotFoundException("Product not found");
        });
        productRepository.delete(product);
    }

    public List<ProductDTO> fetchList() {
        return productRepository.findAll().stream()
                .map(productMapper::fromEntityToDTOWithCategoryDetails)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> fetchAll(Pageable pageable) {
        Page<Product> productsPage = productRepository.findAll(pageable);
        return productsPage.map(productMapper::toDTO);
    }

    @Override
    public Page<ProductDTO> search(String keyword, Pageable pageable) {
        Page<Product> productsPage = productRepository.searchByKeyword(keyword, pageable);
        return productsPage.map(productMapper::toDTO);
    }

    public List<PurchaseProducts> purchaseProducts(List<Long> ids) {
        return productRepository.findAllById(ids).stream()
                .map(product -> {
                    return PurchaseProducts.builder()
                            .id(product.getId())
                            .title(product.getTitle())
                            .price(product.getPrice())
                            .build();
                })
                .collect(Collectors.toList());
    }

}
