package ru.clevertec.sm.statemachine;

public enum States {
    IDLE,
    STARTED,
    CATEGORY_PROCESSING,
    MAKING_CSV_FILES,
    MAKING_ZIP_ARCHIVES,
    SENDING_EMAIL,
    FINISHED
}
