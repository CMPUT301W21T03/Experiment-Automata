package com.example.experiment_automata.Experiments.ExperimentModel;

import com.example.experiment_automata.trials.MeasurementTrial;
import com.example.experiment_automata.trials.Trial;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Holds the information needed to maintain a measurement experiment
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

    public List<Entry> generatePlot(Collection<Trial> trials) {
        List<Entry> data = new ArrayList<>();
        for (Trial trial : trials ) {
            final MeasurementTrial measurementTrial = (MeasurementTrial) trial;
            data.add(new Entry(measurementTrial.getDate().getTime(), measurementTrial.getResult()));
        }
        return data;
    }
}
