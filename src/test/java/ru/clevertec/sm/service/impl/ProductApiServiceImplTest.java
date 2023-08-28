package ru.clevertec.sm.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.sm.dto.Product;
import ru.clevertec.sm.service.ProductApiService;
import ru.clevertec.sm.util.TestData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductApiServiceImplTest {

    @Autowired
    private ProductApiService productService;

    @Test
    void checkFetchCategoriesShouldReturnNotEmptyList() {
        List<String> categories = productService.fetchSortedCategories();
        assertThat(categories).containsAll(TestData.getCategories());
    }

    @Test
    void fetchProductsByCategory() {
        var categories = TestData.getCategories();

        for (String category : categories) {
            List<Product> products = productService.fetchProductsByCategory(category);

            assertThat(products).isNotNull();
            assertThat(products).isNotEmpty();
            assertThat(products).allMatch(p -> category.equals(p.getCategory()));
        }
    }
}