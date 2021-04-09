package com.example.experiment_automata.backend.location;

import android.location.Location;
import android.location.LocationListener;
import androidx.annotation.NonNull;

/**
 *  Role/Pattern:
 *      The goal of this class is to get and updates the current location
 *      that the user has is in.
 *
 *  Source/Citation:
 *      1.
 *          Author: https://stackoverflow.com/users/1371853/swiftboy
 *          Editor: https://stackoverflow.com/users/202311/ianb
 *          Date: January 6th, 2016
 *          License:   CC BY-SA 3.0
 *          Full Source: https://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android
 */

public class LocationServices implements LocationListener {
    public Location currentLocation;

    public LocationServices() {
        currentLocation = null;
    }

    /**
     * Called when the location has changed.
     *
     * @param location the updated location
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentLocation = location;
    }
}
