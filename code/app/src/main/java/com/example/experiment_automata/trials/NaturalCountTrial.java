package com.example.experiment_automata.trials;

import android.location.Location;

import java.util.UUID;

public class NaturalCountTrial extends Trial {
    public NaturalCountTrial(UUID collector) {
        super(collector);
    }

    public NaturalCountTrial(UUID collector, Location location) {
        super(collector, location);
    }
}
