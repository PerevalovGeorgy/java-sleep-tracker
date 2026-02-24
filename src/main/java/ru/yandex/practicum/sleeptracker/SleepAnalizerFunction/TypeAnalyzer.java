package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.CounterOfNightWithOutSleep;
import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.InterfesForSleepTraker;
import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.SleepingSession;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeAnalyzer implements InterfesForSleepTraker {

    @Override
    public SleepAnalysisResult<?> execute(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Хронотип", "нет данных");
        }

        Map<ChronoType, Long> counts = sessions.stream()
                .filter(s -> s != null && s.getStart() != null && s.getEnd() != null)
                .map(this::classifySession)
                .filter(type -> type != ChronoType.DAY_SLEEP)
                .collect(Collectors.groupingBy(
                        type -> type,
                        Collectors.counting()
                ));

        ChronoType chronotype = findMostFrequent(counts);
        return new SleepAnalysisResult<>("Хронотип", getRussianName(chronotype));
    }

    private ChronoType findMostFrequent(Map<ChronoType, Long> counts) {
        if (counts.isEmpty()) {
            return ChronoType.DOVE;
        }

        long maxCount = counts.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);

        List<ChronoType> mostFrequent = counts.entrySet().stream()
                .filter(entry -> entry.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .toList();

        if (mostFrequent.size() > 1) {
            return ChronoType.DOVE;
        }

        return mostFrequent.getFirst();
    }

    private ChronoType classifySession(SleepingSession session) {
        try {
            if (!CounterOfNightWithOutSleep.isNightSleep(session)) {
                return ChronoType.DAY_SLEEP;
            }

            LocalDateTime start = LocalDateTime.parse(session.getStart(), SleepingSession.FORMATTER);
            LocalDateTime end = LocalDateTime.parse(session.getEnd(), SleepingSession.FORMATTER);

            return determineType(start.toLocalTime(), end.toLocalTime(),
                    end.toLocalDate().isAfter(start.toLocalDate()));

        } catch (Exception e) {
            return ChronoType.DAY_SLEEP;
        }
    }


    private ChronoType determineType(LocalTime start, LocalTime end, boolean isNextDay) {
        if (isNextDay) {
            if (start.isAfter(SleepingSession.OWL_SLEEP)) return ChronoType.OWL;
            if (start.isBefore(SleepingSession.LARK_SLEEP)) return ChronoType.LARK;
            return ChronoType.DOVE;
        }

        if (start.isAfter(SleepingSession.OWL_SLEEP) && end.isAfter(SleepingSession.OWL_WAKEUP)) {
            return ChronoType.OWL;
        }
        if (start.isBefore(SleepingSession.LARK_SLEEP) && end.isBefore(SleepingSession.LARK_WAKEUP)) {
            return ChronoType.LARK;
        }
        return ChronoType.DOVE;
    }

    private String getRussianName(ChronoType type) {
        return switch (type) {
            case OWL -> "сова";
            case LARK -> "жаворонок";
            case DOVE -> "голубь";
            default -> "не определен";
        };
    }
}