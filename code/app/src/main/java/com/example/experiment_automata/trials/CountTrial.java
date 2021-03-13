package com.example.experiment_automata.trials;

import android.location.Location;

import java.util.UUID;

public class CountTrial extends Trial {
    public CountTrial(UUID collector) {
        super(collector);
    }

    public CountTrial(UUID collector, Location location) {
        super(collector, location);
    }
}
