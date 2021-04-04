package com.example.experiment_automata.backend.experiments;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.experiment_automata.backend.trials.BinomialTrial;
import com.example.experiment_automata.backend.trials.CountTrial;
import com.example.experiment_automata.backend.trials.MeasurementTrial;
import com.example.experiment_automata.backend.trials.NaturalCountTrial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    private static ExperimentManager experimentManager;
    private static HashMap<UUID, Experiment> experiments;
    public static int updateCalls = 0;
    private Experiment currentExperiment;


    /**
     * Initializes the experiment manager.
     */
    private ExperimentManager()
    {

        experiments = new HashMap<UUID, Experiment>();
        getAllFromFirestore();
    }

    public static ExperimentManager getInstance()
    {
        if(experimentManager == null)
            experimentManager = new ExperimentManager();
        return experimentManager;
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
        Collections.sort(experimentsList);
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
            if (experiments.containsKey(id)) {
                experimentsList.add(experiments.get(id));
            }
        }
        Collections.sort(experimentsList);
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
        Collections.sort(experimentsList);
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
            try {
                Experiment experiment = experiments.get(entry.getKey());
                //Log.d("SEARCHING", "Experiment:\t" + experiment.getDescription());
                if (queryMatch(query, experiment.getDescription())) {
                    //Log.d("QUERY", "Found Match");
                    experimentsList.add(experiment);
                }
            } catch (NullPointerException e) {}
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
            try {
                Experiment experiment = experiments.get(id);
                if (queryMatch(query, experiment.getDescription())) {
                    experimentsList.add(experiments.get(id));
                }
            } catch (NullPointerException e) {}
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
     * Populate experiments in Experiment manager with all experiments from Firestore
     */
    public void getAllFromFirestore(){
        updateCalls++;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference experimentCollection = db.collection("experiments");
        experimentCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ExperimentMaker maker = new ExperimentMaker();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Experiment currentExperiment = maker.makeExperiment(
                                ExperimentType.valueOf((String) document.get("type")),
                                (String) document.get("description"),
                                ((Long) document.get("min-trials")).intValue(),
                                (boolean) document.get("location-required"),
                                (boolean) document.get("accepting-new-results"),
                                UUID.fromString((String) document.get("owner")),
                                (boolean) document.get("published"),
                                UUID.fromString(document.getId())
                        );
                        UUID currentDocId = UUID.fromString(document.getId());
                        if ( document.get("results") != null){
                            buildTrials(currentExperiment, document.get("results"));
                        }


                        experiments.put(currentDocId,currentExperiment);
                        Log.d("FIRESTORE",(String) document.get("description"));
                    }
                }
                else{
                    //not able to query all from firestore
                    Log.d("FIRESTORE","Unable to pull experiments from firestore");
                }
            }
        });
    }
    /**
     * Populates given experiment with trials found in trials from firestore
     * @param experiment
     *  Experiment you want to populate
     * @param trialsObj
     *  Hashmap containing other hashmaps as found in the results field in the given Experiment document
     */
    public void buildTrials(Experiment experiment, Object trialsObj){
        HashMap<String,Object> trials = ( HashMap<String,Object> )trialsObj;
        for (String k : trials.keySet()){
            HashMap<String,Object> currentTrialMap = (HashMap<String,Object>) trials.get(k);
            UUID ownerId = UUID.fromString((String)currentTrialMap.get("owner-id"));
            //Location location = ; NEED TO WRITE LOCATION HANDLING
            switch(experiment.getType()){
                case Binomial:
                    boolean binResult = (boolean)currentTrialMap.get("result");
                    BinomialTrial binTrial;
                    if(experiment.isRequireLocation()){
                        binTrial = new BinomialTrial(ownerId,locationFromTrialHash(currentTrialMap),binResult);
                    }
                    else{
                        binTrial = new BinomialTrial(ownerId,binResult);
                    }
                    experiment.recordTrial(binTrial, true);
                    break;
                case Count:
                    CountTrial countTrial;

                    if(experiment.isRequireLocation()){
                        countTrial = new CountTrial(ownerId,locationFromTrialHash(currentTrialMap));
                    }
                    else{
                        countTrial = new CountTrial(ownerId);
                    }
                    experiment.recordTrial(countTrial, true);
                    break;
                case NaturalCount:
                    int natResult = (int)((long)currentTrialMap.get("result"));
                    NaturalCountTrial natTrial;

                    if(experiment.isRequireLocation()){
                        natTrial = new NaturalCountTrial(ownerId,locationFromTrialHash(currentTrialMap),natResult);
                    }
                    else{
                        natTrial = new NaturalCountTrial(ownerId,natResult);
                    }
                    experiment.recordTrial(natTrial, true);
                    break;
                case Measurement:
                    float measResult = (float)((double)currentTrialMap.get("result"));
                    MeasurementTrial mesTrial;
                    if(experiment.isRequireLocation()){
                        mesTrial = new MeasurementTrial(ownerId,locationFromTrialHash(currentTrialMap),measResult);
                    }
                    else{
                        mesTrial = new MeasurementTrial(ownerId,measResult);
                    }
                    experiment.recordTrial(mesTrial, true);
                    break;
                default:
                    //do nothing!
            }
        }
    }
    /**
     * Build a Location form trial hashmap taken from firestore
     * @param trialHash
     *  HashMap containing a specific Trial as represented in Firestore
     * @return
     *  The location in the given Trial
     */
    public Location locationFromTrialHash(HashMap<String,Object> trialHash){
        //ENSURE THAT THE GIVEN TRIAL HAS LAT/LON
        double latitude = (double) trialHash.get("latitude");
        double longitude = (double) trialHash.get("longitude");
        Location location = new Location("bad");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return  location;
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

}
