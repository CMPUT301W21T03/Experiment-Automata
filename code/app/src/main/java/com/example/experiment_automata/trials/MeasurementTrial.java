package com.example.experiment_automata.trials;

import android.location.Location;

import java.util.UUID;

public class MeasurementTrial extends Trial {
    private float result;

    public MeasurementTrial(UUID collector, float result) {
        super(collector);
        this.result = result;
    }

    public MeasurementTrial(UUID collector, Location location, float result) {
        super(collector, location);
        this.result = result;
    }

    public float getResult() {
        return result;
    }
}
