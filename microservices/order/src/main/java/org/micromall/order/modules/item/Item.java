package org.micromall.order.modules.item;

import org.micromall.order.modules.order.Order;
import org.micromall.order.utils.MyEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class Item extends MyEntity {

    private Long productId;
    private Integer quantity;
    private Double itemAmount;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
