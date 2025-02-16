package org.micromall.catalog.modules.product.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseProducts {
    private Long id;
    private String title;
    private Double price;
}
