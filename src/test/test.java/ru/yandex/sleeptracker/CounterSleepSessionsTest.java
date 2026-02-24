package ru.yandex.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.CounterSleepSessions;
import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.SleepingSession;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CounterSleepSessionsTest {

    private CounterSleepSessions counter;

    @BeforeEach
    void setUp() {
        counter = new CounterSleepSessions();
    }

    @Test
    void testCountSessions_WithMultipleSessions() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD"),
                createSession("02.10.25 23:50", "03.10.25 06:40", "NORMAL"),
                createSession("03.10.25 14:10", "03.10.25 15:00", "NORMAL"),
                createSession("03.10.25 23:40", "04.10.25 08:00", "BAD"),
                createSession("05.10.25 00:10", "05.10.25 06:20", "GOOD")
        );
        SleepAnalysisResult<?> result = counter.execute(sessions);

        assertNotNull(result);
        assertEquals("Всего сессий сна", result.description());
        assertEquals(5L, result.value());
    }

    @Test
    void testCountSessions_WithEmptyList() {
        List<SleepingSession> emptySessions = new ArrayList<>();
        SleepAnalysisResult<?> result = counter.execute(emptySessions);

        assertNotNull(result);
        assertEquals(0L, result.value());
    }

    @Test
    void testCountSessions_WithNullList() {
        SleepAnalysisResult<?> result = counter.execute(null);

        assertNotNull(result);
        assertEquals(0L, result.value());
    }

    private SleepingSession createSession(String start, String end, String quality) {
        List<String> data = Arrays.asList(start, end, quality);
        return new SleepingSession(data);
    }
}