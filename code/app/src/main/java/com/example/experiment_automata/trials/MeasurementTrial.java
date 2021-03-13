package com.example.experiment_automata.trials;

import android.location.Location;

import java.util.UUID;

/**
 * Role/Pattern:
 *     Holds the information needed to maintain and track a measurement trial
 *
 *  Known Issue:
 *
 *      1. None
 */

public class MeasurementTrial extends Trial {

    private float result;

    public MeasurementTrial(UUID collector, float result) {
        super(collector);
        this.result = result;
    }

    public MeasurementTrial(UUID collector, Location location, float result) {
        super(collector, location);
        this.result = result;
    }

    /**
     *  gets the result of a single measurement trial that was taken.
     * @return
     *  The single trial result.
     */
    public float getResult() {
        return result;
    }
}
