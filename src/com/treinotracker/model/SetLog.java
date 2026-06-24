package com.treinotracker.model;

import java.time.LocalDate;

public final class SetLog {
    private final String exerciseName;
    private final int week;
    private final double weight;
    private final int reps;
    private final int sets;
    private final LocalDate date;

    public SetLog(String exerciseName, int week, double weight, int reps, int sets, LocalDate date) {
        this.exerciseName = exerciseName;
        this.week = week;
        this.weight = weight;
        this.reps = reps;
        this.sets = sets;
        this.date = date;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public int getWeek() {
        return week;
    }

    public double getWeight() {
        return weight;
    }

    public int getReps() {
        return reps;
    }

    public int getSets() {
        return sets;
    }

    public LocalDate getDate() {
        return date;
    }

    public double volume() {
        return sets * reps * weight;
    }

    public double estimated1RM() {
        return weight * (1 + reps / 30.0);
    }

    public String toCsv() {
        return exerciseName + ";" + week + ";" + weight + ";" + reps + ";" + sets + ";" + date;
    }

    public static SetLog fromCsv(String csv) {
        String[] parts = csv.split(";");
        return new SetLog(
                parts[0],
                Integer.parseInt(parts[1]),
                Double.parseDouble(parts[2]),
                Integer.parseInt(parts[3]),
                Integer.parseInt(parts[4]),
                LocalDate.parse(parts[5])
        );
    }
}
