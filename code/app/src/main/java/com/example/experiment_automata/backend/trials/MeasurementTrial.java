package com.example.experiment_automata.backend.trials;

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

public class MeasurementTrial extends Trial<Float> {
    public MeasurementTrial(UUID collector, float result) {
        super(collector, result);
        this.result = result;
    }

    public MeasurementTrial(UUID collector, Location location, float result) {
        super(collector, location, result);
        this.result = result;
    }

    /**
     *  gets the result of a single measurement trial that was taken.
     * @return
     *  The single trial result.
     */
    public Float getResult() {
        return result;
    }

    /**
     * get the the type of the current trial
     *
     * @return the type of the current trial
     */
    @Override
    public String getType() {
        return "Measurement";
    }

    /**
     * sets the result so something
     * @param result
     *  the result we want to set
     */
    public void setResult(float result)
    {
        this.result = result;
    }
}
