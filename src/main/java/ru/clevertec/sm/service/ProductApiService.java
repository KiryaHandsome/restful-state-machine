package ru.clevertec.sm.service;

import ru.clevertec.sm.dto.Product;

import java.util.List;

public interface ProductApiService {

    List<String> fetchSortedCategories();

    List<Product> fetchProductsByCategory(String category);
}
