package com.example.experiment_automata.Experiments.ExperimentModel;

import android.util.Log;

import com.example.experiment_automata.Experiments.ExperimentModel.Experiment;

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
     *  id corresponding to the experiment
     * @param experiment
     *  experiment to add to the manager
     * @throws IllegalArgumentException
     *  the id is already associated to an experiment
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
     *  experiment ID to delete
     */
    public void delete(UUID id) { experiments.remove(id); }

    /**
     * gets the size of the currently maintained list
     * @return
     *  the size of the current list
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
        ArrayList experimentsList = new ArrayList<Experiment>();
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
        ArrayList experimentsList = new ArrayList<Experiment>();
        for (UUID id : experimentIds) {
            experimentsList.add(experiments.get(id));
        }
        return experimentsList;
    }

    public ArrayList<Experiment> queryPublishedExperiments() {
        ArrayList experimentsList = new ArrayList<Experiment>();
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
     *  gives back a list of experiments that match a search term given by the
     *  user.
     * @param query
     * @return
     *  A list of experiments that match the given query.
     */
    public ArrayList<Experiment> queryExperiments(String query) {
        ArrayList experimentsList = new ArrayList<Experiment>();
        for (Map.Entry<UUID, Experiment> entry : experiments.entrySet()) {
            Experiment experiment = experiments.get(entry.getKey());
            Log.d("SEARCHING", "Experiment:\t" + experiment.getDescription());
            if (queryMatch(query, experiment.getDescription())) {
                Log.d("QUERY", "Found Match");
                experimentsList.add(experiment);
            }
        }
        return experimentsList;
    }

    private boolean queryMatch(String query, String source) {
        String[] queryTokens = query.toLowerCase().split("\\W");
        for (int j = 0; j < queryTokens.length; j++) {
            Log.d("SEARCHING", "query token:\t" + queryTokens[j]);
            if (source.toLowerCase().indexOf(queryTokens[j]) >= 0) return true;
        }
        return false;
    }
}
