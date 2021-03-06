package com.example.experiment_automata;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddExperimentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddExperimentFragment extends DialogFragment {

    // note: locale not currently added as I am not sure what input it has for Experiment
    private EditText description;
    private EditText minTrials;
    private Spinner trialType;
    private Switch requireLocation;
    private Switch acceptNewResults;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Experiment newExperiment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public static AddExperimentFragment newInstance(Experiment experiment) {
        AddExperimentFragment fragment = new AddExperimentFragment();
        Bundle args = new Bundle();
        args.putSerializable("EXPERIMENT", experiment);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstancesState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_experiment, null);

        description = view.findViewById(R.id.create_experiment_description_editText);
        minTrials = view.findViewById(R.id.experiment_min_trials_editText);
        trialType = view.findViewById(R.id.experiment_type_spinner);
        requireLocation = view.findViewById(R.id.experiment_require_location_switch);
        acceptNewResults = view.findViewById(R.id.experiment_accept_new_results_switch);

        Bundle args = getArguments();
        if (args != null) {
            Experiment currentExperiment = (Experiment) args.getSerializable("EXPERIMENT");
            // todo: update the UI elements if editing an experiment
        }

        AlertDialog.Builder build = new AlertDialog.Builder(getContext());
        return build.setView(view)
                .setTitle("Add Experiment")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String experimentDescription = description.getText().toString();
                        //https://stackoverflow.com/questions/4903515/how-do-i-return-an-int-from-edittext-android
                        int experimentTrials = Integer.parseInt(minTrials.getText().toString());
                        //ExperimentType experimentType

                    }
                }).create();
    }
}