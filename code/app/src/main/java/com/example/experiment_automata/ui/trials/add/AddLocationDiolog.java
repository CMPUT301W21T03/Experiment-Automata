package com.example.experiment_automata.ui.trials.add;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentMaker;
import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.example.experiment_automata.backend.users.User;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.ui.experiments.AddExperimentFragment;

import java.io.Serializable;

public class AddLocationDiolog extends DialogFragment
{

    public static final String PASSED_LOCATION = "LOCATION-RECV";

    private AddLocationDiolog.OnFragmentInteractionListener listener;

    /**
     * This is an interface for any activity using this fragment
     */
    public interface OnFragmentInteractionListener {

        void onOkPressed();
    }

    /**
     * This identifies the listener for the fragment when it attaches
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddLocationDiolog.OnFragmentInteractionListener) {
            listener = (AddLocationDiolog.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
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

            description.setText(currentExperiment.getDescription());
            requireLocation.setChecked(currentExperiment.isRequireLocation());
            acceptNewResults.setChecked(currentExperiment.isActive());
            minTrials.setText("" + currentExperiment.getMinTrials());

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

                            listener.onOKPressedEdit(experimentDescription, experimentTrials,
                                    experimentLocation, experimentNewResults, currentExperiment);
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
                            // todo: this should be reformatted like edit question where it passes back all the inputs and something else creates the question
                            int experimentTrials;
                            if (experimentTrialsString.isEmpty()) {
                                experimentTrials = 0;
                            } else {
                                experimentTrials = Integer.parseInt(experimentTrialsString);
                            }

                            ExperimentType experimentType = ExperimentType.valueOf(trialType.getSelectedItem().toString());
                            boolean experimentLocation = requireLocation.isChecked();
                            boolean experimentNewResults = acceptNewResults.isChecked();
                            // todo: determine if we need to do unit testing on this
                            User user = ((NavigationActivity) getActivity()).loggedUser;
                            listener.onOkPressed(new ExperimentMaker().makeExperiment(experimentType, experimentDescription,
                                    experimentTrials, experimentLocation, experimentNewResults, user.getUserId()));
                            // debug statements since no unit testing, prints out all info used to create the experiment object
                            Log.d("NEW_EXPERIMENT", experimentDescription);
                            Log.d("EXPERIMENT_TYPE", experimentType.toString());
                            Log.d("REMAINING INFO", "trials=" + experimentTrialsString + " location=" +
                                    experimentLocation + " accept new=" + experimentNewResults);
                        }
                    }).create();
        }
    }
}