package ru.yandex.practicum.sleeptracker.SleepAnalizerFunction;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class MaxTimeSession implements InterfesForSleepTraker {

    @Override
    public SleepAnalysisResult<?> execute(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Максимальная продолжительность сна", "нет данных");
        }

        long maxMinutes = sessions.stream()
                .filter(s -> s != null && s.getStart() != null && s.getEnd() != null)
                .mapToLong(s -> {
                    try {
                        LocalDateTime start = LocalDateTime.parse(s.getStart(), SleepingSession.FORMATTER);
                        LocalDateTime end = LocalDateTime.parse(s.getEnd(), SleepingSession.FORMATTER);

                        return Duration.between(start, end).toMinutes();
                    } catch (Exception e) {
                        return 0L;
                    }
                })
                .max()
                .orElse(0);

        if (maxMinutes == 0) {
            return new SleepAnalysisResult<>("Максимальная продолжительность сна", "не удалось вычислить");
        }

        return new SleepAnalysisResult<>("Максимальная продолжительность сна", maxMinutes + " мин");
    }
}