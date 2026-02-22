package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TypeAnalyzerTest {

    private TypeAnalyzer typeAnalyzer;

    @BeforeEach
    void setUp() {
        typeAnalyzer = new TypeAnalyzer();
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

        assertNotNull(result);
        assertEquals("Хронотип", result.getDescription());
        assertEquals("сова", result.getValue());
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

        assertEquals("жаворонок", result.getValue());
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

        assertEquals("сова", result.getValue());
    }

    @Test
    void testChronotype_WithNullSessions() {
        SleepAnalysisResult<?> result = typeAnalyzer.execute(null);

        assertEquals("нет данных", result.getValue());
    }

    @Test
    void testChronotype_WithTie() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.10.25 23:30", "02.10.25 09:30", "GOOD"),
                createSession("02.10.25 23:15", "03.10.25 09:15", "GOOD"),
                createSession("03.10.25 21:00", "04.10.25 06:00", "GOOD"),
                createSession("04.10.25 21:30", "05.10.25 06:30", "GOOD")
        );
        SleepAnalysisResult<?> result = typeAnalyzer.execute(sessions);
        
        assertEquals("сова", result.getValue());
    }
    
    private SleepingSession createSession(String start, String end, String quality) {
        List<String> data = Arrays.asList(start, end, quality);
        return new SleepingSession(data);
    }
}