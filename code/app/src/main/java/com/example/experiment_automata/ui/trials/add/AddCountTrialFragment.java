package com.example.experiment_automata.ui.trials.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.experiments.CountExperiment;
import com.example.experiment_automata.backend.trials.CountTrial;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.ui.trials.MapDisplay.MapUtility;

import org.osmdroid.views.MapView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCountTrialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCountTrialFragment extends Fragment {

    private MapView currentMapDisplay;
    private MapUtility utility;


    public AddCountTrialFragment() {
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
        View root = inflater.inflate(R.layout.fragment_add_count_trial, container, false);
        TextView description = (TextView) root.findViewById(R.id.count_trial_experiment_description);
        NavigationActivity parentActivity = ((NavigationActivity) getActivity());
        CountExperiment currentExperiment = (CountExperiment) parentActivity.experimentManager.getCurrentExperiment();
        description.setText(currentExperiment.getDescription());
        currentMapDisplay = root.findViewById(R.id.count_trial_map_view);
        CountTrial trial = new CountTrial(parentActivity.loggedUser.getUserId());
        utility = new MapUtility(currentExperiment, currentMapDisplay, getContext(), parentActivity, trial);
        utility.mapSupport();


        return root;
    }

}