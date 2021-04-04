package com.example.experiment_automata.backend.experiments;

import com.example.experiment_automata.backend.trials.Trial;

import java.util.UUID;

/**
 * Role/Pattern:
 *      This class is meant to make an experiment of the type the user want to take away the
 *      the works needed by other classes. This is of course using the factory pattern
 *      for the given experiments.
 *
 *  Known Issue:
 *
 *      1. None
 */
public class ExperimentMaker {

    /**
     * Will create a desired experiment type with a description
     * @param type
     *   the type of experiment that will be created
     * @param description
     *   the description of the experiment to be created
     * @return
     *   an experiment object of the requested type
     */
    public Experiment<?> makeExperiment(ExperimentType type, String description) {
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
     */
    public static Experiment<?> makeExperiment(ExperimentType type, String description, int minTrials,
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
    /**
     * Will create an experiment with the selected type from Firestore
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
     * @param published
     *   a boolean for whether this experiment is published
     * @param experimentId
     *   UUID representing the current experiment
     * @return
     *   an experiment object of the requested type with information
     */
    public static Experiment<?> makeExperiment(ExperimentType type, String description, int minTrials,
                                                                boolean requireLocation, boolean acceptNewResults,
                                                                UUID ownerId, Boolean published, UUID experimentId) {
        switch (type) {
            case NaturalCount:
                return new NaturalCountExperiment(description, minTrials, requireLocation, acceptNewResults, ownerId, published, experimentId);
            case Binomial:
                return new BinomialExperiment(description, minTrials, requireLocation, acceptNewResults, ownerId, published, experimentId);
            case Count:
                return new CountExperiment(description, minTrials, requireLocation, acceptNewResults, ownerId, published, experimentId);
            case Measurement:
                return new MeasurementExperiment(description, minTrials, requireLocation, acceptNewResults, ownerId, published, experimentId);
            default:
                return null;
        }
    }


}
