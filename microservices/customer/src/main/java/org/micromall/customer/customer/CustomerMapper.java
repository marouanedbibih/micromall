package org.micromall.customer.customer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    // Entity to DTO
    CustomerDTO toDTO(Customer customer);

    // DTO to Entity
    Customer toEntity(CustomerDTO dto);

    // Request to DTO
    @Mapping(target = "id", ignore = true)
    CustomerDTO toDTO(CustomerRequest request);

    // Request to Entity
    @Mapping(target = "id", ignore = true)
    Customer toEntity(CustomerRequest request);
}