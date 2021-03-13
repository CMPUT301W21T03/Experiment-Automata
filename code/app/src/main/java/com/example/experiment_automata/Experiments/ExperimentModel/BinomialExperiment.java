package com.example.experiment_automata.Experiments.ExperimentModel;

import com.example.experiment_automata.trials.BinomialTrial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;


/**
 * Role/Pattern:
 *      Holds the main data needed for a binomial experiment
 *
 * Known Issue:
 *
 *      1. None
 */
public class BinomialExperiment extends Experiment {
    private Collection<BinomialTrial> results;

    /**
     * Default constructor for Binomial Experiment with just a description
     * @param description
     *   the description for this experiment
     */
    public BinomialExperiment(String description) {
        super(description);
        results = new ArrayList<>();
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
    public BinomialExperiment(String description, int minTrials, boolean requireLocation, boolean acceptNewResults, UUID ownerId) {
        super(description, minTrials, requireLocation, acceptNewResults, ownerId, ExperimentType.Binomial);
        results = new ArrayList<>();
    }

    /**
     * Record a trial.
     * @param trial
     *  the trail to add
     */
    public void recordTrial(BinomialTrial trial) {
        if (active) {
            results.add(trial);
        } else {
            throw new IllegalStateException("Experiment is not accepting new results.");
        }
    }
}
