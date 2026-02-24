package test.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.MinTimeSession;
import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.SleepingSession;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MinTimeSessionTest {

    private MinTimeSession minTimeSession;

    @BeforeEach
    void setUp() {
        minTimeSession = new MinTimeSession();
    }

    @Test
    void testMinTime_WithNormalSessions() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD"),
                createSession("02.10.25 23:50", "03.10.25 06:40", "NORMAL"),
                createSession("03.10.25 14:10", "03.10.25 15:00", "NORMAL"),
                createSession("03.10.25 23:40", "04.10.25 08:00", "BAD"),
                createSession("05.10.25 00:10", "05.10.25 06:20", "GOOD")
        );
        SleepAnalysisResult<?> result = minTimeSession.execute(sessions);

        assertNotNull(result);
        assertEquals("Минимальная продолжительность сна", result.description());
        assertEquals("50 мин", result.value());
    }

    @Test
    void testMinTime_WithEmptySessions() {
        List<SleepingSession> emptySessions = new ArrayList<>();
        SleepAnalysisResult<?> result = minTimeSession.execute(emptySessions);

        assertNotNull(result);
        assertEquals("Минимальная продолжительность сна", result.description());
        assertEquals("нет данных", result.value());
    }

    @Test
    void testMinTime_WithNullSessions() {
        SleepAnalysisResult<?> result = minTimeSession.execute(null);

        assertNotNull(result);
        assertEquals("нет данных", result.value());
    }

    private SleepingSession createSession(String start, String end, String quality) {
        List<String> data = Arrays.asList(start, end, quality);
        return new SleepingSession(data);
    }
}