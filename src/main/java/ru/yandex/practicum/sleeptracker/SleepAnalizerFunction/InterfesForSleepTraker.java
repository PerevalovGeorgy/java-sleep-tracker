package ru.yandex.practicum.sleeptracker.SleepAnalizerFunction;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;

import java.util.List;

@FunctionalInterface
public interface InterfesForSleepTraker {

    SleepAnalysisResult<?> execute(List<SleepingSession> sessions);
}
