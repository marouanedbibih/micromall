package org.micromall.order.modules.item;


import ch.qos.logback.core.joran.spi.NoAutoStart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@NoAutoStart
public class ItemDTO {

    private Long id;
    private Long productId;
    private String title;
    private Double price;
    private Integer quantity;

}
