package com.example.experiment_automata;

/**
 * Holds the main data needed for a binomial data
 */
public class BinomialExperiment extends Experiment
{
    /**
     * Default constructor for Binomial Experiment with just a description
     * @param description
     *   the description for this experiment
     */
    public BinomialExperiment(String description) {
        super(description);
    }

    /**
     * Default constructor for Binomial Experiment from using the AddExperimentFragment
     * @param description
     *   the description for the experiment
     * @param minTrials
     *   the minimum number of trials for the experiment
     * @param requireLocation
     *   a boolean for if the trials need a location
     * @param acceptNewResults
     *   a boolean for if the experiment is accepting new results
     */
    public BinomialExperiment(String description, int minTrials, boolean requireLocation, boolean acceptNewResults) {
        super(description, minTrials, requireLocation, acceptNewResults);
    }

    @Override
    public void stub() {

    }
}
