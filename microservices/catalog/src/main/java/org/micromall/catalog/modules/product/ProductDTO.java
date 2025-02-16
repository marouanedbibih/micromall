package org.micromall.catalog.modules.product;

import java.time.LocalDateTime;

import org.micromall.catalog.modules.category.CategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CategoryDTO category;
}
