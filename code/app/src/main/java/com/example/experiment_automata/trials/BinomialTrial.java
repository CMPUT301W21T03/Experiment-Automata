package com.example.experiment_automata.trials;

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

    /**
     *  gets the result of the single binomial trial recorded.
     * @return
     *  The result of the trial
     */
    public boolean getResult() {
        return result;
    }
}
