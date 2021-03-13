package com.example.experiment_automata.Experiments.ExperimentModel;


import com.example.experiment_automata.trials.CountTrial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Role/Pattern:
 *      Holds the information needed to maintain a count experiment
 *
 * Known Issue:
 *
 *      1. None
 */
public class CountExperiment extends Experiment {
    private Collection<CountTrial> results;

    /**
     * Default constructor for Count Experiment with just a description
     * @param description
     *   the description for this experiment
     */
    public CountExperiment(String description) {
        super(description);
        results = new ArrayList<>();
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
        super(description, minTrials, requireLocation, acceptNewResults, ownerId, ExperimentType.Count);
        results = new ArrayList<>();
    }

    /**
     * Record a trial.
     * @param trial
     *  the trail to add
     */
    public void recordTrial(CountTrial trial) {
        if (active) {
            results.add(trial);
        } else {
            throw new IllegalStateException("Experiment is not accepting new results.");
        }
    }
}
