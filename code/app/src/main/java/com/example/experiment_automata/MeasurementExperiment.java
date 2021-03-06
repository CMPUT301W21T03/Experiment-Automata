package com.example.experiment_automata;

/**
 * Holds the information needed to maintain a measurement experiment
 */

public class MeasurementExperiment extends Experiment
{
    public MeasurementExperiment(String description) {
        super(description);
    }
    public MeasurementExperiment(String description, int minTrials, boolean requireLocation, boolean acceptNewResults) {
        super(description, minTrials, requireLocation, acceptNewResults);
    }

    @Override
    public void stub() {

    }
}
