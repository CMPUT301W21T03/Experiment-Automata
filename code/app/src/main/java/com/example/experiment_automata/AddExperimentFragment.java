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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddExperimentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
// Basic layout of this fragment inspired by lab work in CMPUT 301
// Abdul Ali Bangash, "Lab 3", 2021-02-04, Public Domain,
// https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf
public class AddExperimentFragment extends DialogFragment {

    public static final String ADD_EXPERIMENT_CURRENT_VALUE = "ADD-FRAGMENT-CURRENT-EXPERIMENT";

    // note: locale not currently added as I am not sure what input it has for Experiment
    private EditText description;
    private EditText minTrials;
    private Spinner trialType;
    private Switch requireLocation;
    private Switch acceptNewResults;
    private OnFragmentInteractionListener listener;

    /**
     * This is an interface for any activity using this fragment
     */
    public interface OnFragmentInteractionListener {
        void onOkPressed(Experiment newExperiment);
    }

    /**
     * This identifies the listener for the fragment when it attaches
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This will create a new instance of this fragment with an experiment
     * @param experiment
     *   The experiment that will be edited
     * @return
     *   a fragment to edit an experiment's information
     */
    public static AddExperimentFragment newInstance(Experiment experiment) {
        AddExperimentFragment fragment = new AddExperimentFragment();
        Bundle args = new Bundle();
        args.putSerializable("EXPERIMENT", experiment);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This gives instructions for when creating this dialog and prepares it's dismissal
     * @param savedInstancesState
     *   allows you to pass information in if editing an experiment with existing info
     * @return
     *   the dialog that will be created
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstancesState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_experiment, null);

        // link all of the variables with their objects in the UI
        description = view.findViewById(R.id.create_experiment_description_editText);
        minTrials = view.findViewById(R.id.experiment_min_trials_editText);
        trialType = view.findViewById(R.id.experiment_type_spinner);
        requireLocation = view.findViewById(R.id.experiment_require_location_switch);
        acceptNewResults = view.findViewById(R.id.experiment_accept_new_results_switch);

        // preparing the spinner done from android documentation, Nov 18, 2020. Apache 2.0
        // https://developer.android.com/guide/topics/ui/controls/spinner#java
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(getContext(), R.array.experiment_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trialType.setAdapter(adapter);

        AlertDialog.Builder build = new AlertDialog.Builder(getContext());

        // prepare the UI elements if experiment has existing information
        Bundle args = getArguments();
        if (args != null) {
            Experiment currentExperiment = (Experiment) args.getSerializable(ADD_EXPERIMENT_CURRENT_VALUE);
            (view.findViewById(R.id.add_experiment_fragment_trial_type_prompt)).setVisibility(View.GONE);
            (view.findViewById(R.id.experiment_type_spinner)).setVisibility(View.GONE);

            return build.setView(view).setTitle("Edit Experiment")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            String experimentDescription = description.getText().toString();
                            // method of reading input as integer found on Stack Overflow from CommonsWare, Feb 4 2011
                            //https://stackoverflow.com/questions/4903515/how-do-i-return-an-int-from-edittext-android
                            String experimentTrialsString = minTrials.getText().toString();
                            boolean experimentLocation = requireLocation.isChecked();
                            boolean experimentNewResults = acceptNewResults.isChecked();
                            // todo: this logic should be relocated in the future
                            int experimentTrials;
                            if (experimentTrialsString.isEmpty()) {
                                experimentTrials = 0;
                            } else {
                                experimentTrials = Integer.parseInt(experimentTrialsString);
                            }
                            // Assuming you can't change the type of an experiment
                            currentExperiment.setDescription(experimentDescription);
                            currentExperiment.setMinTrials(experimentTrials);
                            currentExperiment.setRequireLocation(experimentLocation);
                            currentExperiment.setAcceptNewResults(experimentNewResults);
                            listener.onOkPressed(currentExperiment);
                        }
                    }).create();
        }
        else {
            // build the dialog and give instructions for its dismissal

            return build.setView(view)
                    .setTitle("Add Experiment")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String experimentDescription = description.getText().toString();
                            // method of reading input as integer found on Stack Overflow from CommonsWare, Feb 4 2011
                            //https://stackoverflow.com/questions/4903515/how-do-i-return-an-int-from-edittext-android
                            String experimentTrialsString = minTrials.getText().toString();
                            // todo: this logic should be relocated in the future
                            int experimentTrials;
                            if (experimentTrialsString.isEmpty()) {
                                experimentTrials = 0;
                            } else {
                                experimentTrials = Integer.parseInt(experimentTrialsString);
                            }

                            ExperimentType experimentType = ExperimentType.valueOf(trialType.getSelectedItem().toString());
                            boolean experimentLocation = requireLocation.isChecked();
                            boolean experimentNewResults = acceptNewResults.isChecked();
                            try {
                                // todo: determine if we need to do unit testing on this
                                User user = ((NavigationActivity) getActivity()).loggedUser;
                                listener.onOkPressed(new ExperimentMaker().makeExperiment(experimentType, experimentDescription,
                                        experimentTrials, experimentLocation, experimentNewResults, user.getUserId()));
                                // debug statements since no unit testing, prints out all info used to create the experiment object
                                Log.d("NEW_EXPERIMENT", experimentDescription);
                                Log.d("EXPERIMENT_TYPE", experimentType.toString());
                                Log.d("REMAINING INFO", "trials=" + experimentTrialsString + " location=" +
                                        experimentLocation + " accept new=" + experimentNewResults);
                            } catch (IllegalExperimentException e) {
                                e.printStackTrace();
                            }
                        }
                    }).create();
        }
    }
}