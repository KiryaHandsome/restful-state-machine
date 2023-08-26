package ru.clevertec.sm.statemachine;

public enum Event {
    STARTED_WITH_CATEGORY,
    FETCH_CATEGORIES,
    FETCH_PRODUCTS,
    MAKE_CSV_FILES,
    CATEGORIES_REMAINED,
    MAKE_ZIP_ARCHIVES,
    FINISH_ZIP_ARCHIVES,
    SEND_EMAIL
}
