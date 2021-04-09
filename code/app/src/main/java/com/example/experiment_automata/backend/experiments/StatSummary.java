package com.example.experiment_automata.backend.experiments;

/**
 * Role/Pattern:
 *     provides contract for stats
 *
 *  Known Issue:
 *
 *      1. None
 */
public interface StatSummary {
    /**
     * Gets the mean value of the trials.
     * @return
     *  the mean
     */
    float getMean();

    /**
     * Gets the median value of the trials.
     * @return
     *  the median
     */
    float getMedian();

    /**
     * Gets the standard deviation of the trials.
     * @return
     *  the standard deviation
     */
    float getStdev();

    /**
     * Gets the quartiles of the trials
     * @return
     *  the quartiles
     */
    float[] getQuartiles();

}
