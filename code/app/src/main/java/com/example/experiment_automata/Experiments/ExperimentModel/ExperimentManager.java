package com.example.experiment_automata.Experiments.ExperimentModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Role/Pattern:
 *      Class made to maintain experiments that the users will make.
 *      This class is the main Model for keeping the experiments
 *      in their place while taking the work away from other classes.
 *
 *  Known Issue:
 *
 *      1. None
 */
public class ExperimentManager
{
    private static HashMap<UUID, Experiment> experiments;
    private Experiment currentExperiment;
    private String currentFragment;


    /**
     * Initializes the experiment manager.
     */
    public ExperimentManager()
    {
        experiments = new HashMap<UUID, Experiment>();
    }

    /**
     * Adds the given experiment that the user class/caller gives to this class
     * @param id
     *  Id corresponding to the experiment
     * @param experiment
     *  Experiment to add to the manager
     * @throws IllegalArgumentException
     *  The id is already associated to an experiment
     */
    public void add(UUID id, Experiment experiment) throws IllegalArgumentException {
        if(experiments.containsKey(id))
            throw new IllegalArgumentException();
        else {
            experiments.put(id, experiment);
        }
    }

    /**
     * Deletes a given experiment from the currently maintained list
     * @param id
     *  Experiment ID to delete
     */
    public void delete(UUID id) { experiments.remove(id); }

    /**
     * Gets the size of the currently maintained list
     * @return
     *  The size of the current list
     */
    public int getSize() { return experiments.size(); }

    /**
     * Get list of experiments owned by the specified user.
     * @param ownerId
     *  The user id to match the experiment's owner
     * @return
     *  A list of all the experiments owned by the user specified
     */
    public ArrayList<Experiment> getOwnedExperiments(UUID ownerId) {
        ArrayList<Experiment> experimentsList = new ArrayList<>();
        for (Map.Entry<UUID, Experiment> entry : experiments.entrySet()) {
            Experiment experiment = experiments.get(entry.getKey());
            if (experiment.getOwnerId().equals(ownerId)) {
                experimentsList.add(experiment);
            }
        }
        return experimentsList;
    }

    /**
     * Get list of experiments that match the experiment IDs given
     * @param experimentIds
     *  The collection of experiment IDs to query with
     * @return
     *  A list of all the experiments that matched the query
     */
    public ArrayList<Experiment> queryExperiments(Collection<UUID> experimentIds) {
        ArrayList<Experiment> experimentsList = new ArrayList<>();
        for (UUID id : experimentIds) {
            experimentsList.add(experiments.get(id));
        }
        return experimentsList;
    }

    /**
     * Get all published experiments
     * @return
     *  An arraylist of all published experiments
     */
    public ArrayList<Experiment> getPublishedExperiments() {
        ArrayList<Experiment> experimentsList = new ArrayList<>();
        for (Map.Entry<UUID, Experiment> entry : experiments.entrySet()) {
            Experiment experiment = experiments.get(entry.getKey());
            if (experiment.isPublished()) {
                experimentsList.add(experiment);
            }
        }
        return experimentsList;
    }

    /**
     * returns the experiments current UUID
     *
     * @param experimentUUID : the uuid of the experiment we want to get back
     * @return
     *  the experiment containing the given uuid
     */
    public Experiment getAtUUIDDescription(UUID experimentUUID)
    {
        return experiments.get(experimentUUID);
    }

    /**
     *  Gives back a list of experiments that match a search term given by the
     *  user.
     * @param query
     *  The query that is to be matched
     * @return
     *  A list of experiments that match the given query.
     */
    public ArrayList<Experiment> queryExperiments(String query) {
        ArrayList<Experiment> experimentsList = new ArrayList<>();
        for (Map.Entry<UUID, Experiment> entry : experiments.entrySet()) {
            Experiment experiment = experiments.get(entry.getKey());
            //Log.d("SEARCHING", "Experiment:\t" + experiment.getDescription());
            if (queryMatch(query, experiment.getDescription())) {
                //Log.d("QUERY", "Found Match");
                experimentsList.add(experiment);
            }
        }
        return experimentsList;
    }

