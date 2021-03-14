package com.example.experiment_automata;

import com.example.experiment_automata.trials.Trial;

import java.util.Collection;

public class StatSummary {
    /**
     * Gets the mean value of the trials.
     * @param trials
     *  trials to get mean from
     * @return
     *  the mean
     */
    public static float getMean(Collection<Trial> trials) {
        return 0;
    }

    /**
     * Gets the median value of the trials.
     * @param trials
     *  trials to get median from
     * @return
     *  the median
     */
    public static float getMedian(Collection<Trial> trials) {
        return 0;
    }

    /**
     * Gets the standard deviation of the trials.
     * @param trials
     *  the trials to get the standard deviation from
     * @return
     *  the standard deviation
     */
    public static float getStdev(Collection<Trial> trials) {
        return 0;
    }

    /**
     * Gets the quartiles of the trials
     * @param trials
     *  the trials to get the quartiles from
     * @return
     *  the quartiles
     */
    public static float[][] getQuartiles(Collection<Trial> trials) {
        float[][] quartiles = new float[4][2];
        return quartiles;
    }
}
