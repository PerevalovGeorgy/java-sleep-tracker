package ru.yandex.practicum.sleeptracker;

public record SleepAnalysisResult<T>(String description, T value) {

    @Override
    public String toString() {
        return description + ": " + value;
    }
}