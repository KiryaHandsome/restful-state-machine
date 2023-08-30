package ru.clevertec.sm.util;

public interface ServiceConstants {

    // keys to state machine variables map
    String CURRENT_CATEGORY_ITERATOR = "currentCategory";
    String SEND_EMAIL = "sendEmail";
    String GENERATED_ARCHIVES_AND_FILES = "generatedArchivesAndFiles";
    String CREATION_REPORT = "creationReport";

    // default value in state machine
    boolean SEND_EMAIL_DEFAULT = true;

    // path to output folder for zip archives and csv files
    String OUTPUT_PATH = "resource/result";

    // extensions
    String CSV_EXTENSION = ".csv";
    String ZIP_EXTENSION = ".zip";
}
