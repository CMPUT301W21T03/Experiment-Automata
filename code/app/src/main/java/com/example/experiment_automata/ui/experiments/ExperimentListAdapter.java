package com.example.experiment_automata.ui.experiments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import com.example.experiment_automata.backend.events.UpdateEvent;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.users.ContactInformation;
import com.example.experiment_automata.backend.users.User;
import com.example.experiment_automata.backend.users.UserManager;
import com.example.experiment_automata.ui.LinkView;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.ui.profile.ProfileFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Role/Pattern:
 *
 *      This class connects the experiment_layout XML file with the fragment_home.xml file
 *
 * Known Issue:
 *
 *      1. None
 */
public class ExperimentListAdapter extends ArrayAdapter<Experiment<?>> {
    // Syntax inspired by Abdul Ali Bangash, "Lab 3 Instructions - Custom List",
    // 2021-02-04, Public Domain, https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf

    private ArrayList<Experiment<?>> experiment;
    private Context context;
    private String mode;
    private UserManager manager;
    private LinkView owner;

    /**
     * Constructor takes in an array list of experiments and a context to set the attributes properly
     * @param context
     *   the context needed to create experiment_layout if needed
     * @param experiments
     *   all of the experiments to be shown
     * @param mode
     *   the mode to determine what should be shown on each item
     */
    public ExperimentListAdapter(Context context, ArrayList<Experiment<?>> experiments, String mode, UserManager manager){
        super(context, 0, experiments);
        this.experiment = experiments;
        this.context=context;
        this.mode = mode;
        this.manager = manager;
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
            view = LayoutInflater.from(context).inflate(R.layout.experiment_layout, parent, false);
        }
        // The experiment we're going to set the XML file with
        Experiment<?> exp = experiment.get(position);

        // Find the corresponding views from experiment_layout
        TextView name = view.findViewById(R.id.experimentName);
        owner = view.findViewById(R.id.experimentOwner);
        TextView active = view.findViewById(R.id.experimentActivity);
        TextView experimentID = view.findViewById(R.id.experiment__id);
        CheckBox publishedCheckbox = view.findViewById(R.id.publishedCheckbox);

        // Set the name of the experiment accordingly
        UUID oid = exp.getOwnerId();
        name.setText(exp.getDescription());

        // Set the name of the experiment owner

        manager.setUpdateListener(() -> {
            Log.wtf("POSITION", position + "");
            Experiment<?> currentExperiment = experiment.get(position);
            UUID userId = currentExperiment.getOwnerId();
            String username = manager.getSpecificUser(userId).getInfo().getName();
            Log.wtf("Update listener exp", exp.toString());
            Log.wtf("Update listener oid", userId.toString());
            Log.wtf("Update listener username", username);
            updateUsername(username);
        });
        User user = manager.getSpecificUser(oid);
        if(user == null) {
            ContactInformation ci = new ContactInformation("BAD-DATA", "BAD", "BAD");
            user = new User(ci, null);

        }


        User finalUser = user;
        updateUsername(finalUser.getInfo().getName());
        owner.setOnClickListener(v -> {
            NavigationActivity parentActivity = (NavigationActivity) context;
            Bundle args = new Bundle();
            args.putSerializable(ProfileFragment.userKey, finalUser);
            NavController navController = Navigation.findNavController(parentActivity, R.id.nav_host_fragment);
            navController.navigate(R.id.nav_profile, args);
        });

        // Set the activity properly
        boolean isActive = exp.isActive();
        if (isActive) {
            active.setText("Active");
            active.setTextColor(Color.GREEN);
        } else {
            active.setText("Inactive");
            active.setTextColor(Color.RED);
        }

        // Ensure the checkbox is only visible in owned experiments screen
        if (mode.equals("owned")) {
            publishedCheckbox.setVisibility(View.VISIBLE);
        } else {
            publishedCheckbox.setVisibility(View.GONE);
        }

        // Set the published status properly
        boolean isPublished = exp.isPublished();
        publishedCheckbox.setChecked(isPublished);

        // Use a listener to update the published status of experiments
        publishedCheckbox.setOnClickListener(v -> {
            boolean checked = ((CheckBox) v).isChecked();
            exp.setPublished(checked);
        });

        // Set the experiment id
        experimentID.setText(exp.getExperimentId().toString());

        return view;
    }

    private void updateUsername(String name) {
        owner.setText(name);
    }
}
