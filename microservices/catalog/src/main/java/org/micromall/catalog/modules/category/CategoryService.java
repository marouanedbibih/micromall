package org.micromall.catalog.modules.category;

import java.util.List;

import org.micromall.catalog.exception.MyAlreadyExists;
import org.micromall.catalog.exception.MyNotDeleteException;
import org.micromall.catalog.exception.MyNotFoundException;
import org.micromall.catalog.exception.MyNotSaveException;
import org.micromall.catalog.interfaces.IDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements IDaoService<Category, CategoryDTO, CategoryRequest, CategoryRequest, Long> {

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDTO create(CategoryRequest request) throws MyNotSaveException, MyAlreadyExists {
        // Check if the catgory title is already present
        if (categoryRepository.existsByTitle(request.title())) {
            logger.error("Category title already exists");
            throw new MyAlreadyExists("Category title already exists", "title");
        }
        // Create a new category
        Category category = Category.builder()
                .title(request.title())
                .description(request.description())
                .build();
        // Save the category
        category = categoryRepository.save(category);
        // Return the category DTO
        return categoryMapper.toDTO(category);
    }

    @Override
    public CategoryDTO update(CategoryRequest request, Long id) throws MyNotSaveException, MyAlreadyExists {
        // Fetch the category by id
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new MyNotFoundException("Category not found"));
        // Check if the catgory title is already present and not the same as the current
        // category
        if (categoryRepository.existsByTitleAndIdNot(request.title(), id)) {
            logger.error("Category title already exists");
            throw new MyAlreadyExists("Category title already exists", "title");
        }
        // Update the category
        category.setTitle(request.title());
        category.setDescription(request.description());
        // Save the category
        category = categoryRepository.save(category);
        // Return the category DTO
        return categoryMapper.toDTO(category);
    }

    @Override
    public CategoryDTO fetchById(Long id) throws MyNotFoundException {
        // Fetch the category by id
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new MyNotFoundException("Category not found"));
        // Return the category DTO
        return categoryMapper.toDTO(category);
    }

    @Override
    public void delete(Long id) throws MyNotDeleteException {
        // Fetch the category by id
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new MyNotFoundException("Category not found"));
        // Delete the category
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDTO> fetchList() {
        // Fetch all categories
        List<Category> categories = categoryRepository.findAll();
        // Convert the categories to category DTOs
        return categoryMapper.toDTOList(categories);
    }

    @Override
    public Page<CategoryDTO> fetchAll(Pageable pageable) {
        // Fetch all categories
        Page<Category> categories = categoryRepository.findAll(pageable);
        // Convert the categories to category DTOs
        return categories.map(categoryMapper::toDTO);
    }

    @Override
    public Page<CategoryDTO> search(String keyword, Pageable pageable) {
        // Search for categories by title
        Page<Category> categories = categoryRepository.search(keyword, pageable);
        // Convert the categories to category DTOs
        return categories.map(categoryMapper::toDTO);
    }

}
