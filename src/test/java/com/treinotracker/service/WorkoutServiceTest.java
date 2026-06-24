package com.treinotracker.service;

import com.treinotracker.repository.DataStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class WorkoutServiceTest {

    @Test
    void getProgression_calculatesTrendPercentBetweenWeeks(@TempDir Path tempDir) {
        DataStore dataStore = new DataStore(tempDir.toString());
        WorkoutService service = new WorkoutService(dataStore);

        service.addExercise("Supino reto", "Peito");
        service.logSet("Supino reto", 1, 100.0, 10, 3);
        service.logSet("Supino reto", 2, 110.0, 10, 3);

        List<WeekSummary> progression = service.getProgression("Supino reto");

        assertEquals(2, progression.size());
        assertNull(progression.get(0).trendPercent);

        double week1Rm = progression.get(0).estimated1RM;
        double week2Rm = progression.get(1).estimated1RM;
        double expectedTrend = ((week2Rm - week1Rm) / week1Rm) * 100.0;

        assertEquals(expectedTrend, progression.get(1).trendPercent, 0.0001);
    }
}
