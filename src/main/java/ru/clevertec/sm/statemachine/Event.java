package ru.clevertec.sm.statemachine;

public enum Event {
    STARTED_WITH_CATEGORY,
    FETCH_CATEGORIES,
    FETCH_PRODUCTS,
    MAKE_CSV_FILES,
    CATEGORIES_REMAINED,
    FINISH_CSV_FILES,
    FINISH_ZIP_ARCHIVES,
    SEND_EMAIL
}
