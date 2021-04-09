package com.example.experiment_automata.backend.trials;

import android.location.Location;

import java.util.UUID;

/**
 * Role/Pattern:
 *     Holds the information needed to maintain and track a binomial trial
 */
public class BinomialTrial extends Trial<Boolean> {
    public BinomialTrial(UUID collector, String dateString, boolean result) {
        super(collector, dateString, result);
    }

    public BinomialTrial(UUID collector, String dateString, Location location, boolean result) {
        super(collector, dateString, location, result);
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
