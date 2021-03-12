package com.example.experiment_automata;

import com.example.experiment_automata.trials.NaturalCountTrial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Holds the information needed to maintain a natural count experiment
 */
public class NaturalCountExperiment extends Experiment {
    private Collection<NaturalCountTrial> results;

    /**
     * Default constructor for Natural Count Experiment with just a description
     * @param description
     *   the description for this experiment
     */
    public NaturalCountExperiment(String description) {
        super(description);
        results = new ArrayList<>();
    }

    /**
     * Default constructor for Natural Count Experiment from using the AddExperimentFragment
     * @param description
     *   the description for the experiment
     * @param minTrials
     *   the minimum number of trials for the experiment
     * @param requireLocation
     *   a boolean for if the trials need a location
     * @param acceptNewResults
     *   a boolean for if the experiment is accepting new results
     */
    public NaturalCountExperiment(String description, int minTrials, boolean requireLocation, boolean acceptNewResults, UUID ownerId) {
        super(description, minTrials, requireLocation, acceptNewResults, ownerId, ExperimentType.NaturalCount);
        results = new ArrayList<>();
    }

    /**
     * Record a trial.
     * @param trial
     *  the trail to add
     */
    public void recordTrial(NaturalCountTrial trial) {
        if (active) {
            results.add(trial);
        } else {
            throw new IllegalStateException("Experiment is not accepting new results.");
        }
    }
}
