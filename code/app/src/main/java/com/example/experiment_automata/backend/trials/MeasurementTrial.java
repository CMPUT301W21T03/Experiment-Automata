package com.example.experiment_automata.backend.trials;

import android.location.Location;

import java.util.UUID;

/**
 * Role/Pattern:
 *     Holds the information needed to maintain and track a measurement trial
 */
public class MeasurementTrial extends Trial<Float> {
    public MeasurementTrial(UUID collector, long timestamp, float result) {
        super(collector, timestamp, result);
        this.result = result;
    }

    public MeasurementTrial(UUID collector, long timestamp, Location location, float result) {
        super(collector, timestamp, location, result);
        this.result = result;
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
}
