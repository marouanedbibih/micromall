package org.micromall.customer.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDaoService<E, DTO, CREQ, UREQ, ID> {

    DTO create(CREQ request) throws RuntimeException;

    DTO update(UREQ request,ID id) throws RuntimeException;

    DTO fetchById(ID id) throws RuntimeException;

    void delete(ID id) throws RuntimeException;

    List<DTO> fetchList();

    Page<DTO> fetchAll(Pageable pageable);

    Page<DTO> search(String keyword, Pageable pageable);

}
