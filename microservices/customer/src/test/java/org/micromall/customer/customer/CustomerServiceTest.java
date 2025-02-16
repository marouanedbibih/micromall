package org.micromall.customer.customer;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private CustomerMapper mapper;

    @InjectMocks
    private CustomerService customerService;

    private CustomerRequest customerRequest;
    private Customer customerEntity;
    private CustomerDTO customerDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Initialize test data
        Address address = new Address("123 Main St", "Anytown", "Anystate", "12345", "Country");
        customerRequest = new CustomerRequest("Doe", "John", "john.doe@example.com", "+123456789", address);
        customerEntity = new Customer("1", "Doe", "John", "john.doe@example.com", "+123456789", address);
        customerDTO = new CustomerDTO("1", "Doe", "John", "john.doe@example.com", "+123456789", address);
        pageable = Pageable.ofSize(10); // Initialize pageable for pagination tests
    }

    @Test
    void createCustomerTest() {
        when(mapper.toEntity(customerRequest)).thenReturn(customerEntity);
        when(repository.save(customerEntity)).thenReturn(customerEntity);
        when(mapper.toDTO(customerEntity)).thenReturn(customerDTO);

        CustomerDTO result = customerService.create(customerRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getFirstName()).isEqualTo("John");

        verify(repository).save(customerEntity);
    }

    @Test
    void updateCustomerTest() {
        // Arrange
        when(repository.findById("1")).thenReturn(Optional.of(customerEntity));
        when(repository.save(customerEntity)).thenReturn(customerEntity);
        when(mapper.toDTO(customerEntity)).thenReturn(customerDTO);

        // Act
        CustomerDTO result = customerService.update(customerRequest, "1");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");

        verify(repository).findById("1");
        verify(repository).save(customerEntity);
        verify(mapper).toDTO(customerEntity); // Ensure mapper.toDTO is called with the correct argument
    }

    @Test
    void fetchByIdTest() {
        when(repository.findById("1")).thenReturn(Optional.of(customerEntity));
        when(mapper.toDTO(customerEntity)).thenReturn(customerDTO);

        CustomerDTO result = customerService.fetchById("1");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");

        verify(repository).findById("1");
    }

    @Test
    void deleteTest() {
        when(repository.findById("1")).thenReturn(Optional.of(customerEntity));

        customerService.delete("1");

        verify(repository).delete(customerEntity);
    }

    @Test
    void fetchListTest() {
        when(repository.findAll()).thenReturn(List.of(customerEntity));
        when(mapper.toDTO(customerEntity)).thenReturn(customerDTO);

        List<CustomerDTO> result = customerService.fetchList();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getLastName()).isEqualTo("Doe");

        verify(repository).findAll();
    }

    @Test
    void fetchAllTest() {
        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(customerEntity), pageable, 1);
        when(repository.findAll(pageable)).thenReturn(customerPage);
        when(mapper.toDTO(customerEntity)).thenReturn(customerDTO);

        Page<CustomerDTO> result = customerService.fetchAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(customerDTO);
        verify(repository).findAll(pageable);
    }

    @Test
    void searchTest() {
        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(customerEntity), pageable, 1);
        when(repository.search("keyword", pageable)).thenReturn(customerPage);
        when(mapper.toDTO(customerEntity)).thenReturn(customerDTO);

        Page<CustomerDTO> result = customerService.search("keyword", pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(customerDTO);
        verify(repository).search("keyword", pageable);
    }
}
