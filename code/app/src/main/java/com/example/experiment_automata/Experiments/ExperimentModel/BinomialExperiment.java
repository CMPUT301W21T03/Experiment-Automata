package com.example.experiment_automata.Experiments.ExperimentModel;

import com.example.experiment_automata.trials.BinomialTrial;
import com.example.experiment_automata.trials.Trial;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Holds the main data needed for a binomial data
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

    /**
     * Generate a list of entries needed to plot a histogram
     * @param trials
     *  trials to use for data
     * @return
     *  the list of entries that represent a histogram of trials.
     */
    public List<Entry> generateHistogram(Collection<Trial> trials) {
        // Get data range counts into bins
        final int amountOfBins = 2;
        int[] bins = new int[amountOfBins];
        for (int i = 0; i < amountOfBins; i++) { bins[i] = 0; }
        for (Trial trial: trials) {
            final BinomialTrial naturalCountTrial = (BinomialTrial) trial;
            boolean value = naturalCountTrial.getResult();
            int bin = value ? 1 : 0;
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
            final BinomialTrial binomialTrial = (BinomialTrial) trial;
            data.add(new Entry(binomialTrial.getDate().getTime(),
                    binomialTrial.getResult() ? 1 : 0));
        }
        return data;
    }

    /**
     * Gets the mean value of the trials.
     * @return
     *  the mean
     */
    public float getMean() {
        int totalTrials = 0;
        int successfulTrials = 0;

        for(Trial trial: results) {
            totalTrials = totalTrials + 1;
            final BinomialTrial binomialTrial = (BinomialTrial) trial;
            if (binomialTrial.getResult()) {
                successfulTrials = successfulTrials + 1;
            }
        }
        float answer;
        if(totalTrials>0) {
            answer = ((float) successfulTrials) / (totalTrials);
        }
        else{
            // No results
            answer=0;
        }
        // Round to six decimal places (for now), we can change the precision later

        System.out.println(Math.round(1000000*answer));
        System.out.println((float) Math.round(1000000*answer)/1000000);
        return ((float) Math.round(1000000*answer)/1000000);
    }

    /**
     * Gets the median value of the trials.
     * @return
     *  the median
     */
    public float getMedian() {
        // For a binomial experiment, the median is just the value that occurs the most
        int successfulTrials = 0;
        int failureTrials = 0;

        for(BinomialTrial trial: results) {
            if (trial.getResult()) {
                successfulTrials = successfulTrials + 1;
            }
            else{
                failureTrials = failureTrials + 1;
            }
        }
        if(successfulTrials > failureTrials){
            // More successes than failures
            return 1;
        }
        else if(successfulTrials == failureTrials){
            return (float) 0.5;
        }
        else{
            // More failures than successes
            return 0;
        }
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
    public float[][] getQuartiles() {
        float[][] quartiles = new float[4][2];
        return quartiles;
    }
}
