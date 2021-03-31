package com.example.experiment_automata.backend.trials;

import android.location.Location;

import java.util.UUID;

/**
 * Role/Pattern:
 *     Holds the information needed to maintain and track a count trial
 *
 *  Known Issue:
 *
 *      1. None
 */
public class CountTrial extends Trial<Object> {

    public CountTrial(UUID collector) {
        super(collector, null);
    }

    public CountTrial(UUID collector, Location location) {
        super(collector, location, null);
    }

    /**
     * get the the type of the current trial
     *
     * @return the type of the current trial
     */
    @Override
    public String getType() {
        return "Count Trial";
    }

    @Override
    /**
     * Return null since CountTrials don't really hold a value
     */
    public Object getResult() {
        return null;
    }


}
