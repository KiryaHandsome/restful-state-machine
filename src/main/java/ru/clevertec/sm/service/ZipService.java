package ru.clevertec.sm.service;

import ru.clevertec.sm.exception.IsNotDirectoryException;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ZipService {

    /**
     * Creates zip archive for files with needed extension inside folder.
     * Name of archive is a name of folder.
     *
     * @param folder         object for which it creates zip archive
     * @param filesExtension extension which files pack to archive
     * @return entry where key - name of archive, value - list of files added to archive
     * if all was good, empty optional otherwise
     * @throws IsNotDirectoryException if param folder is not directory
     */
    Optional<Map.Entry<String, List<String>>> createArchive(File folder, String filesExtension);
}
