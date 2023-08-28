package ru.clevertec.sm.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import ru.clevertec.sm.dto.Product;

import java.io.InputStream;
import java.util.List;

@UtilityClass
public class TestData {

    public static final String SMARTPHONES = "smartphones";
    public static ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static List<String> getCategories() {
        InputStream resourceAsStream = TestData.class.getResourceAsStream("/__files/categories.json");
        return objectMapper.readValue(resourceAsStream, new TypeReference<>() {
        });
    }

    @SneakyThrows
    public static List<Product> getProducts() {
        InputStream resourceAsStream = TestData.class.getResourceAsStream("/__files/products.json");
        return objectMapper.readValue(resourceAsStream, new TypeReference<>() {
        });
    }

    @SneakyThrows
    public static List<Product> getSmartphones() {
        InputStream resourceAsStream = TestData.class.getResourceAsStream("/__files/smartphones.json");
        return objectMapper.readValue(resourceAsStream, new TypeReference<>() {
        });
    }

    public static List<String> getEmailsFromProps() {
        return List.of("test123@gmail.com", "test222@mail.ru");
    }

}
