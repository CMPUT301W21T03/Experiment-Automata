package com.example.experiment_automata.ui.trials.add;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.experiment_automata.backend.experiments.CountExperiment;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.trials.CountTrial;
import com.example.experiment_automata.backend.trials.Trial;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCountTrialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCountTrialFragment extends Fragment {

    private MapView currentMapDisplay;
    private Experiment currentExperiment;
    private Location currentLocation;
    private Marker currentLocationMarker;


    public AddCountTrialFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_count_trial, container, false);
        TextView description = (TextView) root.findViewById(R.id.count_trial_experiment_description);
        NavigationActivity parentActivity = ((NavigationActivity) getActivity());
        currentExperiment = (CountExperiment) parentActivity.experimentManager.getCurrentExperiment();
        description.setText(currentExperiment.getDescription());
        currentMapDisplay = root.findViewById(R.id.count_trial_map_view);
        currentLocation = parentActivity.currentLocation;

        /**
         * Authors: OSMDROID Contributors on Github (https://github.com/osmdroid/osmdroid/graphs/contributors)
         * Editors: OSMDROID Contributors on Github (https://github.com/osmdroid/osmdroid/graphs/contributors)
         * License: Apache 2.0
         * Date of Publication: Unknown
         * Full Link: https://github.com/osmdroid/osmdroid
         */
        if(!currentExperiment.isRequireLocation())
            currentMapDisplay.setVisibility(View.GONE);
        else {
            setupMap();
            currentLocationMarker = new Marker(currentMapDisplay);
            currentLocationMarker.setTitle("Current Location");
            if(currentLocation != null)
                currentLocationMarker.setPosition(new GeoPoint(currentLocation));

            Trial t = new CountTrial(parentActivity.loggedUser.getUserId());
            parentActivity.addLocationToTrial(t);

            //TODO: Makes changes to add location from add trial view through touch on map!

            MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
                @Override
                public boolean singleTapConfirmedHelper(GeoPoint p) {

                    //currentLocation.setLatitude(p.getLatitude());
                    //currentLocation.setLongitude(p.getLongitude());
                    Log.d("DONE", "MAYBE 0->" + p.getLatitude());

                    return false;
                }

                @Override
                public boolean longPressHelper(GeoPoint p) {
                    return false;
                }
            };

            MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(getContext(), mapEventsReceiver);
            currentMapDisplay.getOverlays().add(mapEventsOverlay);
        }


        return root;
    }

    private void setupMap()
    {
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        currentMapDisplay.setTileSource(TileSourceFactory.MAPNIK);
        currentMapDisplay.setVerticalMapRepetitionEnabled(false);
        currentMapDisplay.setHorizontalMapRepetitionEnabled(false);
        currentMapDisplay.setMultiTouchControls(true);
        IMapController mapController = currentMapDisplay.getController();
        mapController.setZoom(2f);
    }

}