package org.micromall.order.modules.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerService {

    @GetMapping("/api/v1/customer/{id}")
    CustomerDTO findById(@PathVariable String id);    
}
