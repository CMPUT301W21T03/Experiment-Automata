package com.example.experiment_automata.backend.experiments;


import com.example.experiment_automata.backend.trials.CountTrial;
import com.example.experiment_automata.backend.trials.Trial;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

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
 *      1. stat calculation implementation is not final awaiting input from customer on
 *         how count experiments should analyzed.
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
    public void recordTrial(Trial trial) {
        if (active) {
            results.add((CountTrial) trial);
        } else {
            throw new IllegalStateException("Experiment is not accepting new results.");
        }
    }

    /**
     * Generate a list of entries needed to plot a histogram
     * @return
     *  the list of entries that represent a histogram of trials.
     */
    public List<BarEntry> generateHistogram() {
        List<BarEntry> data = new ArrayList<>();
        data.add(new BarEntry(0, results.size()));
        return data;
    }

    /**
     * Generate a list of entries needed to plot results of trials.
     * @return
     *  the list of entries that represent a plot
     */
    public List<Entry> generatePlot() {
        List<Entry> data = new ArrayList<>();
        long offset = 0;
        int i = 0;
        for (CountTrial trial : results ) {
            if (i == 0) offset = trial.getDate().getTime();
            data.add(new Entry(trial.getDate().getTime() - offset, ++i));
        }
        return data;
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

    /**
     * Gets the size of the experiment
     * @return size of the experiment
     */
    public Integer getSize(){
        return results.size();
    }

    public Collection<CountTrial> getTrials() { return results; }
}
