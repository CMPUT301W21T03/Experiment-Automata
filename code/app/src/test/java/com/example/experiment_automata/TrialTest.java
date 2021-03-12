package com.example.experiment_automata;

import android.location.Location;
import android.location.LocationManager;

import com.example.experiment_automata.trials.BinomialTrial;
import com.example.experiment_automata.trials.CountTrial;
import com.example.experiment_automata.trials.MeasurementTrial;
import com.example.experiment_automata.trials.NaturalCountTrial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void naturalCount() {
        assertNotNull(new NaturalCountTrial(userId, 0));
        Location loc = new Location(LocationManager.NETWORK_PROVIDER);
        int count = 420;
        NaturalCountTrial trial = new NaturalCountTrial(userId, loc, count);
        assertEquals(count, trial.getResult());
        assertThrows(IllegalArgumentException.class, () -> new NaturalCountTrial(userId, -1));
    }

    @Test
    void binomial() {
        Location loc = new Location(LocationManager.NETWORK_PROVIDER);
        assertNotNull(new BinomialTrial(userId, loc, false));
        BinomialTrial trial = new BinomialTrial(userId, true);
        assertEquals(true, trial.getResult());
    }

    @Test
    public void measurement() {
        Location loc = new Location(LocationManager.NETWORK_PROVIDER);
        assertNotNull(new MeasurementTrial(userId, loc, 0));
        float res = 6.9f;
        MeasurementTrial trial = new MeasurementTrial(userId, res);
        assertEquals(res, trial.getResult());
        res = -Float.MIN_VALUE;
        trial = new MeasurementTrial(userId, res);
        assertEquals(res, trial.getResult());
    }
}