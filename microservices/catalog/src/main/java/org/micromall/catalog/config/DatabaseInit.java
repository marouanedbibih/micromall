package org.micromall.catalog.config;

import org.micromall.catalog.modules.category.Category;
import org.micromall.catalog.modules.category.CategoryRepository;
import org.micromall.catalog.modules.product.Product;
import org.micromall.catalog.modules.product.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DatabaseInit {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final Faker faker;
    private final int numbersOfCategories = 10;
    private final int numbersOfProducts = 100;


    @Bean
    @SuppressWarnings("unused")
    CommandLineRunner DatabaseInit(CategoryRepository categoryRepository) {
        return args -> {

            this.initProductCategory(numbersOfCategories);
            this.initProduct(numbersOfProducts, numbersOfCategories);
        };
    }


    private void initProductCategory(int numbersOfCategories) {
        for (int i = 0; i < numbersOfCategories; i++) {
            Category category = Category.builder()
                    .title(faker.commerce().department())
                    .description(faker.commerce().material())

                    .build();
            categoryRepository.save(category); 
        }
 
    }

    private void  initProduct(int numbersOfProducts,int numbersOfCategories) {
        for (int i = 0; i < numbersOfProducts; i++) {
            Category category = categoryRepository.findById((long) faker.number().numberBetween(1, numbersOfCategories))
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            Product product = Product.builder()
                    .title(faker.commerce().productName())
                    .description(faker.commerce().material())
                    .price(faker.number().randomDouble(2, 10, 100))
                    .category(category)
                    .build();
            productRepository.save(product);
        }
    }
}
