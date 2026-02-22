package ru.yandex.practicum.sleeptracker.exception;

public class DateTimeParseException extends RuntimeException {
    public DateTimeParseException(String message) {
        super(message);
    }
}
