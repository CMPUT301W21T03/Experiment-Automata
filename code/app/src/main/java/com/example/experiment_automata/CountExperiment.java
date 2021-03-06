package com.example.experiment_automata;


/**
 * Holds the information needed to maintain a count experiment
 */

public class CountExperiment extends Experiment
{
    public CountExperiment(String description) {
        super(description);
    }
    public CountExperiment(String description, int minTrials, boolean requireLocation, boolean acceptNewResults) {
        super(description, minTrials, requireLocation, acceptNewResults);
    }

    @Override
    public void stub() {

    }
}
