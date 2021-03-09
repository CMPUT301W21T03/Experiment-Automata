package com.example.experiment_automata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements AddExperimentFragment.OnFragmentInteractionListener{


    private ExperimentManager experimentManager;
    private ExperimentListAdapter experimentListAdapter;
    private RecyclerView experimentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        experimentManager = ExperimentManager.getExperimentManager();
        experimentListAdapter = new ExperimentListAdapter(experimentManager);

        experimentList = findViewById(R.id.experiment_list_recycler_view);
        experimentList.setLayoutManager(new LinearLayoutManager((MainActivity)this));
        experimentList.setAdapter(experimentListAdapter);


        final FloatingActionButton addExperimentButton = findViewById(R.id.add_experiment_button);
        addExperimentButton.setOnClickListener((v) -> {
            new AddExperimentFragment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
        });
    }

    @Override
    public void onOkPressed(Experiment experiment) {
        Log.d("OK_PRESSED", "experiment created!");
        int originalSize = experimentManager.getSize();
        experimentManager.add(experiment);
        experimentListAdapter.notifyItemChanged(originalSize, experimentManager.getSize());
    }
}