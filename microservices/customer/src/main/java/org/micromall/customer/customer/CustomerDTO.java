package org.micromall.customer.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO {
    private String id;
    private String lastName;
    private String firstName;
    private String email;
    private String phone;
    private Address address;
    
}
