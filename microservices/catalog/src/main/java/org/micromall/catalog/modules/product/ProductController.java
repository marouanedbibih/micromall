package org.micromall.catalog.modules.product;

import java.util.List;

import org.micromall.catalog.modules.product.DTO.PurchaseProducts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Endpoint to create a new products
    @PostMapping("/api/v1/product")
    public ResponseEntity<ProductDTO> create(@RequestBody @Valid ProductRequest request) {
        ProductDTO product = productService.create(request);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    // Update a product
    @PutMapping("/api/v1/product/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody @Valid ProductRequest request) {
        ProductDTO product = productService.update(request, id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    // Delete a product
    @DeleteMapping("/api/v1/product/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Fetch product by id
    @GetMapping("/api/v1/product/{id}")
    public ResponseEntity<ProductDTO> fetch(@PathVariable Long id) {
        ProductDTO product = productService.fetchById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    // Fetch products list
    @GetMapping("/api/v1/products/list")
    public ResponseEntity<List<ProductDTO>> fetchAll() {
        List<ProductDTO> products = productService.fetchList();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    // Fetch page of products
    @GetMapping("/api/v1/products")
    public ResponseEntity<Page<ProductDTO>> fetchPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ProductDTO> products = productService.fetchAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    // GetMapping for purchase products
    @GetMapping("/api/v1/products/purchase")
    public ResponseEntity<List<PurchaseProducts>> fetchPurchaseProducts(@RequestParam List<Long> ids) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.purchaseProducts(ids));
    }
}
