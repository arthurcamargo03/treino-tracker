package com.treinotracker.service;

import com.treinotracker.model.WaterLog;
import com.treinotracker.repository.DataStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WaterServiceTest {

    @Test
    void drinkBottle_addsBottleVolumeToToday(@TempDir Path tempDir) {
        DataStore dataStore = new DataStore(tempDir.toString());
        WaterService service = new WaterService(dataStore);

        int bottleSize = service.getBottleSizeMl();
        service.drinkBottle();

        WaterLog today = service.today();
        assertEquals(bottleSize, today.getConsumedMl());
    }
}
