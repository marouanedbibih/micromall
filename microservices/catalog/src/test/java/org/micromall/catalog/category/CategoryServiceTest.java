package org.micromall.catalog.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.micromall.catalog.exception.MyAlreadyExists;
import org.micromall.catalog.exception.MyNotDeleteException;
import org.micromall.catalog.exception.MyNotFoundException;
import org.micromall.catalog.modules.category.Category;
import org.micromall.catalog.modules.category.CategoryDTO;
import org.micromall.catalog.modules.category.CategoryMapper;
import org.micromall.catalog.modules.category.CategoryRepository;
import org.micromall.catalog.modules.category.CategoryRequest;
import org.micromall.catalog.modules.category.CategoryService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    private CategoryRequest categoryRequest;
    private Category category;
    private CategoryDTO categoryDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryRequest = CategoryRequest.builder()
                .title("Test Category")
                .description("Test Description")
                .build();
        category = Category.builder()
                .id(1L)
                .title("Test Category")
                .description("Test Description")
                .build();
        categoryDTO = CategoryDTO.builder()
                .id(1L)
                .title("Test Category")
                .description("Test Description")
                .build();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void createCategoryTest() throws Exception {
        when(categoryRepository.existsByTitle(any())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDTO(any(Category.class))).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.create(categoryRequest);

        assertEquals(categoryDTO, result);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void updateCategoryTest() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByTitleAndIdNot(any(), any())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDTO(any(Category.class))).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.update(categoryRequest, 1L);

        assertEquals(categoryDTO, result);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void deleteCategoryTest() throws MyNotDeleteException {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.delete(1L);

        verify(categoryRepository).delete(category);
    }

    @Test
    void fetchCategoryByIdTest() throws MyNotFoundException {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDTO(any(Category.class))).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.fetchById(1L);

        assertEquals(categoryDTO, result);
    }

    @Test
    void fetchCategoryListTest() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toDTOList(anyList())).thenReturn(List.of(categoryDTO));

        List<CategoryDTO> result = categoryService.fetchList();

        assertEquals(1, result.size());
        assertEquals(categoryDTO, result.get(0));
    }

    // Fetch all categories
    @Test
    void fetchAllTest() {
        // Prepare a mock page of Category entities
        Page<Category> categoryPage = new PageImpl<>(Collections.singletonList(category), pageable, 1);

        // Mock repository behavior
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        // Call the service method
        Page<CategoryDTO> result = categoryService.fetchAll(pageable);

        // Verify results
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(categoryDTO);

        // Verify the repository method was called
        verify(categoryRepository).findAll(pageable);
    }

    // Search categories
    @Test
    void searchCategoriesTest() {
        // Prepare a mock page of Category entities
        Page<Category> categoryPage = new PageImpl<>(Collections.singletonList(category), pageable, 1);

        // Mock repository behavior
        when(categoryRepository.search("Test", pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        // Call the service method
        Page<CategoryDTO> result = categoryService.search("Test", pageable);

        // Verify results
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(categoryDTO);

        // Verify the repository method was called
        verify(categoryRepository).search("Test", pageable);
    }

}
