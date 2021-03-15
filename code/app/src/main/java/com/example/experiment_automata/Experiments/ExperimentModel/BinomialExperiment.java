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
            final BinomialTrial naturalCountTrial = (BinomialTrial) trial;
            data.add(new Entry(naturalCountTrial.getDate().getTime(),
                    naturalCountTrial.getResult() ? 1 : 0));
        }
        return data;
    }
}
