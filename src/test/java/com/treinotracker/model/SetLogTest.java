package com.treinotracker.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SetLogTest {

    @Test
    void estimated1RM_appliesEpleyFormula() {
        SetLog setLog = new SetLog("Supino reto", 1, 100.0, 10, 3, LocalDate.now());

        double expected = 100.0 * (1 + 10 / 30.0); // 133.333...

        assertEquals(expected, setLog.estimated1RM(), 0.0001);
    }
}
