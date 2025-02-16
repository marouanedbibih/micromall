package org.micromall.catalog.modules.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CategoryRequest(
        
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title must be less than 100 characters")
        String title,

        @Size(max = 500, message = "Description must be less than 500 characters")
        String description) {

}
