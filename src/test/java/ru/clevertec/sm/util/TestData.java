package ru.clevertec.sm.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.InputStream;
import java.util.List;

@UtilityClass
public class TestData {
    public static ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static List<String> getCategoriesList() {
        InputStream resourceAsStream = TestData.class.getResourceAsStream("/files/categories.json");
        return objectMapper.readValue(resourceAsStream, new TypeReference<>() {});
    }

}
