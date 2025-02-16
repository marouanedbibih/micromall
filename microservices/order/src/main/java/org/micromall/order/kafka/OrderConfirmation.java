package org.micromall.order.kafka;

import java.util.List;

import org.micromall.order.modules.customer.CustomerDTO;
import org.micromall.order.modules.product.ProductDTO;

import lombok.Builder;

@Builder
public record OrderConfirmation(
    Long orderId,
    String status,
    Double amount,
    CustomerDTO customer,
    List<ProductDTO> products
) {
    
}
