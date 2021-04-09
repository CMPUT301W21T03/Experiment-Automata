package com.example.experiment_automata.ui.trials;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.trials.Trial;
import com.example.experiment_automata.ui.experiments.NavExperimentDetailsFragment;

import java.util.ArrayList;

/**
 * Role/Pattern:
 *       This fragment class inflates the trial list fragment.
 *       Also contains the trail array adapter.
 *       This fragment contains a list of trials which can be ignored or included
 *       in the stats computation.
 *
 */
public class TrialsFragment extends Fragment {
    private ArrayList<Trial<?>> trialsArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_trials, container, false);
        ListView trialList = root.findViewById(R.id.trial_list);
        trialsArrayList = new ArrayList<>();
        TrialArrayAdapter trialArrayAdapter = new TrialArrayAdapter(requireActivity(), trialsArrayList, (NavExperimentDetailsFragment) getParentFragment());
        trialList.setAdapter(trialArrayAdapter);
        return root;
    }

    public void updateView() {
        Experiment<?> experiment = ((NavigationActivity) requireActivity()).experimentManager
                .getCurrentExperiment();
        trialsArrayList.clear();
        trialsArrayList.addAll(experiment.getTrials());
    }
}