    /**
     * Queries experiments from group that have a description that matches the query
     * @param query
     *  Description to match
     * @param experimentIds
     *  Group of experiments to search through
     * @return
     *  The experiments from the group that match the query
     */
    public ArrayList<Experiment> queryExperiments(String query, Collection<UUID> experimentIds)  {
        ArrayList<Experiment> experimentsList = new ArrayList<>();
        for (UUID id : experimentIds) {
            if (queryMatch(query, experiments.get(id).getDescription())) {
                experimentsList.add(experiments.get(id));
            }
        }
        return experimentsList;
    }

    /**
     * Get all owned experiments based on a query
     * @param query
     *   The description you are querying for
     * @param ownerId
     *   The owner of the experiment
     * @return
     *   All experiments that match the query and are had by that owner
     */
    public ArrayList<Experiment> queryOwnedExperiments(String query, UUID ownerId) {
        ArrayList<Experiment> experimentsList = new ArrayList<>();
        for (Map.Entry<UUID, Experiment> entry : experiments.entrySet()) {
            Experiment experiment = experiments.get(entry.getKey());
            if (experiment.getOwnerId().equals(ownerId) && queryMatch(query,
                    experiment.getDescription())) {
                experimentsList.add(experiment);
            }
        }
        return experimentsList;
    }

    /**
     * Get all published experiments based on a query
     * @param query
     *   The description you are querying for
     * @return
     *   All experiments that match the query and are published
     */
    public ArrayList<Experiment> queryPublishedExperiments(String query) {
        ArrayList<Experiment> experimentsList = new ArrayList<>();
        for (Map.Entry<UUID, Experiment> entry : experiments.entrySet()) {
            Experiment experiment = experiments.get(entry.getKey());
            if (experiment.isPublished() && queryMatch(query, experiment.getDescription())) {
                experimentsList.add(experiment);
            }
        }
        return experimentsList;
    }

    /**
     * Will query a specific experiment based on it's UUID
     * @param experimentId
     *  UUID of the experiment you want
     * @return
     *  The requested experiment
     */
    public Experiment query(UUID experimentId) {
        return experiments.get(experimentId);
    }

    /**
     * Function to help query specific experiments when searching based on description
     * @param query
     *  The query you are searching for
     * @param source
     *  The object you are comparing against
     * @return
     *  A boolean for if the source has a partial match with the query
     */
    private boolean queryMatch(String query, String source) {
        String[] queryTokens = query.toLowerCase().split("\\W");
        for (int j = 0; j < queryTokens.length; j++) {
            if (source.toLowerCase().indexOf(queryTokens[j]) >= 0) return true;
        }
        return false;
    }

    /**
     * Get the current experiment being held in memory by the manager
     * @return
     *  The current experiment
     */
    public Experiment getCurrentExperiment() { return currentExperiment; }

    /**
     * Set the current experiment being held in memory by the manager
     * @param experiment
     *  The experiment that you want to be set as the current experiment
     */
    public void setCurrentExperiment(Experiment experiment) { currentExperiment = experiment; }

    /**
     * Get all of the experiments that have been made regardless of the UUID
     * @return
     *  The list of all made experiments
     */
    public ArrayList<Experiment> getAllExperiments()
    {
        return new ArrayList(experiments.values());
    }

    /**
     * Get the current fragment in case in the search screen
     * @return
     *  A string of the name of the last non-search fragment
     */
    public String getCurrentFragment() {return this.currentFragment;}

    /**
     * Set the current fragment in case someone does many searches in a row
     * @param newFragment
     *  A string for the name of the current non-search fragment
     */
    public void setCurrentFragment(String newFragment) {this.currentFragment = newFragment;}
}
