package com.example.experiment_automata;

/**
 * Holds the main data needed for a binomial data
 */
public class BinomialExperiment extends Experiment
{
    public BinomialExperiment(String description) {
        super(description);
    }
    public BinomialExperiment(String description, int minTrials, boolean requireLocation, boolean acceptNewResults) {
        super(description, minTrials, requireLocation, acceptNewResults);
    }

    @Override
    public void stub() {

    }
}
