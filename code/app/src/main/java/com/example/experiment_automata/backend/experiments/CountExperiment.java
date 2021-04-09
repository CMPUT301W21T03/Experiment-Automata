package com.example.experiment_automata.backend.experiments;

import com.example.experiment_automata.backend.trials.CountTrial;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Role/Pattern:
 *      Holds the information needed to maintain a count experiment
 */
public class CountExperiment extends Experiment<CountTrial> {
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
     * @param ownerId
     *   the UUID for the owner of the experiment
     * @param enableFirestore
     *   whether to enable firestore or not
     */
    public CountExperiment(String description, int minTrials, boolean requireLocation,
                           boolean acceptNewResults, UUID ownerId, String region, boolean enableFirestore) {
        super(description, minTrials, requireLocation, acceptNewResults, ownerId,
                region, ExperimentType.Count, enableFirestore);
    }

    /**
     * Firestore constructor for CountExperiment
     * @param description
     *   the description for the experiment
     * @param minTrials
     *   the minimum number of trials for the experiment
     * @param requireLocation
     *   a boolean for if the trials need a location
     * @param acceptNewResults
     *   a boolean for if the experiment is accepting new results
     * @param published
     *  a boolean for if the experiment is published or not
     * @param experimentId
     * UUID representing the current experiment
     */
    public CountExperiment(String description, int minTrials, boolean requireLocation,
                           boolean acceptNewResults, UUID ownerId, String region, Boolean published, UUID experimentId) {
        super(description, minTrials, requireLocation, acceptNewResults, ownerId,
                region, ExperimentType.Count, published, experimentId);
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
        for (CountTrial trial : this.getTrials()) {
            if (i == 0) offset = trial.getTimestamp();
            data.add(new Entry(trial.getTimestamp() - offset, ++i));
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
        quartiles[0] = 1;
        quartiles[1] = getMedian();
        quartiles[2] = 1;
        return quartiles;
    }
}
