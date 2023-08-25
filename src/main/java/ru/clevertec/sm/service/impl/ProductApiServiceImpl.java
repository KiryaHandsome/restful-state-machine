package ru.clevertec.sm.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.clevertec.sm.dto.Product;
import ru.clevertec.sm.dto.ProductResponse;
import ru.clevertec.sm.service.ProductApiService;

import java.util.Arrays;
import java.util.List;


@Service
public class ProductApiServiceImpl implements ProductApiService {

    private final WebClient webClient;
    private final String categoriesUri;
    private final String productsByCategoryUri;

    public ProductApiServiceImpl(@Value("${productApi.baseUrl}") String baseUrl,
                                 @Value("${productApi.categoriesUri}") String categoriesUri,
                                 @Value("${productApi.categoryUri}") String productsByCategoryUri) {
        this.categoriesUri = categoriesUri;
        this.webClient = WebClient.create(baseUrl);
        this.productsByCategoryUri = productsByCategoryUri;
    }

    /**
     * Fetch category from {@link ProductApiServiceImpl#categoriesUri}
     * sorted in alphabetical order
     *
     * @return stream of categories
     */
    @Override
    public List<String> fetchSortedCategories() {
        return webClient.get()
                .uri(categoriesUri)
                .retrieve()
                .bodyToMono(String[].class)
                .map(Arrays::asList)
                .block()
                .stream()
                .sorted()
                .toList();
    }

    /**
     * Fetch products by its category from {@link ProductApiServiceImpl#productsByCategoryUri}
     *
     * @param category category to fetch
     * @return stream of products with such category
     */
    @Override
    public List<Product> fetchProductsByCategory(String category) {
        return webClient.get()
                .uri(productsByCategoryUri + category)
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .map(ProductResponse::getProducts)
                .block();
    }
}
