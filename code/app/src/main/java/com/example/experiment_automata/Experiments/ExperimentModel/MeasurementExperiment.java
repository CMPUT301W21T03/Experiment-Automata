package com.example.experiment_automata.Experiments.ExperimentModel;

import com.example.experiment_automata.trials.MeasurementTrial;
import com.example.experiment_automata.trials.NaturalCountTrial;
import com.example.experiment_automata.trials.Trial;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Role/Pattern:
 *      Holds the information needed to maintain a measurement experiment
 *
 *  Known Issue:
 *
 *      1. None
 */
public class MeasurementExperiment extends Experiment {
    private Collection<MeasurementTrial> results;

    /**
     * Default constructor for Measurement Experiment with just a description
     * @param description
     *   the description for this experiment
     */
    public MeasurementExperiment(String description) {
        super(description);
        results = new ArrayList<>();
    }

    /**
     * Default constructor for Measurement Experiment from using the AddExperimentFragment
     * @param description
     *   the description for the experiment
     * @param minTrials
     *   the minimum number of trials for the experiment
     * @param requireLocation
     *   a boolean for if the trials need a location
     * @param acceptNewResults
     *   a boolean for if the experiment is accepting new results
     */
    public MeasurementExperiment(String description, int minTrials, boolean requireLocation, boolean acceptNewResults, UUID ownerId) {
        super(description, minTrials, requireLocation, acceptNewResults, ownerId, ExperimentType.Measurement);
        results = new ArrayList<>();
    }

    /**
     * Record a trial.
     * @param trial
     *  the trail to add
     */
    public void recordTrial(MeasurementTrial trial) {
        if (active) {
            results.add(trial);
        } else {
            throw new IllegalStateException("Experiment is not accepting new results.");
        }
    }

    /**
     * Generate a list of entries needed to plot a histogram
     * @param trials
     *  trials to use for data
     * @return
     *  the list of entries that represent a histogram of trials.
     */
    public List<Entry> generateHistogram(Collection<Trial> trials) {
        // Get range of values (min/max)
        float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY;
        for (Trial trial: trials) {
            final MeasurementTrial measurementTrial = (MeasurementTrial) trial;
            float value = measurementTrial.getResult();
            if (value > max) max = value;
            if (value < min) min = value;
        }
        float range = min + max;
        // Get data range counts into bins
        final int amountOfBins = 10;
        int[] bins = new int[amountOfBins];
        for (int i = 0; i < amountOfBins; i++) { bins[i] = 0; }
        for (Trial trial: trials) {
            final MeasurementTrial measurementTrial = (MeasurementTrial) trial;
            float value = measurementTrial.getResult();
            int bin = (int) ((value - min) / range * amountOfBins);
            if (bin == amountOfBins) bin--;
            bins[bin]++;
        }
        // Convert bins to entries
        List<Entry> data = new ArrayList<>();
        for (int i = 0; i < amountOfBins; i++) {
            data.add(new Entry(i, bins[i]));
        }
        return data;
    }

    /**
     * Generate a list of entries needed to plot results of trials.
     * @param trials
     *  trials to use for data
     * @return
     *  the list of entries that represent a plot
     */
    public List<Entry> generatePlot(Collection<Trial> trials) {
        List<Entry> data = new ArrayList<>();
        for (Trial trial : trials ) {
            final MeasurementTrial measurementTrial = (MeasurementTrial) trial;
            data.add(new Entry(measurementTrial.getDate().getTime(), measurementTrial.getResult()));
        }
        return data;
    }

    /**
     * Gets the mean value of the trials.
     * @return
     *  the mean
     */
    public float getMean() {
        float sum = 0;
        int numTrials = 0;
        for (MeasurementTrial trial : results) {
            sum += trial.getResult();
            numTrials += 1;
        }
        // Return sum of all the results divided by the number of trials
        return (float) sum/numTrials;
    }

    /**
     * Gets the median value of the trials.
     * @return
     *  the median
     */
    public float getMedian() {
        ArrayList<Float> values = new ArrayList<>();
        for (MeasurementTrial trial : results) {
            values.add(trial.getResult());
        }
        Collections.sort(values);
        int size = values.size();
        if (size % 2 == 0) {
            final float val1, val2;
            val1 = values.get(size / 2);
            val2 = values.get((size / 2) - 1);
            return (val1 + val2) / 2f;
        } else {
            return values.get((size - 1) / 2);
        }

    }

    /**
     * Gets the standard deviation of the trials.
     * @return
     *  the standard deviation
     */
    public float getStdev() {
        float sum = 0;
        for (MeasurementTrial trial : results) {
            sum += Math.pow(trial.getResult() - getMean(), 2);
        }
        return (float) Math.sqrt(sum / results.size());
    }

    /**
     * Gets the quartiles of the trials
     * @return
     *  the quartiles
     */
    public float[][] getQuartiles() {
        float[][] quartiles = new float[4][2];
        return quartiles;
    }
}
