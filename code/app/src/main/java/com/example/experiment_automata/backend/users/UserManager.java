package com.example.experiment_automata.backend.users;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.experiment_automata.backend.experiments.ExperimentManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UserManager
{
    public static final String DB_USER_GET_ERR = "DB-USER-GET-ERR";
    private static HashMap<UUID, User> currentUsers;
    private static UserManager userManager;

    private UserManager()
    {
        currentUsers = new HashMap<>();
        userManager = new UserManager();
    }

    /**
     * gets the made instance of our hashmap
     * @return
     *  the current instance of the UserManager class
     */
    public UserManager getInstance()
    {
        if(userManager == null)
            userManager = new UserManager();

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
    public void getAllUsersFromFireBase()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userCollection = db.collection("users");
        userCollection.get().addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                for(QueryDocumentSnapshot userInformation : task.getResult())
                {
                    //UUID userId = userInformation.get("user_id");
                }
            }
            else
            {
                Log.d(DB_USER_GET_ERR, "Failed to get users");
            }
        });

    }

}
