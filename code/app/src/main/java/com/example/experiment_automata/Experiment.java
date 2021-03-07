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

    /**
     * Default experiment constructor that only asks for a description
     * @param description
     *   the description of the experiment
     */
    public Experiment(String description)
    {
        this.description = description;
    }

    /**
     * Experiment constructor to be used by ExperimentMaker when creating an experiment from the AddExperimentFragment
     * @param description
     *   the description of the experiment
     * @param minTrials
     *   the minimum number of trials for this experiment
     * @param requireLocation
     *   a boolean for whether or not the trials need a location
     * @param acceptNewResults
     *   a boolean for whether this trial should be accepting new requests or not
     */
    public Experiment(String description, int minTrials, boolean requireLocation, boolean acceptNewResults) {
        this.description = description;
        this.minTrials = minTrials;
        this.requireLocation = requireLocation;
        this.acceptNewResults = acceptNewResults;
        this.published = false;
        this.active = true;
    }

    /**
     * This method will check if an experiment has the same id as another
     * @param experiment
     *   The experiment you want to compare with
     * @return
     *   A boolean based on whether the ids are the same
     */
    public boolean compare(Experiment experiment) {
        return experimentId.equals(experiment.experimentId);
    }

    public abstract void stub();

    /**
     * Adds userID to the list so that each experiment knows which users can participate in them
     * @param userId UserID to be added to the experiment
     */
    public void addUserId(UUID userId){
        crowedExperimenter.add(userId);
    }

    /**
     * Removes userID from the list so that the user can no longer participate in it
     * @param userID UserID to be removed from the experiment
     */

    public void removeUserId(UUID userId){
        crowedExperimenter.remove(userID);
    }

}
