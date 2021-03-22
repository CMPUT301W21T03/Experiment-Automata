package com.example.experiment_automata.ui.trials.add;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.experiment_automata.backend.experiments.MeasurementExperiment;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMeasurementTrialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMeasurementTrialFragment extends Fragment {
    public AddMeasurementTrialFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_measurement_trial, container, false);
        TextView description = (TextView) root.findViewById(R.id.measurement_trial_experiment_description);
        NavigationActivity parentActivity = ((NavigationActivity) getActivity());
        MeasurementExperiment experiment = (MeasurementExperiment) parentActivity.experimentManager.getCurrentExperiment();
        description.setText(experiment.getDescription());
        return root;
    }
}