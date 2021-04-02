package com.example.experiment_automata.ui.trials;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.experiment_automata.ui.LinkView;
import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.trials.BinomialTrial;
import com.example.experiment_automata.backend.trials.CountTrial;
import com.example.experiment_automata.backend.trials.MeasurementTrial;
import com.example.experiment_automata.backend.trials.NaturalCountTrial;
import com.example.experiment_automata.backend.trials.Trial;
import com.example.experiment_automata.backend.users.User;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.ui.experiments.NavExperimentDetailsFragment;
import com.example.experiment_automata.ui.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

/**
 * Role/Pattern:
 *
 * This maintains the list for the android system to display the individual inflated views.
 *
 * Known Issue:
 *
 *      1. None
 */
public class TrialArrayAdapter extends ArrayAdapter<Trial> {
    // Syntax inspired by Abdul Ali Bangash, "Lab 3 Instructions - Custom List",
    // 2021-02-04, Public Domain, https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf

    private ArrayList<Trial> trials;
    private Context context;
    private NavExperimentDetailsFragment parentFragment;

    /**
     * Constructor takes in an array list of trials and a context to set the attributes properly
     * @param context
     *  Context of the array adapter
     * @param trialList
     *  the list of the trials to display
     */
    public TrialArrayAdapter(Context context, ArrayList<Trial> trialList, NavExperimentDetailsFragment parentFragment){
        super(context, 0, trialList);
        this.trials = trialList;
        this.context = context;
        this.parentFragment = parentFragment;
    }

    /**
     * This method sets the contents of the experiment to match up with the correct XML file
     * @param position Index of the experiment
     * @param convertView The XML we are trying to connect it with
     * @param parent Parent view of the current file
     * @return View with all the attributes set properly
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Syntax below taken from Abdul Ali Bangash, "Lab 3 Instructions - Custom List",
        //  2021-02-04, Public Domain, https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.trial_layout, parent, false);
        }
        // The experiment we're going to set the XML file with
        Trial trial = trials.get(position);

        LinkView experimenterNameView = (LinkView) view.findViewById(R.id.trial_experimenter_name);
        UUID userId = trial.getUserId();
        experimenterNameView.setText(User.getInstance(userId).getInfo().getName());
        experimenterNameView.setOnClickListener(v -> {
            NavigationActivity parentActivity = (NavigationActivity) context;
            Bundle args = new Bundle();
            args.putSerializable(ProfileFragment.userKey, User.getInstance(userId));
            NavController navController = Navigation.findNavController(parentActivity, R.id.nav_host_fragment);
            navController.navigate(R.id.nav_profile, args);
        });

        TextView trialValueView = view.findViewById(R.id.trial_value);
        if (trial instanceof CountTrial) {
            trialValueView.setText("");
        } else if (trial instanceof NaturalCountTrial) {
            trialValueView.setText(String.format(Locale.CANADA, "%d",
                    ((NaturalCountTrial) trial).getResult()));
        } else if (trial instanceof BinomialTrial) {
            trialValueView.setText(((BinomialTrial) trial).getResult() ? "Passed" : "Failed");
        } else if (trial instanceof MeasurementTrial) {
            trialValueView.setText(String.format(Locale.CANADA, "%.4f",
                    ((MeasurementTrial) trial).getResult()));
        }

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.trial_ignore_checkbox);
        checkBox.setChecked(!trial.isIgnored());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ignore = !((CheckBox) v).isChecked();
                trial.setIgnore(ignore);
                parentFragment.updateScreen();
            }
        });

        return view;
    }
}
