package com.example.experiment_automata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This class connects the experimentlayout XML file with the activity_subscription.xml file
 */
public class SubscriptionAdapter extends ArrayAdapter<Experiment> {
    // Syntax inspired by Abdul Ali Bangash, "Lab 3 Instructions - Custom List",
    // 2021-02-04, Public Domain, https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf

    private ArrayList<Experiment> experiment;
    private Context context;

    /**
     * Constructor takes in an array list of experiments and a context to set the attributes properly
     * @param context
     * @param experiments
     */
    public SubscriptionAdapter(Context context, ArrayList<Experiment> experiments){
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
        View view=convertView;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.experimentlayout, parent, false);
        }
        // The experiment we're going to set the XML file with
        Experiment exp=experiment.get(position);

        // Find the corresponding views from experimentlayout
        TextView name=view.findViewById(R.id.experimentName);
        TextView owner=view.findViewById(R.id.experimentOwner);
        TextView active=view.findViewById(R.id.experimentActivity);

        // Set the name of the experiment accordingly
        UUID oid=exp.getOwnerId();
        name.setText(oid.toString());

        // Set the activity properly
        boolean isActive=exp.isActive();
        if(isActive){
            active.setText("Active");
        }
        else{
            active.setText("Inactive");
        }

        return view;
    }
}
