package com.example.experiment_automata;

import java.util.Collection;
import java.util.UUID;

/**
 * Class needed to maintain the type of experiments needed!
 */
public abstract class Experiment {

    private String description;
    private int minTrials;
    private UUID experimentId; // changed from UML to better match project
    private UUID ownerId; // // changed from UML to better match project
    private boolean active; // changed form UML for style
    private boolean published; // changed from UML for style
    private ExperimentType type;
    private Collection<UUID> crowedExperimenter; // Experimenter id's


    public Experiment() {
        // Stub should do nothing of yet
        // someone will build experiment class
    }

    public Experiment(String description)
    {
        this.description = description;
    }

    public boolean compare(Experiment experiment) {
        return experimentId.equals(experiment.experimentId);
    }

    public abstract void stub();

}
