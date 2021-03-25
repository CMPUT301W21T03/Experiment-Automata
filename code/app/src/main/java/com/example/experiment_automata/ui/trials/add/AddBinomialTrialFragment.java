package com.example.experiment_automata.ui.trials.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.experiments.BinomialExperiment;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.ui.qr.ScannerActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddBinomialTrialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBinomialTrialFragment extends Fragment {
    private ImageButton scanQRButton;
    public AddBinomialTrialFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_binomial_trial, container, false);
        TextView description = (TextView) root.findViewById(R.id.binomial_trial_experiment_description);
        NavigationActivity parentActivity = ((NavigationActivity) getActivity());
        BinomialExperiment experiment = (BinomialExperiment) parentActivity.experimentManager.getCurrentExperiment();
        description.setText(experiment.getDescription());

        scanQRButton = root.findViewById(R.id.binomial_trial_experiment_description_qr_button);
        scanQRButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScannerActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}