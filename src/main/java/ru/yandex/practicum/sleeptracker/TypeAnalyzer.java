package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeAnalyzer implements Function {

    @Override
    public SleepAnalysisResult<?> execute(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Хронотип", "нет данных");
        }

        Map<String, Long> counts = sessions.stream()
                .filter(s -> s != null && s.getStart() != null && s.getEnd() != null)
                .map(this::classifySession)
                .filter(type -> !type.equals("сон днем"))
                .collect(Collectors.groupingBy(
                        type -> type,
                        Collectors.counting()
                ));

        String chronotype = findMostFrequent(counts);
        return new SleepAnalysisResult<>("Хронотип", chronotype );
    }

    private String findMostFrequent(Map<String, Long> counts) {
        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("голубь");
    }

    private String classifySession(SleepingSession session) {
        try {
            LocalDateTime start = LocalDateTime.parse(session.getStart(), SleepingSession.FORMATTER);
            LocalDateTime end = LocalDateTime.parse(session.getEnd(), SleepingSession.FORMATTER);

            if (!isNightSleep(start, end)) {
                return "сон днем";
            }

            return determineType(start.toLocalTime(), end.toLocalTime(),
                    end.toLocalDate().isAfter(start.toLocalDate()));

        } catch (Exception e) {
            return "сон днем";
        }
    }

    private boolean isNightSleep(LocalDateTime start, LocalDateTime end) {
        LocalTime startTime = start.toLocalTime();
        return end.toLocalDate().isAfter(start.toLocalDate()) ||
                startTime.isBefore(SleepingSession.NIGHT_END) ||
                (startTime.isAfter(LocalTime.NOON) && end.toLocalTime().isBefore(SleepingSession.NIGHT_END));
    }

    private String determineType(LocalTime start, LocalTime end, boolean isNextDay) {
        if (isNextDay) {
            if (start.isAfter(SleepingSession.OWL_SLEEP)) return "сова";
            if (start.isBefore(SleepingSession.LARK_SLEEP)) return "жаворонок";
            return "голубь";
        }

        if (start.isAfter(SleepingSession.OWL_SLEEP) && end.isAfter(SleepingSession.OWL_WAKEUP)) {
            return "сова";
        }
        if (start.isBefore(SleepingSession.LARK_SLEEP) && end.isBefore(SleepingSession.LARK_WAKEUP)) {
            return "жаворонок";
        }
        return "голубь";
    }
}