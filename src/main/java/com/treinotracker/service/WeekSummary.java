package com.treinotracker.service;

public class WeekSummary {
    public int week;
    public double weight;
    public int reps;
    public int sets;
    public double volume;
    public double estimated1RM;
    public Double trendPercent;

    public WeekSummary(int week, double weight, int reps, int sets, double volume, double estimated1RM, Double trendPercent) {
        this.week = week;
        this.weight = weight;
        this.reps = reps;
        this.sets = sets;
        this.volume = volume;
        this.estimated1RM = estimated1RM;
        this.trendPercent = trendPercent;
    }
}
