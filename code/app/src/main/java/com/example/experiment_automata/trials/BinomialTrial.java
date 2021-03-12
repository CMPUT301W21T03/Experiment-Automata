package com.example.experiment_automata.trials;

import android.location.Location;

import java.util.UUID;

public class BinomialTrial extends Trial {
    private boolean result;

    public BinomialTrial(UUID collector, boolean result) {
        super(collector);
        this.result = result;
    }

    public BinomialTrial(UUID collector, Location location, boolean result) {
        super(collector, location);
        this.result = result;
    }

    public boolean getResult() {
        return result;
    }
}
