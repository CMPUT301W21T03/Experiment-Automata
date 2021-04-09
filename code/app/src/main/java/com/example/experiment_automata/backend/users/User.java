package com.example.experiment_automata.backend.users;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.experiment_automata.backend.DataBase;
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
 */
public class User implements Serializable {
    private static final String TAG = "User";
    private final UUID userId;
    private ContactInformation info;
    private Collection<UUID> ownedExperiments;
    private Collection<UUID> subscribedExperiments;
    private boolean testMode;

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
        this.testMode = false;
        updateExperimentFromFirestore();
        updateFirestore();
    }

    /**
     * Makes a user from some ContactInformation and usedId
     * @param ci the new contact information
     * @param userId the userId we want to assign
     */
    public User(ContactInformation ci, UUID userId) {
        this.info = ci;
        this.userId = userId;
        this.testMode = false;
    }

    /**
     * makes a user without all information and sets a test flag for
     * the class that tells us how we should handle the firebase stuff.
     * @param testMode the mode of the class
     */
    public User(boolean testMode, ContactInformation ci, UUID uid) {
        this.info = ci;
        this.userId = uid;
        this.testMode = testMode;
    }

    /**
     * Creates a user by querying the firestore.
     * @param id
     *  The id of the user to get the firestore document
     */
    private User(UUID id, boolean testMode) {
        this.userId = id;
        if (!testMode)
            updateContactFromFirestore();
    }

    /**
     * This returns a User instance with the id specified.
     * @param id
     *  The id of an existing user on the firestore
     * @return
     *  The user instance with the information queried from the firestore
     */
    public static User getInstance(UUID id, boolean testMode) {
        User newUser = new User(id, testMode);
        newUser.setTestMode(testMode);
        return newUser;
    }

    /**
     * Update the user information in the Firestore
     */
    public void updateFirestore() {
        if (testMode) return;
        // convert collection of UUIDs to collection of Strings
        Collection<String> owned = new ArrayList<>();
        if (this.ownedExperiments != null) {
            for (UUID experimentId : this.ownedExperiments) {
                owned.add(experimentId.toString());
            }
        }
        Collection<String> subscriptions = new ArrayList<>();
        if (this.subscribedExperiments != null) {
            for (UUID experimentId : this.subscribedExperiments) {
                subscriptions.add(experimentId.toString());
            }
        }
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", this.info.getName());
        userInfo.put("email", this.info.getEmail());
        userInfo.put("phone", this.info.getPhone());
        userInfo.put("owned", owned);
        userInfo.put("subscriptions", subscriptions);

        DataBase dataBase = DataBase.getInstance();
        FirebaseFirestore db = dataBase.getFireStore();
        db.collection("users").document(this.userId.toString())
                .set(userInfo);
    }

    /**
     * Update the user experiments from the Firestore.
     */
    protected void updateExperimentFromFirestore() {
        if (testMode) return;
        DataBase dataBase = DataBase.getInstance();
        FirebaseFirestore db = dataBase.getFireStore();
        this.ownedExperiments = new ArrayList<>();
        this.subscribedExperiments = new ArrayList<>();
        try {
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
            for (String experimentId : ownedExperiments) {
                this.ownedExperiments.add(UUID.fromString(experimentId));
            }
            for (String experimentId : subscribedExperiments) {
                this.subscribedExperiments.add(UUID.fromString(experimentId));
            }
        } catch (Exception ignored) {}
    }

    /**
     * Update the user contact information from the Firestore.
     */
    protected void updateContactFromFirestore() {
        if (testMode)
            return;

        DataBase dataBase = DataBase.getInstance();
        FirebaseFirestore db = dataBase.getFireStore();
        try {
            DocumentReference documentReference = db.collection("users").document(this.userId.toString());
            Task<DocumentSnapshot> task = documentReference.get();
            // wait until the task is complete
            while (!task.isComplete()) ;
            DocumentSnapshot document = task.getResult();
            String name = (String) document.get("name");
            String email = (String) document.get("email");
            String phone = (String) document.get("phone");
            this.info = new ContactInformation(name, email, phone);
        } catch (Exception ignored) {}
    }

    /**
     * Get the user's id
     * @return
     *  The id
     */
    public UUID getUserId() {
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
     * Add the experiment reference to the owned experiments.
     * If the owned experiments field equals nulls then
     * nothing happens.
     * @param experimentId
     *  the UUID of the experiment
     */
    public void addExperiment(UUID experimentId) {
        if (ownedExperiments != null)
            this.ownedExperiments.add(experimentId);
        if (!testMode)
            updateFirestore();
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

        if (!testMode)
            updateFirestore();
    }

    /**
     * set the subscribed experiments if the given
     * parameter is not null
     * @param subs the new subs
     */
    public void setSubscribedExperiments(Collection<UUID> subs) {
        if (subs == null)
            return;

        if (subscribedExperiments != null) {
            this.subscribedExperiments.clear();
            this.subscribedExperiments.addAll(subs);
        } else {
            this.subscribedExperiments = subs;
        }
    }

    /**
     * sets the owned experiments if the given parameter is
     * not null.
     * @param owned the new owned experiments
     */
    public void setOwnedExperiments(Collection<UUID> owned) {
        if (owned == null)
            return;

        if (ownedExperiments != null) {
            this.ownedExperiments.clear();
            this.ownedExperiments.addAll(owned);
        } else {
            this.ownedExperiments = owned;
        }
    }


    /**
     * sets the user class into our out of test mode
     * @param testMode the mode of the class
     */
    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }
}
