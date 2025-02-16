package org.micromall.catalog.modules.category;

import java.util.Map;

import org.micromall.catalog.utils.MyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Endpoint to create a new category
    @PostMapping("/api/v1/category")
    public ResponseEntity<MyResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        CategoryDTO categoryDTO = categoryService.create(request);
        return ResponseEntity.ok(MyResponse.builder()
                .message("Category created successfully")
                .data(categoryDTO)
                .build());
    }

    // Endpoint to update an existing category
    @PutMapping("/api/v1/category/{id}")
    public ResponseEntity<MyResponse> updateCategory(
            @RequestBody @Valid CategoryRequest request,
            @PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.update(request, id);
        return ResponseEntity.ok(MyResponse.builder()
                .message("Category updated successfully")
                .data(categoryDTO)
                .build());
    }

    // Endpoint to fetch a category by ID
    @GetMapping("/api/v1/category/{id}")
    public ResponseEntity<MyResponse> getCategory(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.fetchById(id);
        return ResponseEntity.ok(MyResponse.builder()
                .message("Category fetched successfully")
                .data(categoryDTO)
                .build());
    }

    // Endpoint to delete a category by ID
    @DeleteMapping("/api/v1/category/{id}")
    public ResponseEntity<MyResponse> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(MyResponse.builder()
                .message("Category deleted successfully")
                .build());
    }

    /**
     * Endpoint to fetch all categories
     * 
     * @param int    page
     * @param int    size
     * @param String sortBy
     * @param String orderBy
     * @param String search
     */
    @GetMapping("/api/v1/categories")
    public ResponseEntity<MyResponse> getAllCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String orderBy,
            @RequestParam(required = false) String search) {

        // Create a pageable object
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.valueOf(orderBy.toUpperCase()), sortBy);

        // Fetch all categories by conditions
        Page<CategoryDTO> categoriesPage;
        if (search == null) {
            categoriesPage = categoryService.fetchAll(pageable);
        } else {
            categoriesPage = categoryService.search(search, pageable);
        }

        // Create a meta object
        Map<String, Object> meta = Map.of(
                "page", categoriesPage.getNumber() + 1,
                "size", categoriesPage.getSize(),
                "total", categoriesPage.getTotalElements(),
                "totalPages", categoriesPage.getTotalPages());

        return ResponseEntity.ok(MyResponse.builder()
                .message("Categories fetched successfully")
                .data(categoriesPage.getContent())
                .meta(meta)
                .build());
    }
}
