package ru.clevertec.sm.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.sm.exception.IsNotDirectoryException;
import ru.clevertec.sm.service.ZipService;
import ru.clevertec.sm.util.ServiceConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class ZipServiceImpl implements ZipService {

    @Override
    public Optional<Map.Entry<String, List<String>>> createArchive(File folder, String filesExtension) {
        if (!folder.isDirectory())
            throw new IsNotDirectoryException("Passed file object is not folder: " + folder.getName());

        List<File> files = retrieveFilesWithExtension(folder, filesExtension);
        String archivePath = folder.getPath() + ServiceConstants.ZIP_EXTENSION;
        File archive = new File(archivePath);

        return writeFilesToArchive(archive, folder, files);
    }

    private List<File> retrieveFilesWithExtension(File folder, String extension) {
        return Optional.ofNullable(folder.listFiles())
                .stream()
                .flatMap(Stream::of)
                .filter(f -> f.isFile() && f.getName().endsWith(extension))
                .toList();
    }

    private Optional<Map.Entry<String, List<String>>> writeFilesToArchive(File archive, File folder, List<File> files) {
        List<String> filesAddedToArchive = new ArrayList<>();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(archive))) {
            for (File file : files) {
                String relativePath = folder.toURI().relativize(file.toURI()).getPath();
                zipOutputStream.putNextEntry(new ZipEntry(relativePath));
                writeFileToZipStream(file, zipOutputStream)
                        .ifPresent(filesAddedToArchive::add);
                zipOutputStream.closeEntry();
            }
            return Optional.of(Map.entry(archive.getName(), filesAddedToArchive));
        } catch (IOException e) {
            log.error("Couldn't create zip archive: {}", archive.getName());
            return Optional.empty();
        }
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
}
