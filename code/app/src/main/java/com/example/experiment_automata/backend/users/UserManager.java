package com.example.experiment_automata.backend.users;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserManager
{
    public static final String DB_USER_GET_ERR = "DB-USER-GET-ERR";
    private static HashMap<UUID, User> currentUsers;
    private static UserManager userManager;
    private User currentUser;
    private static final String TAG = "User";

    private UserManager()
    {
        currentUsers = new HashMap<>();
    }

    /**
     * gets the made instance of our hashmap
     * @return
     *  the current instance of the UserManager class
     */
    public static UserManager getInstance()
    {
        if(userManager == null) {
            userManager = new UserManager();
            userManager.getAllUsersFromFireStore();
        }

        Log.d("STEP_SIZE", userManager.getSize() + "");
        return userManager;
    }


    /**
     * Adds user to current local memory
     * @param newUser user to be added
     */
    public void add(User newUser)
    {
        if(!currentUsers.containsKey(newUser.getUserId()))
            currentUsers.put(newUser.getUserId(), newUser);
    }

    /**
     * the number of the users that we have locally
     * @return
     *  the number of locally stored users
     */
    public int getSize() {
        return currentUsers.size();
    }

    /**
     *  gets a user based on UUID
     * @param userId the UUID of the user we want
     * @return
     *  the user we want
     */
    public User getSpecificUser(UUID userId)
    {
        User current = null;
        if(currentUsers.containsKey(userId))
            current = currentUsers.get(userId);

        return current;
    }

    /**
     * gets all the user from firebase and stores them locally
     */
    public void getAllUsersFromFireStore()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userCollection = db.collection("users");
        userCollection.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    if (documentSnapshot != null) {
                        UUID userId = UUID.fromString(documentSnapshot.getId());
                        Log.d("BUILT", "" + userId);

                        if (!currentUsers.containsKey(userId)) {
                            // We don't have the user so we need to make them

                            Map<String, Object> userInformation = documentSnapshot.getData();
                            String name = (String) userInformation.get("name");
                            String email = (String) userInformation.get("email");
                            String phone = (String) userInformation.get("phone");
                            Collection<String> owned = (List<String>) userInformation.get("owned");
                            Collection<String> subscriptions = (List<String>) userInformation.get("subscriptions");
                            ContactInformation ci = new ContactInformation(name, email, phone);
                            User newUser = new User(ci, userId);
                            ArrayList<UUID> valsO = new ArrayList<>();
                            ArrayList<UUID>valsS = new ArrayList<>();
                            try {
                                for(String o : owned)
                                {
                                    valsO.add(UUID.fromString(o));
                                }
                                for(String s : subscriptions)
                                {
                                    valsS.add(UUID.fromString(s));
                                }
                                newUser.setOwnedExperiments(valsO);
                                newUser.setSubscribedExperiments(valsS);
                            }catch (Exception e)
                            {
                                // Something
                                // Data corruption within the db causes this
                            }
                            currentUsers.put(userId, newUser);
                        }
                    }
                }
            }
        });
    }

    public void setCurrentUser(User current)
    {
        this.currentUser = current;
        addCurrentUser();
    }


    /**
     * Update the user information in the Firestore
     */
    public void addCurrentUser()
    {
        Collection<UUID> ownedExperiments = currentUser.getOwnedExperiments();
        Collection<UUID> subscribedExperiments = currentUser.getSubscriptions();
        ContactInformation info = currentUser.getInfo();
        UUID userId = currentUser.getUserId();
        // convert collection of UUIDs to collection of Strings
        Collection<String> owned = new ArrayList<>();
        for (UUID experimentId : ownedExperiments) {
            owned.add(experimentId.toString());
        }
        Collection<String> subscriptions = new ArrayList<>();
        for (UUID experimentId : subscribedExperiments) {
            subscriptions.add(experimentId.toString());
        }
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", info.getName());
        userInfo.put("email", info.getEmail());
        userInfo.put("phone", info.getPhone());
        userInfo.put("owned", owned);
        userInfo.put("subscriptions", subscriptions);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId.toString())
                .set(userInfo)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User info successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    /**
     * Update the user experiments from the Firestore.
     */
    public void updateExperimentFromFirestore() {

        Collection<UUID> ownedExperimentsCurrent;
        Collection<UUID> subscribedExperimentsCurrent;
        UUID userId = currentUser.getUserId();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(userId.toString());
        Task<DocumentSnapshot> task = documentReference.get();
        // wait until the task is complete
        while (!task.isComplete());
        DocumentSnapshot document = task.getResult();
        Collection<String> ownedExperiments = (List<String>) document.get("owned");
        if (ownedExperiments == null) ownedExperiments = new ArrayList<>();
        Collection<String> subscribedExperiments = (List<String>) document.get("subscriptions");
        if (subscribedExperiments == null) subscribedExperiments = new ArrayList<>();
        // Convert Collection of String to Collection of UUIDs
        ownedExperimentsCurrent = new ArrayList<>();
        for (String experimentId : ownedExperiments) {
            ownedExperimentsCurrent.add(UUID.fromString(experimentId));
        }
        subscribedExperimentsCurrent = new ArrayList<>();
        for (String experimentId : subscribedExperiments) {
            subscribedExperimentsCurrent.add(UUID.fromString(experimentId));
        }
        currentUser.setOwnedExperiments(ownedExperimentsCurrent);
        currentUser.setSubscribedExperiments(subscribedExperimentsCurrent);
    }

    /**
     * Update the user contact information from the Firestore.
     */
    public void updateContactFromFirestore() {
        ContactInformation infoCurrent;
        UUID userId = currentUser.getUserId();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(userId.toString());
        Task<DocumentSnapshot> task = documentReference.get();
        // wait until the task is complete
        while (!task.isComplete());
        DocumentSnapshot document = task.getResult();
        String name = (String) document.get("name");
        String email = (String) document.get("email");
        String phone = (String) document.get("phone");
        infoCurrent = new ContactInformation(name, email, phone);
        currentUser.setContactInformation(infoCurrent);
    }

}
