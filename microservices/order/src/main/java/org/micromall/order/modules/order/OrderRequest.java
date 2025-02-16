package org.micromall.order.modules.order;

import java.util.List;

import org.micromall.order.modules.product.PurchaseRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequest(

    @Positive(message = "Order amount should be positive")
    Double amount,
    @NotNull(message = "Customer should be present")
    @NotEmpty(message = "Customer should be present")
    @NotBlank(message = "Customer should be present")
    String customerId,
    @NotEmpty(message = "You should at least purchase one product")
    List<PurchaseRequest> products
) {

}
