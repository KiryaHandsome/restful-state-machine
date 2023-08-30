package ru.clevertec.sm.service.impl;

import com.opencsv.CSVWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.sm.service.CsvService;
import ru.clevertec.sm.util.ServiceConstants;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
public class CsvServiceImpl implements CsvService {

    @Override
    @SneakyThrows
    public String writeDataToCsv(String directoryPath, String fileName, List<String[]> data) {
        Path filePath = Paths.get(directoryPath, fileName + ServiceConstants.CSV_EXTENSION);
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath.toString()))) {
            for (String[] row : data) {
                writer.writeNext(row);
            }
        }

        return filePath.getFileName().toString();
    }
}
