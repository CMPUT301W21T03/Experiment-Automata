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

import com.example.experiment_automata.Experiments.ExperimentModel.Experiment;
import com.example.experiment_automata.R;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This class connects the experiment_layout XML file with the fragment_home.xml file
 */
public class ExperimentListAdapter extends ArrayAdapter<Experiment> {
    // Syntax inspired by Abdul Ali Bangash, "Lab 3 Instructions - Custom List",
    // 2021-02-04, Public Domain, https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf

    private ArrayList<Experiment> experiment;
    private Context context;

    /**
     * Constructor takes in an array list of experiments and a context to set the attributes properly
     * @param context
     * @param experiments
     */
    public ExperimentListAdapter(Context context, ArrayList<Experiment> experiments){
        super(context, 0, experiments);
        this.experiment=experiments;
        this.context=context;
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
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.experiment_layout, parent, false);
        }
        // The experiment we're going to set the XML file with
        Experiment exp = experiment.get(position);

        // Find the corresponding views from experiment_layout
        TextView name = view.findViewById(R.id.experimentName);
        TextView owner = view.findViewById(R.id.experimentOwner);
        TextView active = view.findViewById(R.id.experimentActivity);
        TextView experimentID = view.findViewById(R.id.experiment__id);

        // Set the name of the experiment accordingly
        UUID oid = exp.getOwnerId();
        name.setText(exp.getDescription());

        // Set the activity properly
        boolean isActive = exp.isActive();
        if(isActive) {
            active.setText("Active");
            active.setTextColor(Color.GREEN);
        }
        else{
            active.setText("Inactive");
            active.setTextColor(Color.RED);
        }

        // Set the experiment id
        experimentID.setText(exp.getExperimentId().toString());

        return view;
    }
}
