package com.example.experiment_automata.ui.trials.MapDisplay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.trials.Trial;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.ui.Screen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link map_display_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class map_display_fragment extends Fragment {


    public static final String CURRENT_EXPERIMENT ="MAP_POINT_VIEW_EXPERIMENT";

    private MapView currentMapDisplay;
    private Experiment currentExperiment;

    public map_display_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currentExperiment the current experiment
     * @return A new instance of fragment map_display_fragment.
     */
    public static map_display_fragment newInstance(Experiment currentExperiment) {
        map_display_fragment fragment = new map_display_fragment();
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_EXPERIMENT, currentExperiment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NavigationActivity)(getActivity())).setCurrentScreen(Screen.MAP);

        if (getArguments() != null) {
            currentExperiment = (Experiment) getArguments().getSerializable(CURRENT_EXPERIMENT);
        }
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
        View root = inflater.inflate(R.layout.fragment_map_display_fragment, container, false);

        if(currentExperiment.isRequireLocation())
        {
            currentMapDisplay = root.findViewById(R.id.map_point_view_fragment_map_display);
            currentMapDisplay.setTileSource(TileSourceFactory.MAPNIK);
            currentMapDisplay.setVerticalMapRepetitionEnabled(false);
            currentMapDisplay.setHorizontalMapRepetitionEnabled(false);
            currentMapDisplay.setMultiTouchControls(true);
            IMapController mapController = currentMapDisplay.getController();
            mapController.setZoom(2.5f);

            ArrayList<Trial> experimentTrials = currentExperiment.getRecordedTrials();

            for(Trial t: experimentTrials){
                /** Code below inspired by Stack Overflow
                 * Link: https://stackoverflow.com/questions/55705988/how-to-add-marker-in-osmdroid
                 * Author: jignyasa tandel
                 * Date: April 16th, 2019
                 * License: Unknown
                 */
                Marker temp = new Marker(currentMapDisplay);
                temp.setTitle("Trial for: " + currentExperiment.getDescription());
                temp.setSubDescription("This is a: " + t.getType());
                // Set location of marker to be the longitude and latitude of the point t
                temp.setPosition(new GeoPoint(t.getLocation().getLatitude(), t.getLocation().getLongitude()));
                currentMapDisplay.getOverlays().add(temp);
                //temp.setAnchor();
            }
        }

        return root;
    }
}