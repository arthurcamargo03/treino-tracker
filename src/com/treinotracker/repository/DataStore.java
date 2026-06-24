package com.treinotracker.repository;

import com.treinotracker.model.Exercise;
import com.treinotracker.model.SetLog;
import com.treinotracker.model.WaterLog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static final String EXERCISES_FILE = "exercises.csv";
    private static final String SETS_FILE = "sets.csv";
    private static final String WATER_FILE = "water.csv";

    private final Path baseDir;

    public DataStore(String baseDir) {
        this.baseDir = Path.of(baseDir);
        try {
            Files.createDirectories(this.baseDir);
        } catch (IOException e) {
            System.err.println("Aviso: não foi possível criar a pasta de dados '" + this.baseDir + "': " + e.getMessage());
        }
    }

    public List<Exercise> loadExercises() {
        List<Exercise> result = new ArrayList<>();
        for (String line : readLines(EXERCISES_FILE)) {
            if (!line.isBlank()) {
                result.add(Exercise.fromCsv(line));
            }
        }
        return result;
    }

    public void saveExercises(List<Exercise> exercises) {
        List<String> lines = new ArrayList<>();
        for (Exercise exercise : exercises) {
            lines.add(exercise.toCsv());
        }
        writeLines(EXERCISES_FILE, lines);
    }

    public List<SetLog> loadSets() {
        List<SetLog> result = new ArrayList<>();
        for (String line : readLines(SETS_FILE)) {
            if (!line.isBlank()) {
                result.add(SetLog.fromCsv(line));
            }
        }
        return result;
    }

    public void saveSets(List<SetLog> sets) {
        List<String> lines = new ArrayList<>();
        for (SetLog set : sets) {
            lines.add(set.toCsv());
        }
        writeLines(SETS_FILE, lines);
    }

    public List<WaterLog> loadWater() {
        List<WaterLog> result = new ArrayList<>();
        for (String line : readLines(WATER_FILE)) {
            if (!line.isBlank()) {
                result.add(WaterLog.fromCsv(line));
            }
        }
        return result;
    }

    public void saveWater(List<WaterLog> waterLogs) {
        List<String> lines = new ArrayList<>();
        for (WaterLog waterLog : waterLogs) {
            lines.add(waterLog.toCsv());
        }
        writeLines(WATER_FILE, lines);
    }

    private List<String> readLines(String fileName) {
        Path file = baseDir.resolve(fileName);
        if (!Files.exists(file)) {
            return new ArrayList<>();
        }
        try {
            return Files.readAllLines(file);
        } catch (IOException e) {
            System.err.println("Aviso: erro ao ler '" + file + "': " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void writeLines(String fileName, List<String> lines) {
        Path file = baseDir.resolve(fileName);
        try {
            Files.write(file, lines);
        } catch (IOException e) {
            System.err.println("Aviso: erro ao escrever '" + file + "': " + e.getMessage());
        }
    }
}
