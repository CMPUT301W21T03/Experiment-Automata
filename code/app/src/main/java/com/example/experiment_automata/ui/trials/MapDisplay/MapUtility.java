package com.example.experiment_automata.ui.trials.MapDisplay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.trials.Trial;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.ui.Screen;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

/**
 * Role/Pattern:
 *     Deals with map information when displaying and adding trial locations
 *
 *  Known Issue:
 *
 *      1. None
 */
public class MapUtility {
    private static final double MIN_BOUND_SIZE_LAT = -85.05112877980658;
    private static final double MAX_BOUND_SIZE_LAT = 85.05112877980658;
    private static final double MIN_BOUND_SIZE_LONG = -180.0;
    private static final double MAX_BOUND_SIZE_LONG = 180.0;
    private final Experiment<?> experiment;
    private final Trial<?> trial;
    private final MapView display;
    private final Context context;
    private final Marker marker;
    private final NavigationActivity parentActivity;
    private Button revertBack;

    public MapUtility(Experiment<?> experiment,
                      MapView display,
                      Context context,
                      NavigationActivity parentActivity,
                      Trial<?> trial) {
        this.experiment = experiment;
        this.display = display;
        this.context = context;
        this.parentActivity = parentActivity;
        this.trial = trial;
        marker = new Marker(this.display);
    }

    public void setRevertBack(Button revertBack) {
        this.revertBack = revertBack;
    }

    /**
     * Set up the map such it moves a marker around where the user clicks.
     * Where the user clicks is the location of that trial.
     *
     * */
    private void mapSupport() {
        setupMap();
        /**
         * Authors: OSMDROID Contributors on Github (https://github.com/osmdroid/osmdroid/graphs/contributors)
         * Editors: OSMDROID Contributors on Github (https://github.com/osmdroid/osmdroid/graphs/contributors)
         * License: Apache 2.0
         * Date of Publication: Unknown
         * Full Link: https://github.com/osmdroid/osmdroid
         */
        if (!experiment.isRequireLocation()) {
            display.setVisibility(View.GONE);
            revertBack.setVisibility(View.GONE);
        } else {
            setupMap();
            marker.setTitle("Recorded Location");
            marker.setSubDescription("This location is what is saved into the trial!");

            parentActivity.addLocationToTrial(trial);

            if (trial.getLocation() == null){
                // Ends the function
                AlertDialog myDialog = new AlertDialog.Builder(context).create();

                myDialog.setTitle("Location not enabled");

                myDialog.setMessage("Please enable location on your device to add a new trial");

                myDialog.show();

                NavController navController = Navigation.findNavController(parentActivity, R.id.nav_host_fragment);

                navController.popBackStack();
                return;
            }

            // Used the following source from the documentation for help with using AlertDialog
            // URL: https://developer.android.com/guide/topics/ui/dialogs
            // Author: Google LLC
            // Date: 2019-12-27
            // License: Apache 2.0
            if (!parentActivity.stopRemindingMe) {
                // Warns the user that their location is being used
                AlertDialog.Builder myDialog = new AlertDialog.Builder(context)
                        .setPositiveButton("Don't Show Again", (dialog, id) -> {
                            parentActivity.stopRemindingMe = true;
                            parentActivity.setCurrentScreen(Screen.Trial);
                        })
                        .setNegativeButton("Refuse", (dialog, id) -> {
                            trial.setLocation(null);
                            parentActivity.setCurrentScreen(Screen.ExperimentDetails);
                            parentActivity.onBackPressed();
                        })
                        .setNeutralButton("Okay", (dialog, which) -> parentActivity.setCurrentScreen(Screen.Trial));

                myDialog.setTitle("Warning");

                myDialog.setMessage("Your location is being used. Please click OK to use location");

                myDialog.create();

                myDialog.show();
            }
            GeoPoint oldLocation = new GeoPoint(trial.getLocation());
            parentActivity.setCurrentScreen(Screen.Trial);

            if (revertBack != null) {
                revertBack.setOnClickListener(v -> {

                    // Maybe make into function -> Violating dry a bit :p
                    marker.setPosition(oldLocation);
                    trial.getLocation().setLongitude(oldLocation.getLongitude());
                    trial.getLocation().setLatitude(oldLocation.getLatitude());
                    display.invalidate();
                });
            }

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
                    boolean boundingBoxLatiCheck = p.getLatitude() > MIN_BOUND_SIZE_LAT && p.getLatitude() < MAX_BOUND_SIZE_LAT;
                    boolean boundingBoxLongiCheck = p.getLongitude() > MIN_BOUND_SIZE_LONG && p.getLongitude() < MAX_BOUND_SIZE_LONG;
                    if (boundingBoxLatiCheck && boundingBoxLongiCheck) {
                        display.invalidate();
                        trial.getLocation().setLongitude(p.getLongitude());
                        trial.getLocation().setLatitude(p.getLatitude());
                        marker.setPosition(new GeoPoint(trial.getLocation()));
                    }
                    return false;
                }

                @Override
                public boolean longPressHelper(GeoPoint p) {

                    Toast.makeText(context, p.toString(), Toast.LENGTH_SHORT).show();

                    return false;
                }
            };

            MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(context, mapEventsReceiver);
            display.getOverlays().add(mapEventsOverlay);
            display.getOverlays().add(marker);
        }
    }

    /**
     * Initializes all the needed items for the map to be displayed and viewed
     */
    private void setupMap() {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        display.setTileSource(TileSourceFactory.MAPNIK);
        display.setVerticalMapRepetitionEnabled(false);
        display.setHorizontalMapRepetitionEnabled(false);
        display.setMultiTouchControls(true);
        IMapController mapController = display.getController();
        mapController.setZoom(2f);
    }


    /**
     * this class removes all maps assets
     * so that we can remove assets when the map is not in use.
     */
    private void makeMapItemsDisappear() {
        if (revertBack != null)
            revertBack.setVisibility(View.GONE);
        display.setVisibility(View.GONE);
    }

    /**
     * this method sets things to deal with the map
     */
    public void run() {
        if (experiment.isRequireLocation()) {
            mapSupport();
        } else {
            makeMapItemsDisappear();
        }
    }
}
