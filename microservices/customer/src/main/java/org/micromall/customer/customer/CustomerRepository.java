package org.micromall.customer.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {


    // Query method to search client by firstName, lastName, email, phone, or
    // address
    @Query("{$or: ["
            + "{firstName: {$regex: ?0, $options: 'i'}},"
            + "{lastName: {$regex: ?0, $options: 'i'}},"
            + "{email: {$regex: ?0, $options: 'i'}},"
            + "{phone: {$regex: ?0, $options: 'i'}},"
            + "{'address.street': {$regex: ?0, $options: 'i'}},"
            + "{'address.city': {$regex: ?0, $options: 'i'}},"
            + "{'address.state': {$regex: ?0, $options: 'i'}},"
            + "{'address.zip': {$regex: ?0, $options: 'i'}},"
            + "{'address.country': {$regex: ?0, $options: 'i'}}"
            + "]}")
    Page<Customer> search(String keyword, Pageable pageable);

}
