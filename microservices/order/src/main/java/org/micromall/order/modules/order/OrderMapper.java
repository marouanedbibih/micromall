package org.micromall.order.modules.order;

import java.util.List;

import org.micromall.order.interfaces.IMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper implements IMapper<Order, OrderDTO, OrderRequest> {

    @Override
    public Order toEntity(OrderDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toEntity'");
    }

    @Override
    public OrderDTO toDTO(Order entity) {
        if (entity == null) {
            return null;
        }

        return OrderDTO.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .status(entity.getStatus())
                .build();
    }

    @Override
    public List<Order> toEntityList(List<OrderDTO> dtos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toEntityList'");
    }

    @Override
    public List<OrderDTO> toDTOList(List<Order> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toDTOList'");
    }

    @Override
    public Order fromRequestToEntity(OrderRequest req) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRequestToEntity'");
    }

    @Override
    public OrderDTO fromRequestToDTO(OrderRequest req) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRequestToDTO'");
    }

}
