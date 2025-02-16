package org.micromall.order.modules.product;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
public record PurchaseRequest(
        @NotNull(message = "Product is mandatory")
        Long productId,
        @Positive(message = "Quantity is mandatory")
        Integer quantity
) {
}