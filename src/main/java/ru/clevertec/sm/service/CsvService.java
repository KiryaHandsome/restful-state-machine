package ru.clevertec.sm.service;

import java.util.List;

public interface CsvService {

    /**
     * Writes data to csv file.
     *
     * @param directoryPath location of directory
     * @param fileName      fileName without extension
     * @param data          list of rows to write
     * @throws RuntimeException if writing went wrong
     */
    String writeDataToCsv(String directoryPath, String fileName, List<String[]> data);
}
