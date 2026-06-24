package com.treinotracker.model;

public final class Exercise {
    private final String name;
    private final String muscleGroup;

    public Exercise(String name, String muscleGroup) {
        this.name = name;
        this.muscleGroup = muscleGroup;
    }

    public String getName() {
        return name;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public String toCsv() {
        return name + ";" + muscleGroup;
    }

    public static Exercise fromCsv(String csv) {
        String[] parts = csv.split(";");
        return new Exercise(parts[0], parts[1]);
    }
}
