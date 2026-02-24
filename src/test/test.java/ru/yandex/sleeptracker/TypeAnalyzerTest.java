package ru.yandex.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.SleepingSession;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TypeAnalyzerTest {

    private ru.yandex.practicum.sleeptracker.TypeAnalyzer typeAnalyzer;

    @BeforeEach
    void setUp() {
        typeAnalyzer = new ru.yandex.practicum.sleeptracker.TypeAnalyzer();
    }

    @Test
    void testChronotype_WithMoreOwls() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:30", "02.10.25 09:30", "GOOD"),
                createSession("02.10.25 23:15", "03.10.25 09:15", "GOOD"),
                createSession("03.10.25 23:45", "04.10.25 09:45", "GOOD"),
                createSession("04.10.25 21:30", "05.10.25 06:30", "GOOD"),
                createSession("05.10.25 22:30", "06.10.25 08:00", "GOOD")
        );
        SleepAnalysisResult<?> result = typeAnalyzer.execute(sessions);

        assertEquals("сова", result.value());
    }

    @Test
    void testChronotype_WithMoreLarks() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 21:00", "02.10.25 06:00", "GOOD"),
                createSession("02.10.25 21:30", "03.10.25 06:30", "GOOD"),
                createSession("03.10.25 20:45", "04.10.25 05:45", "GOOD"),
                createSession("04.10.25 23:30", "05.10.25 09:30", "GOOD"),
                createSession("05.10.25 22:30", "06.10.25 08:00", "GOOD")
        );
        SleepAnalysisResult<?> result = typeAnalyzer.execute(sessions);

        assertEquals("жаворонок", result.value());
    }

    @Test
    void testChronotype_WithMoreDoves() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 22:30", "02.10.25 08:00", "GOOD"),
                createSession("02.10.25 23:30", "03.10.25 08:30", "GOOD"),
                createSession("03.10.25 21:30", "04.10.25 08:30", "GOOD"),
                createSession("04.10.25 23:30", "05.10.25 09:30", "GOOD"),
                createSession("05.10.25 21:00", "06.10.25 06:00", "GOOD")
        );
        SleepAnalysisResult<?> result = typeAnalyzer.execute(sessions);

        assertEquals("голубь", result.value());
    }

    @Test
    void testChronotype_WithEmptySessions() {
        SleepAnalysisResult<?> result = typeAnalyzer.execute(new ArrayList<>());

        assertEquals("нет данных", result.value());
    }

    @Test
    void testChronotype_WithNullSessions() {
        SleepAnalysisResult<?> result = typeAnalyzer.execute(null);

        assertEquals("нет данных", result.value());
    }

    @Test
    void testChronotype_WithOnlyDaySleep() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 14:10", "01.10.25 15:00", "NORMAL"),
                createSession("02.10.25 13:30", "02.10.25 14:15", "NORMAL"),
                createSession("03.10.25 15:00", "03.10.25 16:30", "NORMAL")
        );
        SleepAnalysisResult<?> result = typeAnalyzer.execute(sessions);

        assertEquals("голубь", result.value());
    }

    @Test
    void testChronotype_WithMixedNightAndDaySleep() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:30", "02.10.25 09:30", "GOOD"),
                createSession("02.10.25 14:10", "02.10.25 15:00", "NORMAL"),
                createSession("02.10.25 21:00", "03.10.25 06:00", "GOOD"),
                createSession("03.10.25 13:30", "03.10.25 14:15", "NORMAL"),
                createSession("03.10.25 22:30", "04.10.25 08:00", "GOOD")
        );
        SleepAnalysisResult<?> result = typeAnalyzer.execute(sessions);

        assertEquals("голубь", result.value());
    }

    @Test
    void testChronotype_WithTieBetweenOwlAndLark() {
        // Подготовка: ничья между совой и жаворонком (по 2 каждого)
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:30", "02.10.25 09:30", "GOOD"),  // сова
                createSession("02.10.25 23:15", "03.10.25 09:15", "GOOD"),  // сова
                createSession("03.10.25 21:00", "04.10.25 06:00", "GOOD"),  // жаворонок
                createSession("04.10.25 21:30", "05.10.25 06:30", "GOOD")   // жаворонок
        );
        SleepAnalysisResult<?> result = typeAnalyzer.execute(sessions);

        assertEquals("голубь", result.value());
    }

    @Test
    void testChronotype_WithInvalidSessions() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 23:30", "02.10.25 09:30", "GOOD"));
        sessions.add(new SleepingSession(Arrays.asList(null, null, null)));
        sessions.add(createSession("02.10.25 21:00", "03.10.25 06:00", "GOOD"));

        SleepAnalysisResult<?> result = typeAnalyzer.execute(sessions);

        assertEquals("голубь", result.value());
    }

    @Test
    void testChronotype_WithOneNightSession() {
        List<SleepingSession> sessions = List.of(
                createSession("01.10.25 23:30", "02.10.25 09:30", "GOOD")
        );
        SleepAnalysisResult<?> result = typeAnalyzer.execute(sessions);

        assertEquals("сова", result.value());
    }

    @Test
    void testChronotype_Description() {
        List<SleepingSession> sessions = List.of(
                createSession("01.10.25 23:30", "02.10.25 09:30", "GOOD")
        );
        SleepAnalysisResult<?> result = typeAnalyzer.execute(sessions);

        assertEquals("Хронотип", result.description());
    }

    private SleepingSession createSession(String start, String end, String quality) {
        List<String> data = Arrays.asList(start, end, quality);
        return new SleepingSession(data);
    }
}