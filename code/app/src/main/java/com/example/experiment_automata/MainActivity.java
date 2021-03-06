package com.example.experiment_automata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements AddExperimentFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FloatingActionButton addExperimentButton = findViewById(R.id.add_experiment_button);
        addExperimentButton.setOnClickListener((v) -> {
            new AddExperimentFragment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
        });
    }

    @Override
    public void onOkPressed(Experiment experiment) {
        Log.d("OK_PRESSED", "experiment created!");
        
    }
}