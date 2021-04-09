package com.example.experiment_automata.backend.experiments;

/**
 * Role/Pattern:
 *     provides contract for stats
 *
 */
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
    public float[] getQuartiles();

}
