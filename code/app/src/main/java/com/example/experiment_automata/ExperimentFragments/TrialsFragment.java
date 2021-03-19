package com.example.experiment_automata.ExperimentFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.experiment_automata.R;
import com.example.experiment_automata.trials.Trial;

import java.util.ArrayList;

public class TrialsFragment extends Fragment {
    private ArrayList<Trial> trialsArrayList;
    private TrialArrayAdapter trialArrayAdapter;

    public TrialsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_trials, container, false);
        ListView trailList = (ListView) root.findViewById(R.id.trial_list);
        trialsArrayList = new ArrayList<>();
        trialArrayAdapter = new TrialArrayAdapter(getActivity(), trialsArrayList);
        return root;
    }
}