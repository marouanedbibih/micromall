package org.micromall.catalog.modules.category;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    // Convert Category entity to CategoryDTO
    public CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDTO.builder()
                .id(category.getId())
                .title(category.getTitle())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    // Convert CategoryDTO to Category entity
    public Category toEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setTitle(categoryDTO.getTitle());
        category.setDescription(categoryDTO.getDescription());
        category.setCreatedAt(categoryDTO.getCreatedAt());
        category.setUpdatedAt(categoryDTO.getUpdatedAt());
        return category;
    }

    // Convert CategoryRequest to Category entity
    public Category toEntity(CategoryRequest categoryRequest) {
        if (categoryRequest == null) {
            return null;
        }
        return Category.builder()
                .title(categoryRequest.title())
                .description(categoryRequest.description())
                .build();
    }

    // Convert CategoryRequest to CategoryDTO
    public CategoryDTO toDTO(CategoryRequest categoryRequest) {
        if (categoryRequest == null) {
            return null;
        }
        return CategoryDTO.builder()
                .title(categoryRequest.title())
                .description(categoryRequest.description())
                .build();
    }

    // Convert a list of Category entities to a list of CategoryDTOs
    public List<CategoryDTO> toDTOList(List<Category> categories) {
        return categories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Convert a list of CategoryDTOs to a list of Category entities
    public List<Category> toEntityList(List<CategoryDTO> categoryDTOs) {
        return categoryDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
