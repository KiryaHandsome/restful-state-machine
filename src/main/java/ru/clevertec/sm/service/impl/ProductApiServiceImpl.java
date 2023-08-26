package ru.clevertec.sm.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.clevertec.sm.dto.Product;
import ru.clevertec.sm.dto.ProductResponse;
import ru.clevertec.sm.service.ProductApiService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class ProductApiServiceImpl implements ProductApiService {

    private final String categoriesUrl;
    private final RestTemplate restTemplate;
    private final String productsByCategoryUrl;

    public ProductApiServiceImpl(
            RestTemplate restTemplate,
            @Value("${productApi.categoryUrl}") String productsByCategoryUrl,
            @Value("${productApi.categoriesUrl}") String categoriesUrl
    ) {
        this.restTemplate = restTemplate;
        this.categoriesUrl = categoriesUrl;
        this.productsByCategoryUrl = productsByCategoryUrl;
    }

    @Override
    public List<String> fetchSortedCategories() {
        Optional<String[]> categories = Optional.ofNullable(
                restTemplate.getForObject(
                        categoriesUrl,
                        String[].class
                )
        );

        return categories
                .map(Arrays::asList)
                .orElseGet(Collections::emptyList)
                .stream()
                .sorted()
                .toList();
    }

    @Override
    public List<Product> fetchProductsByCategory(String category) {
        Optional<ProductResponse> productResponse = Optional.ofNullable(
                restTemplate.getForObject(
                        productsByCategoryUrl + category,
                        ProductResponse.class
                )
        );

        return productResponse
                .map(ProductResponse::getProducts)
                .orElse(Collections.emptyList());
    }
}
