package test.java.ru.yandex.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.AverageTimeSession;
import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.SleepingSession;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AverageTimeSessionCompleteTest {

    private AverageTimeSession averageTimeSession;

    @BeforeEach
    void setUp() {
        averageTimeSession = new AverageTimeSession();
    }

    @Test
    void testAverageTimeWithNormalSessions() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD"),
                createSession("02.10.25 00:30", "02.10.25 06:30", "NORMAL"),
                createSession("03.10.25 14:00", "03.10.25 14:45", "NORMAL"),
                createSession("03.10.25 23:00", "04.10.25 06:00", "GOOD")
        );
        SleepAnalysisResult<?> result = averageTimeSession.execute(sessions);

        assertNotNull(result);
        assertEquals("Средняя продолжительность сна", result.description());
        assertEquals("326 мин", result.value());
    }

    @Test
    void testAverageTimeWithEmptySessions() {
        SleepAnalysisResult<?> result = averageTimeSession.execute(new ArrayList<>());

        assertNotNull(result);
        assertEquals("Средняя продолжительность сна", result.description());
        assertEquals("нет данных", result.value());
    }

    @Test
    void testAverageTimeWithNullSessions() {
        SleepAnalysisResult<?> result = averageTimeSession.execute(null);

        assertNotNull(result);
        assertEquals("Средняя продолжительность сна", result.description());
        assertEquals("нет данных", result.value());
    }

    @Test
    void testAverageTimeWithCrossMidnightSessions() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD"),
                createSession("02.10.25 14:00", "02.10.25 15:30", "NORMAL"),
                createSession("03.10.25 00:30", "03.10.25 06:30", "GOOD")
        );
        SleepAnalysisResult<?> result = averageTimeSession.execute(sessions);

        assertNotNull(result);
        assertEquals("310 мин", result.value());
    }

    @Test
    void testAverageTimeWithInvalidSessions() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD"));
        sessions.add(new SleepingSession(Arrays.asList(null, null, null)));
        sessions.add(new SleepingSession(Arrays.asList("", "", "")));
        sessions.add(createSession("02.10.25 14:00", "02.10.25 15:30", "NORMAL"));

        SleepAnalysisResult<?> result = averageTimeSession.execute(sessions);

        assertNotNull(result);
        assertEquals("285 мин", result.value());
    }

    @Test
    void testAverageTimeWithRounding() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:00", "02.10.25 00:40", "GOOD"),
                createSession("02.10.25 23:00", "03.10.25 00:41", "GOOD"),
                createSession("03.10.25 23:00", "04.10.25 00:42", "GOOD")
        );
        SleepAnalysisResult<?> result = averageTimeSession.execute(sessions);

        assertNotNull(result);
        assertEquals("101 мин", result.value());
    }

    private SleepingSession createSession(String start, String end, String quality) {
        List<String> data = Arrays.asList(start, end, quality);
        return new SleepingSession(data);
    }
}