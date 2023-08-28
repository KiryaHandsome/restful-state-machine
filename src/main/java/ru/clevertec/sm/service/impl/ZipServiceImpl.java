package ru.clevertec.sm.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.sm.service.ZipService;
import ru.clevertec.sm.util.ServiceConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class ZipServiceImpl implements ZipService {

    /**
     * Creates zip archives in {@link ServiceConstants#OUTPUT_PATH} folder
     *
     * @return map with zip archive name as a key
     * and list of files names inside this archive
     */
    @Override
    public Map<String, List<String>> createArchives() {
        Map<String, List<String>> zipNameAndNamesOfFiles = new HashMap<>();
        File sourceDir = new File(ServiceConstants.OUTPUT_PATH);
        Optional<File[]> brandFolders = Optional.ofNullable(sourceDir.listFiles(File::isDirectory));
        for (File brandFolder : brandFolders.orElse(new File[0])) {
            createZipArchive(brandFolder)
                    .ifPresent(e -> zipNameAndNamesOfFiles.put(e.getKey(), e.getValue()));
        }

        return zipNameAndNamesOfFiles;
    }

    /**
     * Creates zip archive for csv files in brand folder
     *
     * @param brandFolder folder to create zip archive
     * @return list of files added to zip archive
     */
    private Optional<Map.Entry<String, List<String>>> createZipArchive(File brandFolder) {
        String archivePath = buildZipArchivePath(brandFolder.getName());
        File archive = new File(archivePath);
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(archive))) {
            List<String> namesOfFiles = addFilesToZip(brandFolder, zipOutputStream);
            return Optional.of(Map.entry(archive.getName(), namesOfFiles));
        } catch (IOException e) {
            log.error("Couldn't create zip archive: {}", archivePath);
            return Optional.empty();
        }
    }

    private List<String> addFilesToZip(File brandFolder, ZipOutputStream zipOutputStream) throws IOException {
        List<String> createdFileNames = new ArrayList<>();
        for (File file : brandFolder.listFiles()) {
            if (file.getName().endsWith(ServiceConstants.CSV_EXTENSION)) {
                String relativePath = brandFolder.toURI().relativize(file.toURI()).getPath();
                zipOutputStream.putNextEntry(new ZipEntry(relativePath));
                writeFileToZipStream(file, zipOutputStream)
                        .ifPresent(createdFileNames::add);
                zipOutputStream.closeEntry();
            }
        }

        return createdFileNames;
    }

    private Optional<String> writeFileToZipStream(File file, ZipOutputStream zipOutputStream) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, length);
            }
            return Optional.of(file.getName());
        } catch (IOException e) {
            log.error("Couldn't write file {} to zip", file.getName());
        }

        return Optional.empty();
    }

    private String buildZipArchivePath(String fileName) {
        return ServiceConstants.OUTPUT_PATH + File.separator +
                fileName + ServiceConstants.ZIP_EXTENSION;
    }
}
