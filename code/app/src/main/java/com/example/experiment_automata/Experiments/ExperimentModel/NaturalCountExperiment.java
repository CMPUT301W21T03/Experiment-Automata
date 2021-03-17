package com.example.experiment_automata.Experiments.ExperimentModel;

import com.example.experiment_automata.trials.MeasurementTrial;
import com.example.experiment_automata.trials.NaturalCountTrial;
import com.example.experiment_automata.trials.Trial;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Role/Pattern:
 *     Holds the information needed to maintain a natural count experiment
 *
 *  Known Issue:
 *
 *      1. None
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

    /**
     * Generate a list of entries needed to plot a histogram
     * @return
     *  the list of entries that represent a histogram of trials.
     */
    public List<BarEntry> generateHistogram() {
        // Get range of values (min/max)
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (NaturalCountTrial trial : results) {
            int value = trial.getResult();
            if (value > max) max = value;
            if (value < min) min = value;
        }
        int range = min + max;
        // Get data range counts into bins
        final int amountOfBins = 10;
        int[] bins = new int[amountOfBins];
        for (int i = 0; i < amountOfBins; i++) { bins[i] = 0; }
        for (NaturalCountTrial trial : results) {
            float value = trial.getResult();
            int bin = (int) ((value - min) / range * amountOfBins);
            if (bin == amountOfBins) bin--;
            bins[bin]++;
        }
        // Convert bins to entries
        List<BarEntry> data = new ArrayList<>();
        for (int i = 0; i < amountOfBins; i++) {
            data.add(new BarEntry(i, bins[i]));
        }
        return data;
    }

    /**
     * Generate a list of entries needed to plot results of trials.
     * @return
     *  the list of entries that represent a plot
     */
    public List<Entry> generatePlot() {
        List<Entry> data = new ArrayList<>();
        for (NaturalCountTrial trial : results ) {
            data.add(new Entry(trial.getDate().getTime(), trial.getResult()));
        }
        return data;
    }

    /**
     * Gets the mean value of the trials.
     * @return
     *  the mean
     */
    public float getMean() {
        int sum = 0;
        int numTrials = 0;
        for (NaturalCountTrial trial : results) {
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
        ArrayList<Integer> values = new ArrayList<>();
        for (NaturalCountTrial trial : results) {
            values.add(trial.getResult());
        }
        Collections.sort(values);
        int size = values.size();
        if (size % 2 == 0) {
            final int val1, val2;
            val1 = values.get(size / 2);
            val2 = values.get((size / 2) - 1);
            return (val1 + val2) / 2f;
        } else {
            return values.get((size - 1) / 2);
        }
    }

    /**
     * Computes the median of any arraylist of integers (useful in quartiles function)
     * @param values An arraylist of integers
     * @return the median of the floats
     */
    public float getMedianList(ArrayList<Integer> values) {
        // Implementation exactly the same as getMedian above
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
        for (NaturalCountTrial trial : results) {
            sum += Math.pow(trial.getResult() - getMean(), 2);
        }
        return (float) Math.sqrt(sum / results.size());
    }

    /**
     * Gets the quartiles of the trials
     * @return
     *  the quartiles
     */
    public float[] getQuartiles() {
        float[] quartiles = new float[3];
        quartiles[1]=getMedian();
        // Can only compute other quartiles if there's at least 4 data points
        if(results.size() >= 4){
            // Sort all the values in results
            ArrayList<Integer> values = new ArrayList<>();
            for (NaturalCountTrial trial : results) {
                values.add(trial.getResult());
            }
            Collections.sort(values);
            int highPoint;
            // If we have an array of size 5, then we want to find the median of (0 to 1) and (3 to 4)
            // If we have an array of size 4, then we want to find the median of (0 to 1) and (2 to 3)
            int lowPoint = results.size()/2-1;
            if(results.size()%2 == 0 ){
                highPoint = lowPoint+1;
            }
            else{
                highPoint = lowPoint+2;
            }

            ArrayList<Integer> valuesSmall = new ArrayList<>();
            ArrayList<Integer> valuesLarge = new ArrayList<>();
            for(int i = 0; i <= lowPoint; i++){
                valuesSmall.add(values.get(i));
            }

            for(int i=highPoint; i<values.size(); i++){
                valuesLarge.add(values.get(i));
            }

            quartiles[0] = getMedianList(valuesSmall);

            quartiles[2] = getMedianList(valuesLarge);
        }

        else if(results.size() == 3){
            ArrayList<Integer> values = new ArrayList<>();
            for (NaturalCountTrial trial : results) {
                values.add(trial.getResult());
            }
            Collections.sort(values);

            quartiles[0] = values.get(0);
            quartiles[1] = values.get(1);
            quartiles[2] = values.get(2);
        }

        return quartiles;

    }

    /**
     * Gets the size of the experiment
     * @return size of the experiment
     */
    public Integer getSize(){
        return results.size();
    }

}
