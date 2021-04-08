package com.example.experiment_automata.ui.trials.MapDisplay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
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
 * Use the {@link MapDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapDisplayFragment extends Fragment {


    public static final String CURRENT_EXPERIMENT ="MAP_POINT_VIEW_EXPERIMENT";

    private MapView currentMapDisplay;
    private Experiment<?> currentExperiment;

    public MapDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currentExperiment the current experiment
     * @return A new instance of fragment map_display_fragment.
     */
    public static MapDisplayFragment newInstance(Experiment<?> currentExperiment) {
        MapDisplayFragment fragment = new MapDisplayFragment();
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_EXPERIMENT, currentExperiment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NavigationActivity) getActivity()).setCurrentScreen(Screen.MAP);

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
        currentMapDisplay = root.findViewById(R.id.map_point_view_fragment_map_display);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_button);
        fab.setVisibility(View.GONE);
        ((NavigationActivity)(getActivity())).setCurrentScreen(Screen.MAP);

        if(currentExperiment.isRequireLocation())
            updateMap(root);
        else
            currentMapDisplay.setVisibility(View.GONE);

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((NavigationActivity)getActivity()).setCurrentScreen(Screen.ExperimentDetails);
    }

    /**
     * sets up the map markers for display
     * @param root
     */
    private void updateMap(View root) {
        ((root.findViewById(R.id.map_point_view_experiment_loc_error_display))).setVisibility(View.GONE);
        currentMapDisplay.setTileSource(TileSourceFactory.MAPNIK);
        currentMapDisplay.setVerticalMapRepetitionEnabled(false);
        currentMapDisplay.setHorizontalMapRepetitionEnabled(false);
        currentMapDisplay.setMultiTouchControls(true);
        IMapController mapController = currentMapDisplay.getController();
        mapController.setZoom(2f);

        ArrayList<Trial<?>> experimentTrials = (ArrayList<Trial<?>>) currentExperiment.getRecordedTrials();

        for(Trial<?> t: experimentTrials){
            /** Code below inspired by Stack Overflow
             * Link: https://stackoverflow.com/questions/55705988/how-to-add-marker-in-osmdroid
             * Author: jignyasa tandel
             * Date: April 16th, 2019
             * License: Unknown
             */
            Marker temp = new Marker(currentMapDisplay);
            temp.setTitle(String.format("Trial for: %s", currentExperiment.getDescription()));
            temp.setSubDescription(String.format("This is a: %s", t.getType()));
            // Set location of marker to be the longitude and latitude of the point t
            temp.setPosition(new GeoPoint(t.getLocation()));
            currentMapDisplay.getOverlays().add(temp);
        }
    }

}
