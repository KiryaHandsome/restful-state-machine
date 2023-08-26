package ru.clevertec.sm.service;

import ru.clevertec.sm.dto.Product;

import java.util.List;

public interface CsvService {

    void writeProductsToCSV(List<Product> productsToWrite, String category);
}
