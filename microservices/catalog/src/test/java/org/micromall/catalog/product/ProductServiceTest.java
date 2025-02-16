// package org.micromall.catalog.product;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.*;
// import org.micromall.catalog.exception.MyNotDeleteException;
// import org.micromall.catalog.exception.MyNotFoundException;
// import org.micromall.catalog.exception.MyNotSaveException;
// import org.micromall.catalog.modules.category.Category;
// import org.micromall.catalog.modules.category.CategoryRepository;
// import org.micromall.catalog.modules.product.*;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;

// import java.util.Optional;
// import java.util.List;

// import static org.mockito.Mockito.*;
// import static org.junit.jupiter.api.Assertions.*;

// class ProductServiceTest {

//     @Mock
//     private ProductRepository productRepository;
    
//     @Mock
//     private ProductMapper productMapper;
    
//     @Mock
//     private CategoryRepository categoryRepository;

//     @InjectMocks
//     private ProductService productService;

//     private ProductRequest productRequest;
//     private Product product;
//     private Category category;
//     private ProductDTO productDTO;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
        
//         productRequest = new ProductRequest("Product", "Description", 100.0, 1L);
//         category = new Category(1L, "Category");
//         Category category = Category.
//         product = new Product(1L, "Product", "Description", 100.0, category);
//         productDTO = new ProductDTO(1L, "Product", "Description", 100.0, "Category");

//         when(productMapper.fromRequestToEntity(productRequest)).thenReturn(product);
//         when(productMapper.toDTO(product)).thenReturn(productDTO);
//         when(productMapper.fromEntityToDTOWithCategoryDetails(product)).thenReturn(productDTO);
//     }

//     @Test
//     void create_shouldReturnProductDTO_whenValidRequest() throws MyNotSaveException {
//         when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
//         when(productRepository.save(any(Product.class))).thenReturn(product);

//         ProductDTO result = productService.create(productRequest);

//         assertNotNull(result);
//         assertEquals("Product", result.getTitle());
//         verify(productRepository).save(any(Product.class));
//     }

//     @Test
//     void create_shouldThrowException_whenCategoryNotFound() {
//         when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

//         MyNotFoundException exception = assertThrows(MyNotFoundException.class, () -> {
//             productService.create(productRequest);
//         });

//         assertEquals("Category not found", exception.getMessage());
//     }

//     @Test
//     void update_shouldReturnUpdatedProductDTO_whenValidRequest() throws MyNotSaveException {
//         when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//         when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
//         when(productRepository.save(any(Product.class))).thenReturn(product);

//         ProductDTO result = productService.update(productRequest, 1L);

//         assertNotNull(result);
//         assertEquals("Product", result.getTitle());
//         verify(productRepository).save(any(Product.class));
//     }

//     @Test
//     void update_shouldThrowException_whenProductNotFound() {
//         when(productRepository.findById(1L)).thenReturn(Optional.empty());

//         MyNotFoundException exception = assertThrows(MyNotFoundException.class, () -> {
//             productService.update(productRequest, 1L);
//         });

//         assertEquals("Product not found", exception.getMessage());
//     }

//     @Test
//     void fetchById_shouldReturnProductDTO_whenProductFound() throws MyNotFoundException {
//         when(productRepository.findById(1L)).thenReturn(Optional.of(product));

//         ProductDTO result = productService.fetchById(1L);

//         assertNotNull(result);
//         assertEquals("Product", result.getTitle());
//     }

//     @Test
//     void fetchById_shouldThrowException_whenProductNotFound() {
//         when(productRepository.findById(1L)).thenReturn(Optional.empty());

//         MyNotFoundException exception = assertThrows(MyNotFoundException.class, () -> {
//             productService.fetchById(1L);
//         });

//         assertEquals("Product not found", exception.getMessage());
//     }

//     @Test
//     void delete_shouldDeleteProduct_whenProductExists() throws MyNotDeleteException {
//         when(productRepository.findById(1L)).thenReturn(Optional.of(product));

//         productService.delete(1L);

//         verify(productRepository).delete(any(Product.class));
//     }

//     @Test
//     void delete_shouldThrowException_whenProductNotFound() {
//         when(productRepository.findById(1L)).thenReturn(Optional.empty());

//         MyNotFoundException exception = assertThrows(MyNotFoundException.class, () -> {
//             productService.delete(1L);
//         });

//         assertEquals("Product not found", exception.getMessage());
//     }

//     @Test
//     void fetchList_shouldReturnListOfProducts() {
//         when(productRepository.findAll()).thenReturn(List.of(product));

//         List<ProductDTO> result = productService.fetchList();

//         assertNotNull(result);
//         assertEquals(1, result.size());
//     }

//     @Test
//     void fetchAll_shouldReturnPagedProducts() {
//         Page<Product> productPage = new PageImpl<>(List.of(product));
//         when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

//         Page<ProductDTO> result = productService.fetchAll(Pageable.unpaged());

//         assertNotNull(result);
//         assertEquals(1, result.getContent().size());
//     }

//     @Test
//     void search_shouldReturnSearchedProducts() {
//         Page<Product> productPage = new PageImpl<>(List.of(product));
//         when(productRepository.searchByKeyword(anyString(), any(Pageable.class))).thenReturn(productPage);

//         Page<ProductDTO> result = productService.search("Product", Pageable.unpaged());

//         assertNotNull(result);
//         assertEquals(1, result.getContent().size());
//     }
// }
