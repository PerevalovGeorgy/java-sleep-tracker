package java.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.CounterOfNightWithOutSleep;
import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.SleepingSession;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CounterOfNightWithOutSleepTest {

    private CounterOfNightWithOutSleep counter;

    @BeforeEach
    void setUp() {
        counter = new CounterOfNightWithOutSleep();
    }

    @Test
    void testSleeplessNights_WithMixedSleepTypes() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD"),
                createSession("02.10.25 14:10", "02.10.25 15:00", "NORMAL"),
                createSession("02.10.25 23:40", "03.10.25 08:00", "BAD"),
                createSession("03.10.25 14:10", "03.10.25 15:00", "NORMAL"),
                createSession("05.10.25 00:10", "05.10.25 06:20", "GOOD")
        );
        SleepAnalysisResult<?> result = counter.execute(sessions);

        assertEquals(2L, result.value());
    }

    @Test
    void testSleeplessNights_AllNightSleep() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD"),
                createSession("02.10.25 23:30", "03.10.25 06:30", "GOOD"),
                createSession("03.10.25 22:45", "04.10.25 07:15", "GOOD"),
                createSession("04.10.25 23:15", "05.10.25 08:00", "GOOD")
        );
        SleepAnalysisResult<?> result = counter.execute(sessions);

        assertEquals(1L, result.value());
    }

    @Test
    void testSleeplessNights_OnlyDaySleep() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 14:10", "01.10.25 15:00", "NORMAL"),
                createSession("02.10.25 13:30", "02.10.25 14:15", "NORMAL"),
                createSession("03.10.25 14:00", "03.10.25 15:30", "NORMAL")
        );
        SleepAnalysisResult<?> result = counter.execute(sessions);

        assertEquals(3L, result.value());
    }

    @Test
    void testSleeplessNights_WithNullAndEmpty() {
        assertEquals("нет данных", counter.execute(null).value());
        assertEquals("нет данных", counter.execute(new ArrayList<>()).value());
    }

    @Test
    void testSleeplessNights_WithInvalidData() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD"));
        sessions.add(new SleepingSession(Arrays.asList(null, null, null)));
        sessions.add(createSession("02.10.25 23:30", "03.10.25 06:30", "GOOD"));

        SleepAnalysisResult<?> result = counter.execute(sessions);

        assertEquals(1L, result.value());
    }

    @Test
    void testSleeplessNights_WithOneSession() {
        List<SleepingSession> sessions = List.of(
                createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD")
        );
        SleepAnalysisResult<?> result = counter.execute(sessions);

        assertEquals(1L, result.value());
    }

    @Test
    void testSleeplessNights_WithSleepBefore6AM() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 02:30", "01.10.25 07:30", "GOOD"),
                createSession("02.10.25 04:15", "02.10.25 08:00", "GOOD"),
                createSession("03.10.25 23:00", "04.10.25 07:00", "GOOD")
        );
        SleepAnalysisResult<?> result = counter.execute(sessions);

        assertEquals(1L, result.value());
    }

    @Test
    void testSleeplessNights_WithGapInDates() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:00", "02.10.25 07:00", "GOOD"),
                createSession("05.10.25 23:00", "06.10.25 07:00", "GOOD")
        );
        SleepAnalysisResult<?> result = counter.execute(sessions);

        assertEquals(4L, result.value());
    }

    @Test
    void testSleeplessNights_WithMonthTransition() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("30.09.25 23:00", "01.10.25 07:00", "GOOD"),
                createSession("01.10.25 23:30", "02.10.25 07:30", "GOOD"),
                createSession("02.10.25 14:10", "02.10.25 15:00", "NORMAL")
        );
        SleepAnalysisResult<?> result = counter.execute(sessions);
        assertNotNull(result);

        assertEquals(1L, result.value());
    }

    private SleepingSession createSession(String start, String end, String quality) {
        List<String> data = Arrays.asList(start, end, quality);
        return new SleepingSession(data);
    }

}