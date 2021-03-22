package com.example.experiment_automata.ui.trials.add;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.experiment_automata.backend.experiments.NaturalCountExperiment;
import com.example.experiment_automata.NavigationActivity;
import com.example.experiment_automata.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNaturalCountTrialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNaturalCountTrialFragment extends Fragment {
    public AddNaturalCountTrialFragment() {
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
        View root = inflater.inflate(R.layout.fragment_add_natural_count_trial, container, false);
        TextView description = (TextView) root.findViewById(R.id.natural_count_trial_experiment_description);
        NavigationActivity parentActivity = ((NavigationActivity) getActivity());
        NaturalCountExperiment experiment = (NaturalCountExperiment) parentActivity.experimentManager.getCurrentExperiment();
        description.setText(experiment.getDescription());
        return root;
    }
}