package com.example.experiment_automata.backend.trials;

import android.location.Location;

import java.util.UUID;

/**
 * Role/Pattern:
 *     Holds the information needed to maintain and track a binomial trial
 */
public class BinomialTrial extends Trial<Boolean> {
    public BinomialTrial(UUID collector, boolean ignore, long timestamp, boolean result) {
        super(collector, ignore, timestamp, result);
    }

    public BinomialTrial(UUID collector, boolean ignore, long timestamp, Location location, boolean result) {
        super(collector, ignore, timestamp, location, result);
    }

    /**
     * get the the type of the current trial
     *
     * @return the type of the current trial
     */
    @Override
    public String getType() {
        return "Binomial";
    }
}
