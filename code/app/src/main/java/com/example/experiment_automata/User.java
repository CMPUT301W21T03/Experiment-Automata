package com.example.experiment_automata;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This is the user. Contains the information that represents a user.
 */
public class User {
    private static String DEFAULT_UUID_STRING = "00000000-0000-0000-0000-000000000000";//move this to a constants class later
    private static final String TAG = "User";
    private UUID userId;//changed from int to UUID
    private ContactInformation info;
//    private SearchController controller;
//    private Collection<Experiment> ownedExperiments;
//    private Collection<Experiment> participatingExperiments;

    /**
     * Creates the user. Assigns a user id automatically.
     * @param info
     * the ContactInformation object containing the information for the user
     */
    User(ContactInformation info) {
        userId = UUID.randomUUID();//generates a random UUID
        this.info = info;
        updateFirestore();
    }

    /**
     * Creates the stub user class
     */
    User() {
        userId = UUID.fromString(DEFAULT_UUID_STRING);//hard code UUID for stub to defaultUUIDString
        this.info = new ContactInformation("Individual",
                "example@ualberta.ca", "780-555-1234");
        updateFirestore();
    }

    /**
     * Update the user information in the Firestore
     */
    protected void updateFirestore() {
        Map<String, String> userInfo = new HashMap<String, String>();
        userInfo.put("name", info.getName());
        userInfo.put("email", info.getEmail());
        userInfo.put("phone", info.getPhone());

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
}
