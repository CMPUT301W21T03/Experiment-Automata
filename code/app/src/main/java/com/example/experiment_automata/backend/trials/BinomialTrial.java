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

    /**
     * get the the type of the current trial
     *
     * @return the type of the current trial
     */
    @Override
    public String getType() {
        return "Binomial";
    }

    /**
     * sets the the result to a specified value
     *
     * @param result the new result that will be made
     */
    public void setResult(boolean result)
    {
        this.result = result;
    }
}
