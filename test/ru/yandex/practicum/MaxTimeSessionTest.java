package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaxTimeSessionTest {

    private MaxTimeSession maxTimeSession;

    @BeforeEach
    void setUp() {
        maxTimeSession = new MaxTimeSession();
    }

    @Test
    void testMaxTime_WithNormalSessions() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD"),
                createSession("02.10.25 23:50", "03.10.25 06:40", "NORMAL"),
                createSession("03.10.25 14:10", "03.10.25 15:00", "NORMAL"),
                createSession("03.10.25 23:40", "04.10.25 08:00", "BAD"),
                createSession("05.10.25 00:10", "05.10.25 06:20", "GOOD")
        );
        SleepAnalysisResult<?> result = maxTimeSession.execute(sessions);

        assertNotNull(result);
        assertEquals("Максимальная продолжительность сна", result.getDescription());
        assertEquals("500 мин", result.getValue());
    }

    @Test
    void testMaxTime_WithEmptySessions() {
        List<SleepingSession> emptySessions = new ArrayList<>();
        SleepAnalysisResult<?> result = maxTimeSession.execute(emptySessions);

        assertNotNull(result);
        assertEquals("Максимальная продолжительность сна", result.getDescription());
        assertEquals("нет данных", result.getValue());
    }

    @Test
    void testMaxTime_WithNullSessions() {
        SleepAnalysisResult<?> result = maxTimeSession.execute(null);

        assertNotNull(result);
        assertEquals("нет данных", result.getValue());
    }

    private SleepingSession createSession(String start, String end, String quality) {
        List<String> data = Arrays.asList(start, end, quality);
        return new SleepingSession(data);
    }
}