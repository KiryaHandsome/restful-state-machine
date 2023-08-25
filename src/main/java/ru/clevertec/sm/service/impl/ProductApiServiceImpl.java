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


@Service
public class ProductApiServiceImpl implements ProductApiService {

    private final String baseUrl;
    private final String categoriesUri;
    private final RestTemplate restTemplate;
    private final String productsByCategoryUri;

    public ProductApiServiceImpl(
            RestTemplate restTemplate,
            @Value("${productApi.baseUrl}") String baseUrl,
            @Value("${productApi.categoryUri}") String productsByCategoryUri,
            @Value("${productApi.categoriesUri}") String categoriesUri
    ) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
        this.categoriesUri = categoriesUri;
        this.productsByCategoryUri = productsByCategoryUri;
    }

    @Override
    public List<String> fetchSortedCategories() {
        String[] categories = restTemplate.getForObject(categoriesUri, String[].class);
        return Arrays.stream(categories)
                .sorted()
                .toList();
    }

    @Override
    public List<Product> fetchProductsByCategory(String category) {
        ProductResponse productResponse = restTemplate.getForObject(productsByCategoryUri + category, ProductResponse.class);
        return productResponse != null ? productResponse.getProducts() : Collections.emptyList();
    }
}
