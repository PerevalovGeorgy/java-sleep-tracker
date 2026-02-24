package ru.yandex.practicum.sleeptracker.SleepAnalizerFunction;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MinTimeSession implements InterfesForSleepTraker {

    @Override
    public SleepAnalysisResult<?> execute(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Минимальная продолжительность сна", "нет данных");
        }

        long minMinutes = sessions.stream()
                .filter(s -> s != null && s.getStart() != null && s.getEnd() != null)
                .mapToLong(this::calculateDurationMinutes)
                .filter(min -> min > 0)
                .min()
                .orElse(0);

        if (minMinutes == 0) {
            return new SleepAnalysisResult<>("Минимальная продолжительность сна", "не удалось вычислить");
        }

        return new SleepAnalysisResult<>("Минимальная продолжительность сна", minMinutes + " мин");
    }

    private long calculateDurationMinutes(SleepingSession session) {
        try {
            LocalDateTime start = LocalDateTime.parse(session.getStart(), SleepingSession.FORMATTER);
            LocalDateTime end = LocalDateTime.parse(session.getEnd(), SleepingSession.FORMATTER);

            return Duration.between(start, end).toMinutes();
        } catch (DateTimeParseException e) {
            System.err.println("Ошибка парсинга даты: " + session.getStart() + " - " + session.getEnd());
            return 0L;
        } catch (Exception e) {
            return 0L;
        }
    }
}