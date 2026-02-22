package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CounterOfNightWithOutSleep implements Function {

    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult<?> execute(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Бессонные ночи", "нет данных");
        }

        long nightsWithSleep = sessions.stream()
                .filter(s -> s != null && s.getStart() != null && s.getEnd() != null)
                .filter(this::isNightSleep)
                .count();

        LocalDate firstDate = parseDate(sessions.getFirst().getStart());
        LocalDate lastDate = parseDate(sessions.getLast().getEnd());

        int totalNights = Period.between(firstDate, lastDate).getDays() + 1;
        long sleeplessNights = totalNights - nightsWithSleep;

        return new SleepAnalysisResult<>("Бессонные ночи",
                sleeplessNights);
    }

    private LocalDate parseDate(String dateTimeStr) {
        return LocalDate.parse(dateTimeStr.substring(0, 8),
                DateTimeFormatter.ofPattern("dd.MM.yy"));
    }

    private boolean isNightSleep(SleepingSession session) {
        try {
            LocalDateTime start = LocalDateTime.parse(session.getStart(), SleepingSession.FORMATTER);
            LocalDateTime end = LocalDateTime.parse(session.getEnd(), SleepingSession.FORMATTER);

            return end.toLocalDate().isAfter(start.toLocalDate()) ||
                    start.toLocalTime().isBefore(NIGHT_END);

        } catch (Exception e) {
            return false;
        }
    }
}