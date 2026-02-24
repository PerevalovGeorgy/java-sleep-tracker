package ru.yandex.practicum.sleeptracker.SleepAnalizerFunction;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepQuality;

import java.util.List;

public class CountBadSession implements InterfesForSleepTraker {
    @Override
    public SleepAnalysisResult<?> execute(List<SleepingSession> sessions) {
        long count = sessions == null ? 0 : sessions.stream()
                .filter(s -> s != null && s.getQuality() == SleepQuality.BAD)
                .count();

        return new SleepAnalysisResult<>("Количество сессий с плохим сном", count);
    }
}