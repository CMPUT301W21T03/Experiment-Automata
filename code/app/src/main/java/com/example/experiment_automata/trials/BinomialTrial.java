package com.example.experiment_automata.trials;

import android.location.Location;

import java.util.UUID;

public class BinomialTrial extends Trial {
    private boolean result;

    public BinomialTrial(UUID collector) {
        super(collector);
    }

    public BinomialTrial(UUID collector, Location location) {
        super(collector, location);
    }

    public boolean getResult() {
        return result;
    }
}
