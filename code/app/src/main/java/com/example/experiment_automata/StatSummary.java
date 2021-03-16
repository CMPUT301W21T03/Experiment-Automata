package com.example.experiment_automata;

import com.example.experiment_automata.trials.BinomialTrial;
import com.example.experiment_automata.trials.Trial;

import java.util.Collection;

public interface StatSummary {
    /**
     * Gets the mean value of the trials.
     * @return
     *  the mean
     */
    public float getMean();

    /**
     * Gets the median value of the trials.
     * @return
     *  the median
     */
    public float getMedian();

    /**
     * Gets the standard deviation of the trials.
     * @return
     *  the standard deviation
     */
    public float getStdev();

    /**
     * Gets the quartiles of the trials
     * @return
     *  the quartiles
     */
    public float[][] getQuartiles();
}
