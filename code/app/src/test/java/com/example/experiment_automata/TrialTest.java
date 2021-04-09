package com.example.experiment_automata;

import android.location.Location;
import android.location.LocationManager;

import com.example.experiment_automata.backend.trials.BinomialTrial;
import com.example.experiment_automata.backend.trials.CountTrial;
import com.example.experiment_automata.backend.trials.MeasurementTrial;
import com.example.experiment_automata.backend.trials.NaturalCountTrial;
import com.example.experiment_automata.backend.trials.Trial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrialTest {
    private UUID userId;

    @BeforeEach
    public void setup() {
        userId = UUID.randomUUID();
    }

    @Test
    public void count() {
        assertNotNull(new CountTrial(userId));
        Location loc = new Location(LocationManager.NETWORK_PROVIDER);
        assertNotNull(new CountTrial(userId, loc));
    }

    @Test
    public void ignore() {
        Trial<?> trial = new BinomialTrial(userId, false);
        assertFalse(trial.isIgnored());
        trial.setIgnore(true);
        assertTrue(trial.isIgnored());
    }

    @Test
    public void naturalCount() {
        new NaturalCountTrial(userId, 0);
        Location loc = new Location(LocationManager.NETWORK_PROVIDER);
        int count = 420;
        NaturalCountTrial trial = new NaturalCountTrial(userId, loc, count);
        assertEquals(count, (int) trial.getResult());
        assertThrows(IllegalArgumentException.class, () -> new NaturalCountTrial(userId, -1));
    }

    @Test
    void binomial() {
        Location loc = new Location(LocationManager.NETWORK_PROVIDER);
        BinomialTrial trial = new BinomialTrial(userId, loc, true);
        assertTrue(trial.getResult());
    }

    @Test
    public void measurement() {
        Location loc = new Location(LocationManager.NETWORK_PROVIDER);
        new MeasurementTrial(userId, loc, 0);
        float res = 6.9f;
        MeasurementTrial trial = new MeasurementTrial(userId, res);
        assertEquals(res, (float) trial.getResult());
        res = -Float.MIN_VALUE;
        trial = new MeasurementTrial(userId, res);
        assertEquals(res, (float) trial.getResult());
    }
}
