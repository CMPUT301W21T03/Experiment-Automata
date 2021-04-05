package com.example.experiment_automata.backend;

import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class DataBase
{
    private static DataBase current;
    private static FirebaseFirestore db;

    private DataBase(boolean test_mode)
    {
        db = FirebaseFirestore.getInstance();
        if(test_mode)
        {
            db.disableNetwork();
            db.clearPersistence();
        }
    }

    public static DataBase getInstanceTesting()
    {
        if(current == null)
        {
            current = new DataBase(true);
            Log.d("called_test", "test should've been called");
        }
        return current;
    }

    public static DataBase getInstance()
    {

        if(current == null) {
            current = new DataBase(false);
            Log.d("called_test_END", "test should've been called");
        }

        return current;
    }

    public FirebaseFirestore getFireStore()
    {
        return db;
    }



}
