package ru.clevertec.sm.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.sm.dto.Product;
import ru.clevertec.sm.service.ProductService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @Test
    void checkFetchCategoriesShouldReturnNotEmptyList() {
        List<String> categories = productService.fetchCategories();

        Assertions.assertThat(categories).isNotNull();
        Assertions.assertThat(categories).isNotEmpty();
    }

    @Test
    void fetchProductsByCategory() {
        String category = productService.fetchCategories().get(0);

        List<Product> products = productService.fetchProductsByCategory(category);

        Assertions.assertThat(products).isNotNull();
        Assertions.assertThat(products).isNotEmpty();
    }
}