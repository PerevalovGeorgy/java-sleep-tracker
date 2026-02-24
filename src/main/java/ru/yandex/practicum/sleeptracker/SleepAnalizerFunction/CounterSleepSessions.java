package ru.yandex.practicum.sleeptracker.SleepAnalizerFunction;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;

import java.util.List;

public class CounterSleepSessions implements InterfesForSleepTraker {
    @Override
    public SleepAnalysisResult<?> execute(List<SleepingSession> sessions) {
        long count = sessions == null ? 0 : (long) sessions.size();
        return new SleepAnalysisResult<>("Всего сессий сна", count);
    }
}