package org.micromall.catalog.interfaces;

import java.util.List;

public interface IMapper<E, DTO, REQ> {
    E toEntity(DTO dto);

    DTO toDTO(E entity);

    List<E> toEntityList(List<DTO> dtos);

    List<DTO> toDTOList(List<E> entities);

    E fromRequestToEntity(REQ req);

    DTO fromRequestToDTO(REQ req);

}
