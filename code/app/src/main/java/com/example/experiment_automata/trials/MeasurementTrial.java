package com.example.experiment_automata.trials;

import android.location.Location;

import java.util.UUID;

public class MeasurementTrial extends Trial {
    public MeasurementTrial(UUID collector) {
        super(collector);
    }

    public MeasurementTrial(UUID collector, Location location) {
        super(collector, location);
    }
}
