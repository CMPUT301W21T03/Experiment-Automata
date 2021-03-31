package com.example.experiment_automata.backend.trials;

import android.location.Location;

import java.util.UUID;

/**
 * Role/Pattern:
 *     Holds the information needed to maintain and track a binomial trial
 *
 *  Known Issue:
 *
 *      1. None
 */
public class BinomialTrial extends Trial<Boolean> {
    public BinomialTrial(UUID collector, boolean result) {
        super(collector, result);
    }

    public BinomialTrial(UUID collector, Location location, boolean result) {
        super(collector, location, result);
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
