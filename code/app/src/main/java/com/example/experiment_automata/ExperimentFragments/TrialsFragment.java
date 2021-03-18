package com.example.experiment_automata.ExperimentFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.experiment_automata.Experiments.ExperimentModel.BinomialExperiment;
import com.example.experiment_automata.Experiments.ExperimentModel.CountExperiment;
import com.example.experiment_automata.Experiments.ExperimentModel.Experiment;
import com.example.experiment_automata.Experiments.ExperimentModel.MeasurementExperiment;
import com.example.experiment_automata.Experiments.ExperimentModel.NaturalCountExperiment;
import com.example.experiment_automata.NavigationActivity;
import com.example.experiment_automata.R;
import com.example.experiment_automata.trials.BinomialTrial;
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
        ListView trialList = (ListView) root.findViewById(R.id.trial_list);
        trialsArrayList = new ArrayList<>();
        trialArrayAdapter = new TrialArrayAdapter(getActivity(), trialsArrayList);
        trialList.setAdapter(trialArrayAdapter);
        return root;
    }

    public void updateView() {
        Experiment e = ((NavigationActivity) getActivity()).experimentManager
                .getCurrentExperiment();
        trialsArrayList.clear();
        switch (e.getType()) {
            case Count:
                CountExperiment countExperiment = (CountExperiment) e;
                trialsArrayList.addAll(countExperiment.getTrials());
                System.out.println(countExperiment.getTrials().size());
                break;
            case NaturalCount:
                NaturalCountExperiment naturalCountExperiment = (NaturalCountExperiment) e;
                trialsArrayList.addAll(naturalCountExperiment.getTrials());
                break;
            case Binomial:
                BinomialExperiment binomialExperiment = (BinomialExperiment) e;
                trialsArrayList.addAll(binomialExperiment.getTrials());
                break;
            case Measurement:
                MeasurementExperiment measurementExperiment = (MeasurementExperiment) e;
                trialsArrayList.addAll(measurementExperiment.getTrials());
                break;
        }
    }
}