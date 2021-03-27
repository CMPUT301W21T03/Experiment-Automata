package com.example.experiment_automata.backend.users;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Role/Pattern:
 *     This is the user. Contains the information that represents a user.
 *
 *  Known Issue:
 *
 *      1. None
 */
public class User implements Serializable {
    private static String DEFAULT_UUID_STRING = "00000000-0000-0000-0000-000000000000";//move this to a constants class later
    private static final String TAG = "User";
    private UUID userId;//changed from int to UUID
    private ContactInformation info;
//    private SearchController controller;
    private Collection<UUID> ownedExperiments;
    private Collection<UUID> subscribedExperiments;
//    private Collection<Experiment> participatingExperiments;

    /**
     * Creates the user. Assigns a user id automatically.
     * @param preferences
     * the ContactInformation object containing the information for the user
     */
    public User(SharedPreferences preferences) {
        this.userId = UUID.fromString(preferences.getString("userId", UUID.randomUUID().toString()));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userId", userId.toString());
        editor.apply();
        this.info = new ContactInformation(preferences);
        this.ownedExperiments = new ArrayList<>();
        this.subscribedExperiments = new ArrayList<>();
        updateFirestore();
    }

    /**
     * Creates a user by querying the firestore.
     * @param id
     *  The id of the user to get the firestore document
     */
    private User(UUID id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(id.toString());
        Task<DocumentSnapshot> task = documentReference.get();
        // wait until the task is complete
        while (!task.isComplete());
        DocumentSnapshot document = task.getResult();
        String name = document.get("name").toString();
        String email = document.get("email").toString();
        String phone = document.get("phone").toString();

        this.userId = id;
        this.info = new ContactInformation(name, email, phone);
        this.ownedExperiments = new ArrayList<>();
        this.subscribedExperiments = new ArrayList<>();
    }

    /**
     * This returns a User instance with the id specified.
     * @param id
     *  The id of an existing user on the firestore
     * @return
     *  The user instance with the information queried from the firestore
     */
    public static User getInstance(UUID id) {
        return new User(id);
    }

    /**
     * Update the user information in the Firestore
     */
    protected void updateFirestore() {
        Map<String, String> userInfo = new HashMap<String, String>();
        userInfo.put("name", this.info.getName());
        userInfo.put("email", this.info.getEmail());
        userInfo.put("phone", this.info.getPhone());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(this.userId.toString())
                .set(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User info successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    /**
     * Get the user's id
     * @return
     *  The id
     */
    public UUID getUserId()
    {
        return this.userId;
    }

    /**
     * Get the user's information
     * @return
     * The user's contact information object
     */
    public ContactInformation getInfo() {
        return this.info;
    }

    /**
     * Get a collection of all the owned experiment IDs.
     * @return
     *  The IDs of owned experiments
     */
    public Collection<UUID> getOwnedExperiments() { return ownedExperiments; }

    /**
     * Get a collection of the experiment IDs the user is subscribed to.
     * @return
     *  The IDs of experiments
     */
    public Collection<UUID> getSubscriptions() { return subscribedExperiments; }

    /**
     * Add the experiment reference to the owned experiments
     * @param experimentId
     *  the UUID of the experiment
     */
    public void addExperiment(UUID experimentId) { ownedExperiments.add(experimentId); }

    /**
     * Adds/removes the experiment reference to the subscribed experiments
     * If already subscribed, unsubscribes
     * If not subscribed, subscribes
     * @param experimentId
     *  the UUID of the experiment
     */
    public void subscribeExperiment(UUID experimentId) {
        if (subscribedExperiments.contains(experimentId)) {
            subscribedExperiments.remove(experimentId);
        } else {
            subscribedExperiments.add(experimentId);
        }
    }
}
