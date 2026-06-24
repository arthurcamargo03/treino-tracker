package com.treinotracker.model;

import java.time.LocalDate;

public final class WaterLog {
    private final LocalDate date;
    private int consumedMl;
    private final int goalMl;

    public WaterLog(LocalDate date, int consumedMl, int goalMl) {
        this.date = date;
        this.consumedMl = consumedMl;
        this.goalMl = goalMl;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getConsumedMl() {
        return consumedMl;
    }

    public int getGoalMl() {
        return goalMl;
    }

    public void add(int ml) {
        this.consumedMl += ml;
    }

    public double percent() {
        return (consumedMl * 100.0) / goalMl;
    }

    public boolean goalReached() {
        return consumedMl >= goalMl;
    }

    public String toCsv() {
        return date + ";" + consumedMl + ";" + goalMl;
    }

    public static WaterLog fromCsv(String csv) {
        String[] parts = csv.split(";");
        return new WaterLog(LocalDate.parse(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }
}
