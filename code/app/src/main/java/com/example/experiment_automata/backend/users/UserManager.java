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
    private static HashMap<UUID, User> currentUsers;
    private static UserManager userManager;

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


}
