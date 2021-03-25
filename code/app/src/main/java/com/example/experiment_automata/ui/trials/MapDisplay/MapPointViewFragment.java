package com.example.experiment_automata.ui.trials.MapDisplay;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.trials.Trial;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;


public class MapPointViewFragment extends Fragment
{
    public static final String CURRENT_EXPERIMENT ="MAP_POINT_VIEW_EXPERIMENT";

    private MapView currentMapDisplay;
    private Experiment currentExperiment;

    public MapPointViewFragment()
    {
        //Needed for fragment to work.
        //Empty for now
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         * Authors: OSMDROID Contributors on Github (https://github.com/osmdroid/osmdroid/graphs/contributors)
         * Editors: OSMDROID Contributors on Github (https://github.com/osmdroid/osmdroid/graphs/contributors)
         * License: Apache 2.0
         * Date of Publication: Unknown
         * Full Link: https://github.com/osmdroid/osmdroid
         */
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));

        View root = inflater.inflate(R.layout.fragment_map_view, container, false);
        currentMapDisplay = root.findViewById(R.id.map_point_view_fragment_map_display);
        currentExperiment = (Experiment) getArguments().getSerializable(CURRENT_EXPERIMENT);
        ArrayList<Trial> experimentTrials = currentExperiment.getRecordedTrials();

        if(!currentExperiment.isRequireLocation())
            currentMapDisplay.setVisibility(View.VISIBLE);
        else
        {



            for(Trial t: currentExperiment.getRecordedTrials()){
                /** Code below inspired by Stack Overflow
                * Link: https://stackoverflow.com/questions/55705988/how-to-add-marker-in-osmdroid
                 * Author: jignyasa tandel
                 * Date: April 16th, 2019
                 * License: Unknown
                 */
                Marker temp = new Marker(currentMapDisplay);
                // Set location of marker to be the longitude and latitude of the point t
                temp.setPosition(new GeoPoint(t.getLocation().getLongitude(), t.getLocation().getLatitude()));

                currentMapDisplay.getOverlays().add(temp);
                //temp.setAnchor();
            }

        }

        return root;
    }
}
