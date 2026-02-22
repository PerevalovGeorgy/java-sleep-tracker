package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class CountBadSession implements Function {
    @Override
    public SleepAnalysisResult<?> execute(List<SleepingSession> sessions) {
        long count = sessions == null ? 0 : sessions.stream()
                .filter(s -> s != null && s.getQuality() == SleepQuality.BAD)
                .count();

        return new SleepAnalysisResult<>("Количество сессий с плохим сном", count);
    }
}