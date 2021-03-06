package com.example.experiment_automata;

/**
 * Holds the information needed to maintain a natural count experiment
 */

public class NaturalCountExperiment extends Experiment
{
    public NaturalCountExperiment(String description) {
        super(description);
    }
    public NaturalCountExperiment(String description, int minTrials, boolean requireLocation, boolean acceptNewResults) {
        super(description, minTrials, requireLocation, acceptNewResults);
    }

    @Override
    public void stub() {

    }
}
