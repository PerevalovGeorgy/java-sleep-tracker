package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.SleepAnalizerFunction.SleepingSession;
import ru.yandex.practicum.sleeptracker.exception.FileLoadingException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReadFile {

    public List<SleepingSession> readFile(String filePath) throws FileLoadingException {
        return readSleepFile(filePath);
    }

    public List<SleepingSession> readSleepFile(String filePath) throws FileLoadingException {
        try {
            return loadSessionFromFile(filePath);
        } catch (IOException e) {
            throw new FileLoadingException("Ошибка при чтении файла: " + filePath, e);
        }
    }

    private List<SleepingSession> loadSessionFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new FileLoadingException("Файл не найден: " + filePath);
        }

        try (var lines = Files.lines(path, StandardCharsets.UTF_8)) {
            return lines
                    .filter(line -> !line.isBlank())
                    .map(this::parseLine)
                    .map(SleepingSession::new)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    private ArrayList<String> parseLine(String line) {
        return new ArrayList<>(Arrays.asList(line.split(";")));
    }
}