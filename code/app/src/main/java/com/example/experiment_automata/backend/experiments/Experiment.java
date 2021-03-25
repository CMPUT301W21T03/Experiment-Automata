package com.example.experiment_automata.backend.experiments;

import com.example.experiment_automata.backend.questions.Question;
import com.example.experiment_automata.backend.trials.Trial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;


/**
 * Role/Pattern:
 *       Base experiment class: the building blocks of all experiment types
 *
 * Known Issue:
 *
 *      1. None
 */
public abstract class Experiment implements Serializable, StatSummary, Graphable {
    private String description;
    private int minTrials;
    private UUID experimentId; // changed from UML to better match project
    private UUID ownerId; // // changed from UML to better match project
    protected boolean active; // changed from UML for style
    private boolean published; // changed from UML for style
    private boolean requireLocation; // added to align with storyboard
    private ExperimentType type; // todo: do we need type here if an experiment has a type? (yes makes it easy)
    private ArrayList<Question> questions;

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
    public Experiment(String description, int minTrials, boolean requireLocation, boolean acceptNewResults, UUID ownerId, ExperimentType type) {
        this.description = description;
        this.minTrials = minTrials;
        this.requireLocation = requireLocation;
        this.published = false;
        this.active = acceptNewResults;
        this.ownerId = ownerId;
        this.experimentId = UUID.randomUUID();
        this.type = type;
        this.questions = new ArrayList<>();
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

    /**
     * Gets the description of an experiment
     * @return String with the description of an experiment
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the owner of an experiment
     * @return UUID with the owner of an experiment
     */
    public UUID getOwnerId() {
        return ownerId;
    }

    /**
     * Gets the id of the experiment
     * @return UUID of the experiment
     */
    public UUID getExperimentId() {
        return experimentId;
    }

    /**
     * Activity status of experiment
     * @return Boolean with whether the experiment is active or not
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Published status of the experiment
     * @return Boolean whether the experiment is published or not
     */
    public boolean isPublished() { return published; }

    /**
     * Set published status of the experiment
     * @param p
     *  Boolean whether the experiment is published or not
     */
    public void setPublished(boolean p) { published = p; }

    /**
     * set the description of experiment
     * @param description value to be set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * sets the min trials for the experiment
     * @param minTrials value to be set
     */
    public void setMinTrials(int minTrials) {
        this.minTrials = minTrials;
    }

    /**
     * sets the activity for each of the experiments
     * @param active value to be set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * sets if we need a location
     * @param requireLocation value to be set
     */
    public void setRequireLocation(boolean requireLocation) {
        this.requireLocation = requireLocation;
    }

    /**
     * get the current experiments set type
     * @return
     *  current experiments type
     */
    public ExperimentType getType()
    {
        return type;
    }

    /**
     * get the min trials
     * @return the int min trial value
     */
    public int getMinTrials() {
        return minTrials;
    }

    /**
     * check if a location is required by the experiment
     * @return the result
     */
    public boolean isRequireLocation() {
        return requireLocation;
    }

    /**
     * Makes a new uuid if the one generated is in use by the system.
     */
    public void makeNewUUID()
    {
        this.experimentId = UUID.randomUUID();
    }

    /**
     * Gets the size of the experiment
     * @return size of the experiment
     */
    public abstract Integer getSize();

    /**
     * Add a trial to an experiment
     * @param trial the trial we want to add
     */
    public abstract void recordTrial(Trial trial);

    /**
     * gets all the recorded trials for an experiment
     * @return the recorded trials
     */
    public abstract ArrayList<Trial> getRecordedTrials();
}
