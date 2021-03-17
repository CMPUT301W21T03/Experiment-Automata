package com.example.experiment_automata.Experiments.ExperimentModel;


import android.os.DropBoxManager;

import com.example.experiment_automata.trials.CountTrial;
import com.example.experiment_automata.trials.Trial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    /**
     * Gets the mean value of the trials.
     * @return
     *  the mean
     */
    public float getMean() {
        return 1;
    }

    /**
     * Gets the median value of the trials.
     * @return
     *  the median
     */
    public float getMedian() {
        return 1;
    }

    /**
     * Gets the standard deviation of the trials.
     * @return
     *  the standard deviation
     */
    public float getStdev() {
        return 0;
    }

    /**
     * Gets the quartiles of the trials
     * @return
     *  the quartiles
     */
    public float[] getQuartiles() {
        float[] quartiles = new float[3];
        quartiles[1]=getMedian();
        return quartiles;
    }
}
