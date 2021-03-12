package com.example.experiment_automata.trials;

import android.location.Location;

import java.util.UUID;

public class NaturalCountTrial extends Trial {
    private int result;

    public NaturalCountTrial(UUID collector, int result) {
        super(collector);
        this.result = result;
    }

    public NaturalCountTrial(UUID collector, Location location, int result) {
        super(collector, location);
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
