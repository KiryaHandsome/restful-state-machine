package ru.clevertec.sm.service.impl;

import com.opencsv.CSVWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.sm.dto.Product;
import ru.clevertec.sm.service.CsvService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CsvServiceImpl implements CsvService {

    public static final String CSV_EXTENSION = ".csv";
    public static final String OUTPUT_PATH = "resource/result";
    private static final String[] HEADER_ROW = {
            "ID",
            "Title",
            "Description",
            "Price",
            "DiscountPercentage",
            "Rating",
            "Stock",
            "Brand",
            "Category"
    };

    /**
     * Writes products to ${category}.csv files by brand
     * Example:
     * {@code
     * category = "smartphones" -->
     * file: resources/{brandOfProduct}/smartphones.csv
     * }
     *
     * @param productsToWrite product to write to file
     * @param category        category of products
     */
    @Override
    public void writeProductsToCSV(List<Product> productsToWrite, String category) {
        Map<String, List<Product>> brandAndProducts = productsToWrite
                .stream()
                .collect(Collectors.groupingBy(Product::getBrand));
        for (var entry : brandAndProducts.entrySet()) {
            String folderPath = buildFolderPath(entry.getKey());
            String filePath = buildFilePath(folderPath, category);
            createFolderIfItDoesntExist(folderPath);
            createFileIfItDoesntExist(filePath);
            try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
                writer.writeNext(HEADER_ROW);
                for (Product product : entry.getValue()) {
                    String[] data = mapProductToCsvData(product);
                    writer.writeNext(data);
                }
                log.info("CSV file \"{}\" created successfully", filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String buildFilePath(String folderPath, String category) {
        return folderPath + File.separator + category + CSV_EXTENSION;
    }

    private String buildFolderPath(String brand) {
        return OUTPUT_PATH + File.separator + brand;
    }

    @SneakyThrows
    private void createFileIfItDoesntExist(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new RuntimeException("File not created: " + filePath);
            }
        }
    }

    private void createFolderIfItDoesntExist(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new RuntimeException("Folder not created");
            }
        }
    }

    private String[] mapProductToCsvData(Product product) {
        return new String[]{
                String.valueOf(product.getId()),
                product.getTitle(),
                product.getDescription(),
                String.valueOf(product.getPrice()),
                String.valueOf(product.getDiscountPercentage()),
                String.valueOf(product.getRating()),
                String.valueOf(product.getStock()),
                product.getBrand(),
                product.getCategory()
        };
    }

}
