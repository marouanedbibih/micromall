package org.micromall.order.modules.product;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "catalog-service")
public interface ProductService {

    @GetMapping("/api/v1/product/{id}")
    ProductDTO fetchById(@PathVariable Long id);

    @GetMapping("/api/v1/products/purchase")
    List<ProductDTO> fetchPurchaseProducts(@RequestParam List<Long> ids);
}
