package org.micromall.customer.customer;

import java.util.List;

import org.micromall.customer.exception.MyNotSaveException;
import org.micromall.customer.interfaces.IDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService implements IDaoService<Customer, CustomerDTO, CustomerRequest, CustomerRequest, String> {

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    public CustomerDTO create(CustomerRequest request) throws MyNotSaveException {
        Customer entity = mapper.toEntity(request);
        try {
            entity = repository.save(entity);
        } catch (Exception e) {
            logger.error("Customer not saved Error :{} ", e);
            throw new MyNotSaveException("Customer not saved try again");
        }
        return mapper.toDTO(entity);
    }

    @Override
    public CustomerDTO update(CustomerRequest request, String id) throws MyNotSaveException {
        Customer entity = repository.findById(id)
                .orElseThrow(() -> new MyNotSaveException("Customer to update not found"));
        // update entity
        entity.setFirstName(request.firstName());
        entity.setLastName(request.lastName());
        entity.setEmail(request.email());
        entity.setPhone(request.phone());
        entity.setAddress(request.address());
        try {
            entity = repository.save(entity);
        } catch (Exception e) {
            logger.error("Customer not saved Error :{} ", e);
            throw new MyNotSaveException("Customer not saved try again");
        }
        return mapper.toDTO(entity);

    }

    @Override
    public CustomerDTO fetchById(String id) throws MyNotSaveException {
        Customer entity = repository.findById(id)
                .orElseThrow(() -> new MyNotSaveException("Customer not found"));
        return mapper.toDTO(entity);
    }

    @Override
    public void delete(String id) throws MyNotSaveException {
        Customer entity = repository.findById(id)
                .orElseThrow(() -> new MyNotSaveException("Customer to delete not found"));
        repository.delete(entity);
    }

    @Override
    public List<CustomerDTO> fetchList() {
        List<CustomerDTO> customers = repository.findAll().stream().map(mapper::toDTO).toList();
        return customers;
    }

    @Override
    public Page<CustomerDTO> fetchAll(Pageable pageable) {
        Page<CustomerDTO> customers = repository.findAll(pageable).map(mapper::toDTO);
        return customers;
    }

    @Override
    public Page<CustomerDTO> search(String keyword, Pageable pageable) {
        Page<CustomerDTO> customers = repository.search(keyword, pageable).map(mapper::toDTO);
        return customers;
    }

}
