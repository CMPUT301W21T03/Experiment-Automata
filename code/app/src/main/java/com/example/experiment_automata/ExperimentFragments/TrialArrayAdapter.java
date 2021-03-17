package com.example.experiment_automata.ExperimentFragments;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.experiment_automata.R;
import com.example.experiment_automata.UserInformation.User;
import com.example.experiment_automata.trials.Trial;

import java.util.ArrayList;
import java.util.UUID;

public class TrialArrayAdapter extends ArrayAdapter<Trial> {
    // Syntax inspired by Abdul Ali Bangash, "Lab 3 Instructions - Custom List",
    // 2021-02-04, Public Domain, https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf

    private ArrayList<Trial> trials;
    private Context context;

    /**
     * Constructor takes in an array list of experiments and a context to set the attributes properly
     * @param context
     *  Context of the array adapter
     * @param trialList
     *  the list of the trials to display
     */
    public TrialArrayAdapter(Context context, ArrayList<Trial> trialList){
        super(context, 0, trialList);
        this.trials = trialList;
        this.context = context;
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
        Trial trial = trials.get(position);

        // Find the corresponding views from experiment_layout
        TextView experimenter = view.findViewById(R.id.experimentOwner);

        // Set the name of the experiment accordingly
        UUID oid = trial.getCollector();
        experimenter.setText(User.getInstance(oid).getInfo().getName());

        return view;
    }
}
