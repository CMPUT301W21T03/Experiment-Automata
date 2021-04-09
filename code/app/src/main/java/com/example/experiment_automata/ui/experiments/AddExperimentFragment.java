package com.example.experiment_automata.ui.experiments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentMaker;
import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.users.User;

import org.jetbrains.annotations.NotNull;

/**
 * Role/Pattern:
 *       This class provides the framework needed to make and edit an experiment.
 */
// Basic layout of this fragment inspired by lab work in CMPUT 301
// Abdul Ali Bangash, "Lab 3", 2021-02-04, Public Domain,
// https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf
public class AddExperimentFragment extends DialogFragment {
    public static final String ADD_EXPERIMENT_CURRENT_VALUE = "ADD-FRAGMENT-CURRENT-EXPERIMENT";
    // note: locale not currently added as I am not sure what input it has for Experiment
    private EditText description;
    private EditText region;
    private EditText minTrials;
    private Spinner trialType;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch requireLocation;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch acceptNewResults;
    private OnFragmentInteractionListener listener;

    /**
     * This is an interface for any activity using this fragment
     */
    public interface OnFragmentInteractionListener {
        void onOkPressed(Experiment<?> newExperiment);
        void onOKPressedEdit(String experimentDescription, String experimentRegion,
                             int experimentTrials, boolean experimentLocation,
                             boolean experimentNewResults, Experiment<?> currentExperiment);
    }

    /**
     * This identifies the listener for the fragment when it attaches
     * @param context the android context
     */
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(String.format("%s must implement OnFragmentInteractionListener",
                    context.toString()));
        }
    }

    /**
     * This gives instructions for when creating this dialog and prepares it's dismissal
     * @param savedInstancesState
     *   allows you to pass information in if editing an experiment with existing info
     * @return
     *   the dialog that will be created
     */
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstancesState) {
        View view = LayoutInflater.from(requireActivity()).inflate(R.layout.fragment_add_experiment, null);

        // link all of the variables with their objects in the UI
        description = view.findViewById(R.id.create_experiment_description_editText);
        region = view.findViewById(R.id.create_experiment_region);
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
        Experiment<?> currentExperiment = (Experiment<?>) args.getSerializable(ADD_EXPERIMENT_CURRENT_VALUE);
            (view.findViewById(R.id.add_experiment_fragment_trial_type_prompt)).setVisibility(View.GONE);
            (view.findViewById(R.id.experiment_type_spinner)).setVisibility(View.GONE);
            (view.findViewById(R.id.experiment_require_location_switch)).setVisibility(View.GONE);

            description.setText(currentExperiment.getDescription());
            region.setText(currentExperiment.getRegion());
            requireLocation.setChecked(currentExperiment.isRequireLocation());
            acceptNewResults.setChecked(currentExperiment.isActive());
            minTrials.setText(currentExperiment.getMinTrials().toString());

            return build.setView(view).setTitle("Edit Experiment")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Ok", (dialog, which) -> {
                        String experimentDescription = description.getText().toString();
                        String experimentRegion = region.getText().toString();
                        // method of reading input as integer found on Stack Overflow from CommonsWare, Feb 4 2011
                        //https://stackoverflow.com/questions/4903515/how-do-i-return-an-int-from-edittext-android
                        String experimentTrialsString = minTrials.getText().toString();
                        boolean experimentLocation = requireLocation.isChecked();
                        boolean experimentNewResults = acceptNewResults.isChecked();
                        int experimentTrials;
                        if (experimentTrialsString.isEmpty()) {
                            experimentTrials = 0;
                        } else {
                            experimentTrials = Integer.parseInt(experimentTrialsString);
                        }
                        // Assuming you can't change the type of an experiment
                        listener.onOKPressedEdit(experimentDescription, experimentRegion,
                                experimentTrials, experimentLocation, experimentNewResults, currentExperiment);
                    }).create();
        } else {
            // build the dialog and give instructions for its dismissal
            return build.setView(view)
                    .setTitle("Add Experiment")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Ok", (dialog, which) -> {
                        String experimentDescription = description.getText().toString();
                        String experimentRegion = region.getText().toString();
                        // method of reading input as integer found on Stack Overflow from CommonsWare, Feb 4 2011
                        //https://stackoverflow.com/questions/4903515/how-do-i-return-an-int-from-edittext-android
                        String experimentTrialsString = minTrials.getText().toString();
                        int experimentTrials;
                        if (experimentTrialsString.isEmpty()) {
                            experimentTrials = 0;
                        } else {
                            experimentTrials = Integer.parseInt(experimentTrialsString);
                        }

                        ExperimentType experimentType = ExperimentType.valueOf(trialType.getSelectedItem().toString());
                        boolean experimentLocation = requireLocation.isChecked();
                        boolean experimentNewResults = acceptNewResults.isChecked();
                        User user = ((NavigationActivity) requireActivity()).loggedUser;
                        listener.onOkPressed(ExperimentMaker.makeExperiment(experimentType, experimentDescription,
                                experimentTrials, experimentLocation, experimentNewResults, user.getUserId(), experimentRegion, true));
                    }).create();
        }
    }
}
