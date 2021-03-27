package com.example.experiment_automata.ui.trials.MapDisplay;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.trials.CountTrial;
import com.example.experiment_automata.backend.trials.Trial;
import com.example.experiment_automata.ui.NavigationActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class MapUtility
{
    private Experiment experiment;
    private Trial trial;
    private MapView display;
    private Context context;
    private Marker marker;
    private NavigationActivity parentActivity;

    public MapUtility(Experiment experiment,
                      MapView display,
                      Context context,
                      NavigationActivity parentActivity,
                      Trial trial)
    {
        this.experiment = experiment;
        this.display = display;
        this.context = context;
        this.parentActivity = parentActivity;
        this.trial = trial;
        marker = new Marker(this.display);
    }

    public void mapSupport()
    {
        setupMap();
        /**
         * Authors: OSMDROID Contributors on Github (https://github.com/osmdroid/osmdroid/graphs/contributors)
         * Editors: OSMDROID Contributors on Github (https://github.com/osmdroid/osmdroid/graphs/contributors)
         * License: Apache 2.0
         * Date of Publication: Unknown
         * Full Link: https://github.com/osmdroid/osmdroid
         */
        if(!experiment.isRequireLocation())
            display.setVisibility(View.GONE);
        else {
            setupMap();
            marker.setTitle("Recorded Location");
            marker.setSubDescription("This location is what is saved into the trial!");

            parentActivity.addLocationToTrial(trial);
            marker.setPosition(new GeoPoint(trial.getLocation()));

            /**
             * How to set the on click listener
             * Author: https://github.com/wildfiregt
             * Editor: https://github.com/wildfiregt
             * Licence: Unknown
             * Date of Publication: Unknown
             * Full Link: https://github.com/osmdroid/osmdroid/issues/295#issuecomment-207787051
             */
            MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
                @Override
                public boolean singleTapConfirmedHelper(GeoPoint p) {
                    display.invalidate();
                    trial.getLocation().setLongitude(p.getLongitude());
                    trial.getLocation().setLatitude(p.getLatitude());
                    marker.setPosition(new GeoPoint(trial.getLocation()));
                    return false;
                }

                @Override
                public boolean longPressHelper(GeoPoint p) {

                    Toast.makeText(context, "" + p, Toast.LENGTH_SHORT).show();

                    return false;
                }
            };

            MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(context, mapEventsReceiver);
            display.getOverlays().add(mapEventsOverlay);
            display.getOverlays().add(marker);
        }

        parentActivity.addTrial(experiment, trial);
    }

    private void setupMap()
    {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        display.setTileSource(TileSourceFactory.MAPNIK);
        display.setVerticalMapRepetitionEnabled(false);
        display.setHorizontalMapRepetitionEnabled(false);
        display.setMultiTouchControls(true);
        IMapController mapController = display.getController();
        mapController.setZoom(2f);
    }
}
