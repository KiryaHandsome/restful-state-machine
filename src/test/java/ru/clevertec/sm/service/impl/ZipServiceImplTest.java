package ru.clevertec.sm.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.sm.util.ServiceConstants;
import ru.clevertec.sm.util.TestData;

import java.io.File;

class ZipServiceImplTest {


    private ZipServiceImpl zipService;

    @BeforeEach
    void setUp() {
        zipService = new ZipServiceImpl();
    }

    @Test
    void createArchive() {
        String folderPathname = ServiceConstants.OUTPUT_PATH + File.separator + TestData.APPLE;
        File folder = new File(folderPathname);

        zipService.createArchive(folder, ServiceConstants.CSV_EXTENSION);
    }

    @Test
    void createArchives() {
    }
}