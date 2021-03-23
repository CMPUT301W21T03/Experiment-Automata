package com.example.experiment_automata.backend.Location;

import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 *  Role/Pattern:
 *      The goal of this class is to get and updates the current location
 *      that the user has is in.
 *
 *  Bugs:
 *      1. None
 *
 *  Source/Citation:
 *      1.
 *          Author: https://stackoverflow.com/users/1371853/swiftboy
 *          Editor: https://stackoverflow.com/users/202311/ianb
 *          Full Source: https://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android
 */

public class LocationServices implements LocationListener
{
    Location currentLocation;

    public LocationServices()
    {
        currentLocation = null;
    }

    /**
     * Called when the location has changed.
     *
     * @param location the updated location
     */
    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        Log.d("Location Changed LAT", "" + location.getLatitude());
        Log.d("Location Changed LONG", ""+ location.getLongitude());
        currentLocation = location;
    }

    public Location getCurrentLocation()
    {
        return currentLocation;
    }
}
