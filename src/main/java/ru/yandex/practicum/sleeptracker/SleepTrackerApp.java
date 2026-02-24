package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.*;
import ru.yandex.practicum.sleeptracker.exception.FileLoadingException;

import java.util.ArrayList;
import java.util.List;

public class SleepTrackerApp {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\Goggy\\Desktop\\учеба java\\7\\src\\main\\resources\\sleep_log.txt";

        ReadFile readFile = new ReadFile();
        List<InterfesForSleepTraker> functions = createFunctionList();

        try {
            List<SleepingSession> sessions = readFile.readSleepFile(filePath);
            executeAndPrintResults(functions, sessions);

        } catch (FileLoadingException e) {
            System.err.println("Ошибка при загрузке файла: " + e.getMessage());
            System.err.println("Проверьте, существует ли файл: " + filePath);
        }
    }

    private static List<InterfesForSleepTraker> createFunctionList() {
        List<InterfesForSleepTraker> functions = new ArrayList<>();
        functions.add(new CounterSleepSessions());
        functions.add(new MaxTimeSession());
        functions.add(new MinTimeSession());
        functions.add(new AverageTimeSession());
        functions.add(new CountBadSession());
        functions.add(new CounterOfNightWithOutSleep());
        functions.add(new ru.yandex.practicum.sleeptracker.TypeAnalyzer());
        return functions;
    }

    private static void executeAndPrintResults(List<InterfesForSleepTraker> functions, List<SleepingSession> sessions) {
        functions.stream()
                .map(function -> function.execute(sessions))
                .forEach(System.out::println);
    }
}