package ru.clevertec.sm.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.sm.service.CsvService;
import ru.clevertec.sm.util.ServiceConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
class CsvServiceImplTest {

    @Autowired
    private CsvService csvService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void checkWriteDataToCsv() {
        List<String[]> data = List.of(
                new String[]{"col1", "col2", "col3"},
                new String[]{"val1", "val2", "val3"},
                new String[]{"val4", "val5", "val6"}
        );
        String directoryPath = "test/folder/path";
        String fileName = "file";

        csvService.writeDataToCsv(directoryPath, fileName, data);

        Assertions.assertThat(new File(directoryPath)).isDirectory();
        Assertions.assertThat(new File(directoryPath + File.separator + fileName + ServiceConstants.CSV_EXTENSION)).isFile();

        deleteFolder("test/");
    }

    private void deleteFolder(String directoryPath) {
        Path directory = Paths.get(directoryPath);
        try {
            if (Files.exists(directory)) {
                if (Files.isDirectory(directory)) {
                    try (Stream<Path> walk = Files.walk(directory)) {
                        walk.sorted(Comparator.reverseOrder())
                                .forEach(path -> {
                                    try {
                                        Files.deleteIfExists(path);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                    }
                } else {
                    Files.deleteIfExists(directory);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}