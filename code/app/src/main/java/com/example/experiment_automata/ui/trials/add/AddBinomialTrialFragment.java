package com.example.experiment_automata.ui.trials.add;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.experiment_automata.backend.experiments.BinomialExperiment;
import com.example.experiment_automata.backend.trials.BinomialTrial;
import com.example.experiment_automata.backend.trials.NaturalCountTrial;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.R;
import com.example.experiment_automata.ui.trials.MapDisplay.MapUtility;

import org.osmdroid.views.MapView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddBinomialTrialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBinomialTrialFragment extends Fragment {

    private MapView currentMapDisplay;
    private MapUtility utility;


    public AddBinomialTrialFragment() {
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
        View root = inflater.inflate(R.layout.fragment_add_binomial_trial, container, false);
        TextView description = (TextView) root.findViewById(R.id.binomial_trial_experiment_description);
        NavigationActivity parentActivity = ((NavigationActivity) getActivity());
        BinomialExperiment experiment = (BinomialExperiment) parentActivity.experimentManager.getCurrentExperiment();
        description.setText(experiment.getDescription());
        currentMapDisplay = root.findViewById(R.id.add_binomial_trial_map_view);

        // get value
        CheckBox passedInput = root.findViewById(R.id.add_binomial_value);
        final boolean passed = passedInput.isChecked();
        BinomialTrial binomialTrial = new BinomialTrial(parentActivity.loggedUser.getUserId(), passed);

        utility = new MapUtility(experiment, currentMapDisplay, getContext(), parentActivity, binomialTrial);
        utility.mapSupport();

        return root;
    }
}