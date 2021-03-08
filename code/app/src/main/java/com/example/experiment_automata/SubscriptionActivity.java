package com.example.experiment_automata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SubscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // References: Abdul Ali Bangash, "Lab 3 Instructions - Custom List",
        // 2021-02-04, Public Domain, https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        // Connects the subscription list to the listview found in the activity_subscription.xml
        ListView subscriptionList=(ListView) findViewById(R.id.subscription_list);

        // Creates an empty arraylist
        // TODO: Connect this arraylist with the arraylist from the user
        ArrayList<Experiment> subscriptionArrayList=new ArrayList<>();

        // Creates an array adapter with this array list
        ArrayAdapter<Experiment> subscriptionArrayAdapter=new SubscriptionAdapter(this, subscriptionArrayList);

        // Connects the list with this array adapter
        subscriptionList.setAdapter(subscriptionArrayAdapter);
    }


}