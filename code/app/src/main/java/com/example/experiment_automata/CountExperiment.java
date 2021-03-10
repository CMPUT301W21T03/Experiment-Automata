package com.example.experiment_automata;


import java.util.UUID;

/**
 * Holds the information needed to maintain a count experiment
 */

public class CountExperiment extends Experiment
{
    /**
     * Default constructor for Count Experiment with just a description
     * @param description
     *   the description for this experiment
     */
    public CountExperiment(String description) {
        super(description);
    }

    /**
     * Default constructor for Count Experiment from using the AddExperimentFragment
     * @param description
     *   the description for the experiment
     * @param minTrials
     *   the minimum number of trials for the experiment
     * @param requireLocation
     *   a boolean for if the trials need a location
     * @param acceptNewResults
     *   a boolean for if the experiment is accepting new results
     */
    public CountExperiment(String description, int minTrials, boolean requireLocation, boolean acceptNewResults, UUID ownerId) {
        super(description, minTrials, requireLocation, acceptNewResults, ownerId);
    }

    @Override
    public void stub() {

    }
}
