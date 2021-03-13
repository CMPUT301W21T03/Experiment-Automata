package com.example.experiment_automata.Experiments.ExperimentModel;

import com.example.experiment_automata.CustomExceptions.IllegalExperimentException;

import java.util.UUID;

public class ExperimentMaker
{
    private String description; // Do we need this to have any attributes? can't we just create an experiment maker when needed?

    /**
     * Will create a desired experiment type with a description
     * @param type
     *   the type of experiment that will be created
     * @param description
     *   the description of the experiment to be created
     * @return
     *   an experiment object of the requested type
     * @throws IllegalExperimentException
     *   will be thrown if ExperimentType is an illegal type
     */
    public Experiment makeExperiment(ExperimentType type, String description) {
        switch (type) {
            case NaturalCount:
                return new NaturalCountExperiment(description);
            case Binomial:
                return new BinomialExperiment(description);
            case Count:
                return new CountExperiment(description);
            case Measurement:
                return new MeasurementExperiment(description);
            default:
                return null;
        }
    }

    /**
     * Will create an experiment with the selected type from the Add Experiment Fragment
     * @param type
     *   the type of experiment that will be created
     * @param description
     *   the description of the experiment to be created
     * @param minTrials
     *   the minimum number of trials for this experiment
     * @param requireLocation
     *   a boolean for whether this experiment requires location
     * @param acceptNewResults
     *   a boolean for whether this experiment is accepting new results
     * @return
     *   an experiment object of the requested type with information
     * @throws IllegalExperimentException
     *   will be thrown if ExperimentType is an illegal type.
     */
    public Experiment makeExperiment(ExperimentType type, String description, int minTrials,
                                     boolean requireLocation, boolean acceptNewResults,
                                     UUID ownerId) {
        switch (type) {
            case NaturalCount:
                return new NaturalCountExperiment(description, minTrials, requireLocation, acceptNewResults, ownerId);
            case Binomial:
                return new BinomialExperiment(description, minTrials, requireLocation, acceptNewResults, ownerId);
            case Count:
                return new CountExperiment(description, minTrials, requireLocation, acceptNewResults, ownerId);
            case Measurement:
                return new MeasurementExperiment(description, minTrials, requireLocation, acceptNewResults, ownerId);
            default:
                return null;
        }
    }
}
