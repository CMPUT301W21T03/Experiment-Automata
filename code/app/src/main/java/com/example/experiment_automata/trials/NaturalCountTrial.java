package com.example.experiment_automata.trials;

import android.location.Location;

import java.util.UUID;

/**
 * Role/Pattern:
 *     Holds the information needed to maintain and track a natural count trial
 *
 *  Known Issue:
 *
 *      1. None
 */
public class NaturalCountTrial extends Trial {

    private int result;

    public NaturalCountTrial(UUID collector, int result) throws IllegalArgumentException {
        super(collector);
        if (result >= 0) {
            this.result = result;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public NaturalCountTrial(UUID collector, Location location, int result) throws IllegalArgumentException {
        super(collector, location);
        if (result >= 0) {
            this.result = result;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     *  gets the result of a single natural count trial that was taken.
     * @return
     *  The single trial result.
     */
    public int getResult() {
        return result;
    }
}
