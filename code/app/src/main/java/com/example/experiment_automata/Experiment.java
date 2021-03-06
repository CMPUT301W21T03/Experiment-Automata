package com.example.experiment_automata;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

/**
 * Class needed to maintain the type of experiments needed!
 */
public abstract class Experiment implements Serializable {

    private String description;
    private int minTrials;
    private UUID experimentId; // changed from UML to better match project
    private UUID ownerId; // // changed from UML to better match project
    private boolean active; // changed from UML for style
    private boolean published; // changed from UML for style
    private boolean requireLocation; // added to align with storyboard
    private boolean acceptNewResults; // added to align with storyboard
    private ExperimentType type; // todo: do we need type here if an experiment has a type?
    private Collection<UUID> crowedExperimenter; // Experimenter id's


    public Experiment() {
        // Stub should do nothing of yet
        // someone will build experiment class
    }

    public Experiment(String description)
    {
        this.description = description;
    }

    // constructor for fragment that creates an experiment
    public Experiment(String description, int minTrials, boolean requireLocation, boolean acceptNewResults) {
        this.description = description;
        this.minTrials = minTrials;
        this.requireLocation = requireLocation;
        this.acceptNewResults = acceptNewResults;
        this.published = false;
        this.active = true;
    }

    public boolean compare(Experiment experiment) {
        return experimentId.equals(experiment.experimentId);
    }

    public abstract void stub();

}
