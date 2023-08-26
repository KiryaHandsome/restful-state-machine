package ru.clevertec.sm.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.sm.dto.Product;
import ru.clevertec.sm.service.CsvService;
import ru.clevertec.sm.util.TestData;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class CsvServiceImplTest {

    private CsvService csvService;

    @BeforeEach
    void setUp() {
        csvService = new CsvServiceImpl();
    }

    @Test
    void checkWriteProductsToCSVShouldCreateFilesAndDirectories() {
        List<Product> smartphones = TestData.getSmartphones();
        Map<String, List<Product>> brandsAndProducts = smartphones.stream()
                .collect(Collectors.groupingBy(Product::getBrand));
        String category = "smartphones";
        csvService.writeProductsToCSV(smartphones, category);

        for(var entry : brandsAndProducts.entrySet()) {
            String brand = entry.getKey();
            String folderPath = CsvServiceImpl.OUTPUT_PATH + File.separator + brand;
            String filePath = folderPath + File.separator + category + CsvServiceImpl.CSV_EXTENSION;
            Assertions.assertThat(new File(folderPath)).isDirectory();
            Assertions.assertThat(new File(filePath)).isFile();
        }
    }
}