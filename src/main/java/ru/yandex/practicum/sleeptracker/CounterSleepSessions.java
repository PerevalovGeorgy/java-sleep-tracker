package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class CounterSleepSessions implements Function {
    @Override
    public SleepAnalysisResult<?> execute(List<SleepingSession> sessions) {
        long count = sessions == null ? 0 : sessions.stream().count();
        return new SleepAnalysisResult<>("Всего сессий сна", count);
    }
}