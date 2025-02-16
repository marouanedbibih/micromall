package org.micromall.catalog.modules.product;

import lombok.Builder;

@Builder
public record ProductRequest(
    String title,
    String description,
    Double price,
    Long categoryId
) {
    
}
