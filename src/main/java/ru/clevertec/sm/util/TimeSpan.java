package ru.clevertec.sm.util;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeSpan {
    LocalDateTime start;
    LocalDateTime finish;

    private TimeSpan(LocalDateTime start, LocalDateTime finish) {
        this.start = start;
        this.finish = finish;
    }

    public static TimeSpan of(LocalDateTime start, LocalDateTime finish) {
        return new TimeSpan(start, finish);
    }
}
