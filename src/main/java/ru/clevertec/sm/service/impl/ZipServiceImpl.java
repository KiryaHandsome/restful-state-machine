package ru.clevertec.sm.service.impl;

import org.springframework.stereotype.Service;
import ru.clevertec.sm.service.ZipService;
import ru.clevertec.sm.util.ServiceConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipServiceImpl implements ZipService {

    @Override
    public void createArchives() {
        File sourceDir = new File(ServiceConstants.OUTPUT_PATH);
        Optional<File[]> brandFolders = Optional.ofNullable(sourceDir.listFiles(File::isDirectory));
        for (File brandFolder : brandFolders.orElse(new File[0])) {
            createZipArchive(brandFolder);
        }
    }

    private void createZipArchive(File brandFolder) {
        String archiveName = buildZipArchiveName(brandFolder.getName());
        File archive = new File(archiveName);
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(archive))) {
            addFilesToZip(brandFolder, zipOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addFilesToZip(File brandFolder, ZipOutputStream zipOutputStream) throws IOException {
        for (File file : brandFolder.listFiles()) {
            if (file.getName().endsWith(ServiceConstants.CSV_EXTENSION)) {
                String relativePath = brandFolder.toURI().relativize(file.toURI()).getPath();
                zipOutputStream.putNextEntry(new ZipEntry(relativePath));

                try (FileInputStream fileInputStream = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fileInputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, length);
                    }
                }

                zipOutputStream.closeEntry();
            }
        }
    }

    private String buildZipArchiveName(String fileName) {
        return ServiceConstants.OUTPUT_PATH + File.separator +
                fileName + ServiceConstants.ZIP_EXTENSION;
    }
}
