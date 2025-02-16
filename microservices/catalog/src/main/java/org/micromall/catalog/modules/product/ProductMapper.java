package org.micromall.catalog.modules.product;

import java.util.List;
import java.util.stream.Collectors;

import org.micromall.catalog.interfaces.IMapper;
import org.micromall.catalog.modules.category.CategoryDTO;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductMapper implements IMapper<Product, ProductDTO, ProductRequest> {

    @Override
    public Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }
        return Product.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    @Override
    public ProductDTO toDTO(Product entity) {
        if (entity == null) {
            return null;
        }
        return ProductDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    public Product fromRequestToEntity(ProductRequest req) {
        if (req == null) {
            return null;
        }
        return Product.builder()
                .title(req.title())
                .description(req.description())
                .price(req.price())
                .build();
    }

    @Override
    public ProductDTO fromRequestToDTO(ProductRequest req) {
        if (req == null) {
            return null;
        }
        return ProductDTO.builder()
                .title(req.title())
                .description(req.description())
                .price(req.price())
                .build();
    }

    @Override
    public List<Product> toEntityList(List<ProductDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream()
                .map(this::toEntity) // Reuse the toEntity method for each DTO
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> toDTOList(List<Product> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDTO) // Reuse the toDTO method for each entity
                .collect(Collectors.toList());
    }

    // Transform a Product Entity to a Product DTO with categoryDetails
    public ProductDTO fromEntityToDTOWithCategoryDetails(Product entity) {
        if (entity == null) {
            return null;
        }

        return ProductDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .category(CategoryDTO.builder()
                        .id(entity.getCategory().getId())
                        .title(entity.getTitle())
                        .build())
                .build();
    }
}
