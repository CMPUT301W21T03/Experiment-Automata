package com.example.experiment_automata.backend.trials;

import android.location.Location;

import java.util.UUID;

/**
 * Role/Pattern:
 *     Holds the information needed to maintain and track a natural count trial
 */
public class NaturalCountTrial extends Trial<Integer> {
    public NaturalCountTrial(UUID collector, boolean ignore, long timestamp, int result) throws IllegalArgumentException {
        super(collector, ignore, timestamp, result);
        if (result < 0) {
            throw new IllegalArgumentException();
        }
    }

    public NaturalCountTrial(UUID collector, boolean ignore, long timestamp, Location location, int result) throws IllegalArgumentException {
        super(collector, ignore, timestamp, location, result);
        if (result < 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * get the the type of the current trial
     *
     * @return the type of the current trial
     */
    @Override
    public String getType() {
        return "Natural Count";
    }
}
