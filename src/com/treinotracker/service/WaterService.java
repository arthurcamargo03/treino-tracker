package com.treinotracker.service;

import com.treinotracker.model.WaterLog;
import com.treinotracker.repository.DataStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WaterService {
    private final DataStore dataStore;
    private final List<WaterLog> waterLogs;
    private int dailyGoalMl = 3000;
    private int bottleSizeMl = 500;

    public WaterService(DataStore dataStore) {
        this.dataStore = dataStore;
        this.waterLogs = new ArrayList<>(dataStore.loadWater());
    }

    public int getDailyGoalMl() {
        return dailyGoalMl;
    }

    public void setDailyGoalMl(int dailyGoalMl) {
        this.dailyGoalMl = dailyGoalMl;
    }

    public int getBottleSizeMl() {
        return bottleSizeMl;
    }

    public void setBottleSizeMl(int bottleSizeMl) {
        this.bottleSizeMl = bottleSizeMl;
    }

    public WaterLog today() {
        LocalDate now = LocalDate.now();
        for (WaterLog log : waterLogs) {
            if (log.getDate().equals(now)) {
                return log;
            }
        }
        WaterLog newLog = new WaterLog(now, 0, dailyGoalMl);
        waterLogs.add(newLog);
        dataStore.saveWater(waterLogs);
        return newLog;
    }

    public void drinkBottle() {
        drink(bottleSizeMl);
    }

    public void drink(int ml) {
        WaterLog log = today();
        log.add(ml);
        dataStore.saveWater(waterLogs);
    }

    public List<WaterLog> history() {
        return waterLogs;
    }
}
