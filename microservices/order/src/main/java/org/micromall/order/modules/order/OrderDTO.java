package org.micromall.order.modules.order;

import java.time.LocalDateTime;
import java.util.List;

import org.micromall.order.modules.item.ItemDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDTO {
    private Long id;
    private Double amount;
    private OrderStatus status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private String customerId;
    private List<ItemDTO> items;
}
