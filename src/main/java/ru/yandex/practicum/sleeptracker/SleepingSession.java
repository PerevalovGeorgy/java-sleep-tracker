package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SleepingSession {
    private List<String> data;
    private SleepQuality quality;

    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    static final DateTimeFormatter FORMATTERTIME = DateTimeFormatter.ofPattern("HH:mm");
    static final DateTimeFormatter FORMATTERDATE = DateTimeFormatter.ofPattern("dd.MM.yy");
    public static final LocalTime NIGHT_END = LocalTime.of(6, 0);
    public static final LocalTime OWL_SLEEP = LocalTime.of(23, 0);
    public static final LocalTime OWL_WAKEUP = LocalTime.of(9, 0);
    public static final LocalTime LARK_SLEEP = LocalTime.of(22, 0);
    public static final LocalTime LARK_WAKEUP = LocalTime.of(7, 0);

    public SleepingSession(List<String> data) {
        this.data = data;
        if (data != null && data.size() > 2) {
            this.quality = parseQuality(data.get(2));
        } else {
            this.quality = SleepQuality.UNKNOWN;
        }
    }

    public List<String> getData() {
        return data;
    }

    public String getStart() {
        return data != null && !data.isEmpty() ? data.getFirst() : null;
    }

    public String getEnd() {
        return data != null && data.size() > 1 ? data.get(1) : null;
    }

    public String getQualityString() {
        return data != null && data.size() > 2 ? data.get(2) : null;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    private SleepQuality parseQuality(String qualityStr) {
        return Optional.ofNullable(qualityStr)
                .map(s -> s.trim().toUpperCase())
                .filter(s -> !s.isEmpty())
                .flatMap(s -> Arrays.stream(SleepQuality.values())
                        .filter(q -> q.name().equals(s))
                        .findFirst())
                .orElse(SleepQuality.UNKNOWN);
    }

    @Override
    public String toString() {
        if (data == null || data.isEmpty()) {
            return "Пустая запись";
        }

        StringBuilder sb = new StringBuilder();
        if (data.size() >= 1) sb.append("Начало: ").append(data.get(0));
        if (data.size() >= 2) sb.append(", Конец: ").append(data.get(1));
        if (data.size() >= 3) {
            sb.append(", Качество: ").append(quality);
        }
        return sb.toString();
    }
}