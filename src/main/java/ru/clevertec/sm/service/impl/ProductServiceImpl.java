package ru.clevertec.sm.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.clevertec.sm.dto.Product;
import ru.clevertec.sm.dto.ProductResponse;
import ru.clevertec.sm.service.ProductService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImpl implements ProductService {

    private final String productsByCategoryURL;
    private final String categoriesURL;
    private final RestTemplate restTemplate;

    public ProductServiceImpl(@Value("${source.urls.productsByCategory}") String productsByCategoryURL,
                              @Value("${source.urls.categories}") String categoriesURL,
                              RestTemplate restTemplate) {
        this.productsByCategoryURL = productsByCategoryURL;
        this.categoriesURL = categoriesURL;
        this.restTemplate = restTemplate;
    }

    /**
     * Fetch category from {@link ProductServiceImpl#categoriesURL}
     *
     * @return list of categories
     */
    @Override
    public List<String> fetchCategories() {
        ResponseEntity<String[]> response = restTemplate.getForEntity(
                categoriesURL,
                String[].class
        );

        return Optional.ofNullable(response.getBody())
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    /**
     * Fetch products by its category from {@link ProductServiceImpl#productsByCategoryURL}
     *
     * @param category category to fetch
     * @return list of products with such category
     */
    @Override
    public List<Product> fetchProductsByCategory(String category) {
        ResponseEntity<ProductResponse> response = restTemplate.getForEntity(
                productsByCategoryURL + category,
                ProductResponse.class
        );

        return Optional.ofNullable(response.getBody())
                .map(ProductResponse::getProducts)
                .orElse(Collections.emptyList());
    }
}
