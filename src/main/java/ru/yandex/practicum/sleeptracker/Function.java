package ru.yandex.practicum.sleeptracker;

import java.util.List;

@FunctionalInterface
public interface Function {

    SleepAnalysisResult<?> execute(List<SleepingSession> sessions);
}
