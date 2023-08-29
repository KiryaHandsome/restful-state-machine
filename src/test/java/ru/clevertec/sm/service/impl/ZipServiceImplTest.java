package ru.clevertec.sm.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.sm.util.ServiceConstants;
import ru.clevertec.sm.util.TestData;

import java.io.File;

@SpringBootTest
class ZipServiceImplTest {

    @Autowired
    private ZipServiceImpl zipService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createArchive() {
        String folderPathname = ServiceConstants.OUTPUT_PATH + File.separator + TestData.APPLE;
        File folder = new File(folderPathname);

        zipService.createArchive(folder, ServiceConstants.CSV_EXTENSION);
    }
}