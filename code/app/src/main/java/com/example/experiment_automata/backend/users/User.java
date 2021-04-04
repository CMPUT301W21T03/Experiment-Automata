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
import java.util.List;
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
    private Collection<UUID> ownedExperiments;
    private Collection<UUID> subscribedExperiments;


    /**
     * a public constructor that lets us make a user without providing information
     * (Used for when making a user for firebase)
     */
    public User(ContactInformation ci, UUID userId)
    {
        this.info = ci;
        this.userId = userId;
    }


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
    }

    /**
     * Creates a user by querying the firestore.
     * @param id
     *  The id of the user to get the firestore document
     */
    private User(UUID id) {
        this.userId = id;
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
    public void addExperiment(UUID experimentId) {
        ownedExperiments.add(experimentId);
    }

    /**
     * Adds/removes the experiment reference to the subscribed experiments.
     * Also updates the firestore.
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

    /**
     * set the subscribed experiments
     * @param subs the new subs
     */
    public void setSubscribedExperiments(Collection<UUID> subs)
    {
        if(subs == null)
            return;
        this.subscribedExperiments.clear();
        this.subscribedExperiments.addAll(subs);

    }

    /**
     * sets the owned experiments
     * @param owned the new owned experiments
     */
    public void setOwnedExperiments(Collection<UUID> owned)
    {
        if(owned == null)
            return;
        this.ownedExperiments.clear();
        this.ownedExperiments.addAll(owned);
    }

    /**
     * the the contact information
     * @param info the new contact infromation
     */
    public void setContactInformation(ContactInformation info)
    {
        this.info = info;
    }
    /**
     * Update the user information in the Firestore
     */
    public void updateFirestore() {
        // convert collection of UUIDs to collection of Strings
        Collection<String> owned = new ArrayList<>();
        for (UUID experimentId : this.ownedExperiments) {
            owned.add(experimentId.toString());
        }
        Collection<String> subscriptions = new ArrayList<>();
        for (UUID experimentId : this.subscribedExperiments) {
            subscriptions.add(experimentId.toString());
        }
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", this.info.getName());
        userInfo.put("email", this.info.getEmail());
        userInfo.put("phone", this.info.getPhone());
        userInfo.put("owned", owned);
        userInfo.put("subscriptions", subscriptions);

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
     * Update the user information from the Firestore.
     */
    protected void updateFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(this.userId.toString());
        Task<DocumentSnapshot> task = documentReference.get();
        // wait until the task is complete
        while (!task.isComplete());
        DocumentSnapshot document = task.getResult();
        String name = (String) document.get("name");
        String email = (String) document.get("email");
        String phone = (String) document.get("phone");
        Collection<String> ownedExperiments = (List<String>) document.get("owned");
        if (ownedExperiments == null) ownedExperiments = new ArrayList<>();
        Collection<String> subscribedExperiments = (List<String>) document.get("subscriptions");
        if (subscribedExperiments == null) subscribedExperiments = new ArrayList<>();
        // Convert Collection of String to Collection of UUIDs
        this.ownedExperiments = new ArrayList<>();
        for (String experimentId : ownedExperiments) {
            this.ownedExperiments.add(UUID.fromString(experimentId));
        }
        this.subscribedExperiments = new ArrayList<>();
        for (String experimentId : subscribedExperiments) {
            this.subscribedExperiments.add(UUID.fromString(experimentId));
        }

        this.info = new ContactInformation(name, email, phone);
    }

    /**
     * Update the user experiments from the Firestore.
     */
    protected void updateExperimentFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(this.userId.toString());
        Task<DocumentSnapshot> task = documentReference.get();
        // wait until the task is complete
        while (!task.isComplete());
        DocumentSnapshot document = task.getResult();
        Collection<String> ownedExperiments = (List<String>) document.get("owned");
        if (ownedExperiments == null) ownedExperiments = new ArrayList<>();
        Collection<String> subscribedExperiments = (List<String>) document.get("subscriptions");
        if (subscribedExperiments == null) subscribedExperiments = new ArrayList<>();
        // Convert Collection of String to Collection of UUIDs
        this.ownedExperiments = new ArrayList<>();
        for (String experimentId : ownedExperiments) {
            this.ownedExperiments.add(UUID.fromString(experimentId));
        }
        this.subscribedExperiments = new ArrayList<>();
        for (String experimentId : subscribedExperiments) {
            this.subscribedExperiments.add(UUID.fromString(experimentId));
        }
    }

    /**
     * Update the user contact information from the Firestore.
     */
    protected void updateContactFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(this.userId.toString());
        Task<DocumentSnapshot> task = documentReference.get();
        // wait until the task is complete
        while (!task.isComplete());
        DocumentSnapshot document = task.getResult();
        String name = (String) document.get("name");
        String email = (String) document.get("email");
        String phone = (String) document.get("phone");
        this.info = new ContactInformation(name, email, phone);
    }
}
