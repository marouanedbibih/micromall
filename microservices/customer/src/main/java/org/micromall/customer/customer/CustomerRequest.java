package org.micromall.customer.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;


@Builder
public record CustomerRequest(
    
        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name must be less than 50 characters")
        String lastName,
        
        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name must be less than 50 characters")
        String firstName,
        
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,
        
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
        String phone,
        
        Address address
) {}