package ru.clevertec.sm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IsNotDirectoryException.class)
    public void handleIsNotDirectoryException(IsNotDirectoryException ex) {
        log.error("Catch {}", "IsNotDirectoryException");
        log.error(ex.getMessage());
    }
}
