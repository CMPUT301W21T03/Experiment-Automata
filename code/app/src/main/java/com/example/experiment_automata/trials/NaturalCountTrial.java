package com.example.experiment_automata.trials;

import android.location.Location;

import java.util.UUID;

public class NaturalCountTrial extends Trial {
    private int result;

    public NaturalCountTrial(UUID collector) {
        super(collector);
    }

    public NaturalCountTrial(UUID collector, Location location) {
        super(collector, location);
    }

    public int getResult() {
        return result;
    }
}
