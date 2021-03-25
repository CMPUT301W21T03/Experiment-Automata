package com.example.experiment_automata.ui.trials.MapDisplay;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.trials.Trial;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;


public class MapPointViewFragment extends Fragment
{
    public static final String CURRENT_EXPERIMENT ="MAP_POINT_VIEW_EXPERIMENT";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

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
        currentMapDisplay.setTileSource(TileSourceFactory.MAPNIK);
        currentExperiment = (Experiment) getArguments().getSerializable(CURRENT_EXPERIMENT);
        ArrayList<Trial> experimentTrials = currentExperiment.getRecordedTrials();

        if(currentExperiment.isRequireLocation())
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
                Double lati = t.getLocation().getLatitude();
                Double longi = t.getLocation().getLongitude();
                GeoPoint trialPoint = new GeoPoint(lati, longi);

                // Set location of marker to be the longitude and latitude of the point t
                temp.setPosition(trialPoint);
                currentMapDisplay.getOverlays().add(temp);
            }

        }

        return root;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
