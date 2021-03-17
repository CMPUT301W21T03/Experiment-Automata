package com.example.experiment_automata.trials;

import android.location.Location;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Role/Pattern:
 *     Holds the information needed to maintain and track a count trial
 *
 *  Known Issue:
 *
 *      1. None
 */
public class CountTrial extends Trial {

    public CountTrial(UUID collector) {
        super(collector);
    }

    public CountTrial(UUID collector, Location location) {
        super(collector, location);
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
        final int amountOfBins = 1;
        int[] bins = new int[amountOfBins];
        for (int i = 0; i < amountOfBins; i++) { bins[i] = 0; }
        for (Trial trial: trials) {
            bins[0]++;
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
            data.add(new Entry(trial.getDate().getTime(), 1));
        }
        return data;
    }
}
