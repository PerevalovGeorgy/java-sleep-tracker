package java.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.CountBadSession;
import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.SleepingSession;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CountBadSessionCompleteTest {

    private CountBadSession countBadSession;

    @BeforeEach
    void setUp() {
        countBadSession = new CountBadSession();
    }

    @Test
    void testCountBadSessions_WithMixedQualities() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD"),
                createSession("02.10.25 23:50", "03.10.25 06:40", "NORMAL"),
                createSession("03.10.25 14:10", "03.10.25 15:00", "NORMAL"),
                createSession("03.10.25 23:40", "04.10.25 08:00", "BAD"),
                createSession("05.10.25 00:10", "05.10.25 06:20", "GOOD"),
                createSession("06.10.25 22:30", "07.10.25 05:50", "BAD")
        );
        SleepAnalysisResult<?> result = countBadSession.execute(sessions);

        assertEquals(2L, result.value());
    }

    @Test
    void testCountBadSessions_WithNoBadSessions() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD"),
                createSession("02.10.25 23:50", "03.10.25 06:40", "NORMAL"),
                createSession("03.10.25 14:10", "03.10.25 15:00", "NORMAL")
        );
        SleepAnalysisResult<?> result = countBadSession.execute(sessions);

        assertEquals(0L, result.value());
    }

    @Test
    void testCountBadSessions_WithOnlyBadSessions() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:40", "02.10.25 08:00", "BAD"),
                createSession("02.10.25 22:30", "03.10.25 05:50", "BAD"),
                createSession("03.10.25 23:45", "04.10.25 06:30", "BAD")
        );
        SleepAnalysisResult<?> result = countBadSession.execute(sessions);

        assertEquals(3L, result.value());
    }

    @Test
    void testCountBadSessions_WithEmptyList() {
        SleepAnalysisResult<?> result = countBadSession.execute(new ArrayList<>());

        assertEquals(0L, result.value());
    }

    @Test
    void testCountBadSessions_WithNullList() {
        SleepAnalysisResult<?> result = countBadSession.execute(null);

        assertEquals(0L, result.value());
    }

    @Test
    void testCountBadSessions_WithNullSessions() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(null);
        sessions.add(createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD"));
        sessions.add(null);
        sessions.add(createSession("02.10.25 23:40", "03.10.25 08:00", "BAD"));

        SleepAnalysisResult<?> result = countBadSession.execute(sessions);

        assertEquals(1L, result.value());
    }

    @Test
    void testCountBadSessions_WithUnknownQuality() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD"),
                createSession("02.10.25 23:40", "03.10.25 08:00", "BAD"),
                new SleepingSession(Arrays.asList("03.10.25 14:10", "03.10.25 15:00", "UNKNOWN"))
        );
        SleepAnalysisResult<?> result = countBadSession.execute(sessions);

        assertEquals(1L, result.value());
    }

    @Test
    void testCountBadSessions_WithDifferentQualityStrings() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD"),
                createSession("02.10.25 23:40", "03.10.25 08:00", "bad"),
                createSession("03.10.25 22:30", "04.10.25 06:30", "Bad"),
                createSession("04.10.25 23:45", "05.10.25 07:00", " BAD ")
        );
        SleepAnalysisResult<?> result = countBadSession.execute(sessions);

        assertEquals(3L, result.value());
    }

    @Test
    void testCountBadSessions_WithAllNullData() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(Arrays.asList(null, null, null)),
                new SleepingSession(Arrays.asList("", "", ""))
        );
        SleepAnalysisResult<?> result = countBadSession.execute(sessions);

        assertEquals(0L, result.value());
    }

    private SleepingSession createSession(String start, String end, String quality) {
        List<String> data = Arrays.asList(start, end, quality);
        return new SleepingSession(data);
    }
}