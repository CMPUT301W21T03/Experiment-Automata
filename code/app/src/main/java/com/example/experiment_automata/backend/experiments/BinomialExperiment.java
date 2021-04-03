package com.example.experiment_automata.backend.experiments;

import com.example.experiment_automata.backend.trials.BinomialTrial;
import com.example.experiment_automata.backend.trials.Trial;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * Role/Pattern:
 *      Holds the main data needed for a binomial experiment
 *
 * Known Issue:
 *
 *      1. None
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
     * Firestore constructor for Binomial Experiment
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
    public BinomialExperiment(String description, int minTrials, boolean requireLocation, boolean acceptNewResults, UUID ownerId, Boolean published, UUID experimentId) {
        super(description, minTrials, requireLocation, acceptNewResults, ownerId, ExperimentType.Binomial,published, experimentId);
        results = new ArrayList<>();
    }

    /**
     * Record a trial.
     * @param trial
     *  the trail to add
     */
    public void recordTrial(Trial trial) {
        if (active) {
            results.add((BinomialTrial) trial);
            postExperimentToFirestore();
        } else {
            throw new IllegalStateException("Experiment is not accepting new results.");
        }
    }

    public void recordTrial(Trial trial, Boolean fromFirestore) {
        if (fromFirestore) results.add((BinomialTrial) trial);
    }

    /**
     * gets all the recorded trials for an experiment
     *
     * @return the recorded trials
     */
    @Override
    public ArrayList<Trial> getRecordedTrials() {
        return new ArrayList<>(results);
    }

    /**
     * Generate a list of entries needed to plot a histogram
     * @return
     *  the list of entries that represent a histogram of trials.
     */
    public List<BarEntry> generateHistogram() {
        // Get data range counts into bins
        final int amountOfBins = 2;
        int[] bins = new int[amountOfBins];
        for (int i = 0; i < amountOfBins; i++) { bins[i] = 0; }
        for (BinomialTrial trial: results) {
            if (!trial.isIgnored()) {
                boolean value = trial.getResult();
                int bin = value ? 1 : 0;
                bins[bin]++;
            }
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
        boolean first = true;
        long offset = 0;
        for (BinomialTrial trial : results ) {
            if (!trial.isIgnored()) {
                if (first) {
                    first = false;
                    offset = trial.getDate().getTime();
                }
                data.add(new Entry(trial.getDate().getTime() - offset, trial.getResult() ? 1 : 0));
            }
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

        for (Trial trial: results) {
            if (!trial.isIgnored()) {
                totalTrials = totalTrials + 1;
                final BinomialTrial binomialTrial = (BinomialTrial) trial;
                if (binomialTrial.getResult()) {
                    successfulTrials = successfulTrials + 1;
                }
            }
        }
        float answer;
        if (totalTrials > 0) {
            answer = ((float) successfulTrials) / (totalTrials);
        } else {
            // No results
            answer = 0;
        }
        return answer;
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
            if (!trial.isIgnored()) {
                if (trial.getResult()) {
                    successfulTrials = successfulTrials + 1;
                } else {
                    failureTrials = failureTrials + 1;
                }
            }
        }
        if (successfulTrials > failureTrials) {
            // More successes than failures
            return 1f;
        } else if (successfulTrials == failureTrials) {
            return 0.5f;
        } else {
            // More failures than successes
            return 0f;
        }
    }

    /**
     * Computes the median of any arraylist of integers (useful in quartiles function)
     * @param values An arraylist of integers
     * @return the median of the floats
     */
    private float getMedian(ArrayList<Integer> values) {
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
        float result;
        for (BinomialTrial trial : results) {
            if (!trial.isIgnored()) {
                if (trial.getResult()) {
                    result = 1f;
                } else {
                    result = 0f;
                }
                sum += Math.pow(result - getMean(), 2);
            }
        }
        return (float) Math.sqrt(sum / getSize());
    }

    /**
     * Gets the quartiles of the trials
     * @return
     *  the quartiles
     */
    public float[] getQuartiles() {
        float[] quartiles = new float[3];
        final int size = getSize();
        quartiles[1] = getMedian();
        // Can only compute other quartiles if there's at least 4 data points
        if (size >= 4) {
            // Sort all the values in results
            ArrayList<Integer> values = new ArrayList<>();
            for (BinomialTrial trial : results) {
                if (!trial.isIgnored()) {
                    if (trial.getResult()) {
                        // Add 1 for a positive result and 0 otherwise
                        values.add(1);
                    } else {
                        values.add(0);
                    }
                }
            }
            Collections.sort(values);
            int highPoint;
            // If we have an array of size 5, then we want to find the median of (0 to 1) and (3 to 4)
            // If we have an array of size 4, then we want to find the median of (0 to 1) and (2 to 3)
            int lowPoint = size / 2 - 1;
            if (size % 2 == 0 ) {
                highPoint = lowPoint + 1;
            } else {
                highPoint = lowPoint + 2;
            }

            ArrayList<Integer> valuesSmall = new ArrayList<>();
            ArrayList<Integer> valuesLarge = new ArrayList<>();
            for (int i = 0; i <= lowPoint; i++) {
                valuesSmall.add(values.get(i));
            }

            for (int i = highPoint; i < values.size(); i++) {
                valuesLarge.add(values.get(i));
            }

            quartiles[0] = getMedian(valuesSmall);

            quartiles[2] = getMedian(valuesLarge);
        } else if (size == 3) {
            // Sort all the values in results
            ArrayList<Integer> values = new ArrayList<>();
            for (BinomialTrial trial : results) {
                if (!trial.isIgnored()) {
                    if (trial.getResult()) {
                        // Add 1 for a positive result and 0 otherwise
                        values.add(1);
                    } else {
                        values.add(0);
                    }
                }
            }
            Collections.sort(values);
            // Set the three numbers to the three values
            quartiles[0] = values.get(0);
            quartiles[1] = values.get(1);
            quartiles[2] = values.get(2);
        }

        return quartiles;
    }

    /**
     * Build hashmap for results
     */
    public HashMap<String,Object> buildResultsmap(){
        HashMap<String,Object> resultsData = new HashMap<String, Object>();
        if (results == null){
            return resultsData;
        }
        for(Trial trial : results){//this for loop cannot be in super class but its contents can
            HashMap<String,Object> singleResult = new HashMap<String, Object>();
            singleResult.put("owner-id",trial.getUserId().toString());
            if (trial.getLocation() != null){//maybe move to a method in superclass
                singleResult.put("latitude",trial.getLocation().getLatitude());
                singleResult.put("longitude",trial.getLocation().getLongitude());
            }
            singleResult.put("date",trial.getDate().toString());
            singleResult.put("ignore",trial.isIgnored());
            singleResult.put("result",trial.getResult());
            resultsData.put(trial.getTrialId().toString(),singleResult);
        }
        return resultsData;
    }
    /**
     * Gets the size of the trials. Does not include ignored trials.
     * @return size of the trials
     */
    public Integer getSize(){
        int size = 0;
        for (Trial trial : results) {
            if (!trial.isIgnored()) {
                size++;
            }
        }
        return size;
    }

    public Collection<BinomialTrial> getResults() {
        return results;
    }

    public Collection<BinomialTrial> getTrials() { return results; }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     *
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Object o) {
        Experiment ec = (Experiment)o;
        return super.getDescription().toLowerCase().compareTo(ec.getDescription().toLowerCase());
    }
}
