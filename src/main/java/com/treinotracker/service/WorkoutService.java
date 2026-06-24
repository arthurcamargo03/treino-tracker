package com.treinotracker.service;

import com.treinotracker.model.Exercise;
import com.treinotracker.model.SetLog;
import com.treinotracker.repository.DataStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WorkoutService {
    private final DataStore dataStore;
    private final List<Exercise> exercises;
    private final List<SetLog> sets;

    public WorkoutService(DataStore dataStore) {
        this.dataStore = dataStore;
        this.exercises = new ArrayList<>(dataStore.loadExercises());
        this.sets = new ArrayList<>(dataStore.loadSets());
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public boolean exerciseExists(String name) {
        return exercises.stream().anyMatch(e -> e.getName().equalsIgnoreCase(name));
    }

    public void addExercise(String name, String group) {
        if (exerciseExists(name)) {
            throw new IllegalArgumentException("Exercício já existe: " + name);
        }
        exercises.add(new Exercise(name, group));
        dataStore.saveExercises(exercises);
    }

    public void logSet(String exerciseName, int week, double weight, int reps, int setsCount) {
        if (!exerciseExists(exerciseName)) {
            throw new IllegalArgumentException("Exercício não encontrado: " + exerciseName);
        }
        sets.add(new SetLog(exerciseName, week, weight, reps, setsCount, LocalDate.now()));
        dataStore.saveSets(sets);
    }

    public List<SetLog> getSetsFor(String exerciseName) {
        List<SetLog> result = new ArrayList<>();
        for (SetLog set : sets) {
            if (set.getExerciseName().equalsIgnoreCase(exerciseName)) {
                result.add(set);
            }
        }
        return result;
    }

    public List<WeekSummary> getProgression(String exerciseName) {
        TreeMap<Integer, SetLog> bestPerWeek = new TreeMap<>();
        for (SetLog set : getSetsFor(exerciseName)) {
            SetLog current = bestPerWeek.get(set.getWeek());
            if (current == null || set.estimated1RM() > current.estimated1RM()) {
                bestPerWeek.put(set.getWeek(), set);
            }
        }

        List<WeekSummary> summaries = new ArrayList<>();
        Double previous1RM = null;
        for (Map.Entry<Integer, SetLog> entry : bestPerWeek.entrySet()) {
            SetLog best = entry.getValue();
            double oneRm = best.estimated1RM();
            Double trend = previous1RM == null ? null : ((oneRm - previous1RM) / previous1RM) * 100.0;
            summaries.add(new WeekSummary(
                    best.getWeek(),
                    best.getWeight(),
                    best.getReps(),
                    best.getSets(),
                    best.volume(),
                    oneRm,
                    trend
            ));
            previous1RM = oneRm;
        }
        return summaries;
    }

    public boolean isProgressing(String exerciseName) {
        List<WeekSummary> progression = getProgression(exerciseName);
        if (progression.isEmpty()) {
            return false;
        }
        WeekSummary last = progression.get(progression.size() - 1);
        return last.trendPercent != null && last.trendPercent > 0;
    }

    public void seedDemoData() {
        addExercise("Supino reto", "Peito");
        addExercise("Agachamento", "Pernas");
        addExercise("Remada curvada", "Costas");

        logSet("Supino reto", 1, 60, 10, 3);
        logSet("Supino reto", 2, 62.5, 10, 3);
        logSet("Supino reto", 3, 65, 10, 3);

        logSet("Agachamento", 1, 80, 8, 3);
        logSet("Agachamento", 2, 90, 8, 3);
        logSet("Agachamento", 3, 100, 8, 3);

        logSet("Remada curvada", 1, 50, 10, 3);
        logSet("Remada curvada", 2, 50, 10, 3);
        logSet("Remada curvada", 3, 50, 10, 3);
    }
}
