package ru.yandex.practicum.sleeptracker.SleepAnalizerFunction;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class AverageTimeSession implements InterfesForSleepTraker {

    @Override
    public SleepAnalysisResult<?> execute(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Средняя продолжительность сна", "нет данных");
        }

        double avgMinutes = sessions.stream()
                .filter(s -> s != null && s.getStart() != null && s.getEnd() != null)
                .mapToLong(this::calculateDurationMinutes)
                .filter(min -> min > 0)
                .average()
                .orElse(0);

        long rounded = Math.round(avgMinutes);
        return new SleepAnalysisResult<>("Средняя продолжительность сна", rounded + " мин");
    }

    private long calculateDurationMinutes(SleepingSession session) {
        try {
            LocalDateTime start = LocalDateTime.parse(session.getStart(), SleepingSession.FORMATTER);
            LocalDateTime end = LocalDateTime.parse(session.getEnd(), SleepingSession.FORMATTER);

            return Duration.between(start, end).toMinutes();
        } catch (Exception e) {
            return 0L;
        }
    }
}