/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.example.geoto;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.util.Date;

/**
 * A Location Intent service called in the NewVisitFragment to handle location updates
 *
 */
public class LocationService extends IntentService {
    private Location mCurrentLocation;
    private String mLastUpdateTime;

    public LocationService(String name) {
        super(name);
    }

    public LocationService() {
        super("Location Intent");
    }

    /**
     * Called to handle the intent
     * If there are location updates available
     * Plots lines on the google map in the NewVisitFragment
     * Stores the location retrieved into the database
     * @param intent received from the NewVisitFragment
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (LocationResult.hasResult(intent)) {
            LocationResult locResults = LocationResult.extractResult(intent);
            if (locResults != null) {
                for (Location location : locResults.getLocations()) {
                    if (location == null) continue;
                    //do something with the location
                    Log.i("New Location", "Current location: " + location);
                    mCurrentLocation = location;
                    mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                    Log.i("MAP", "new location " + mCurrentLocation.toString());
                    // check if the activity has not been closed in the meantime
                    if (!NewVisitFragment.getButtonState()){
                        NewVisitFragment.setPlaceOld(null);
                        stopSelf();
                    }
                    if (NewVisitFragment.getFragActivity()!=null && NewVisitFragment.getButtonState()){
                        // any modification of the user interface must be done on the UI Thread. The Intent Service is running
                        // in its own thread, so it cannot communicate with the UI.
                        NewVisitFragment.getFragActivity().runOnUiThread(new Runnable() {
                            /**
                             * If activity is not null the location updates are run
                             */
                            public void run() {
                                try {
                                    NewVisitFragment.getMap().setMyLocationEnabled(true);
                                    Date date = new Date();
                                    mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                                    Log.i("MAP", "new location " + mCurrentLocation.toString());
                                    LatLng place2 = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                                    if (NewVisitFragment.getMap() != null)
                                        NewVisitFragment.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 14.0f));
                                    if(NewVisitFragment.getPlaceOld() != null) {
                                        NewVisitFragment.getMap().addPolyline(new PolylineOptions()
                                                .clickable(true)
                                                .add(
                                                        NewVisitFragment.getPlaceOld(),
                                                        place2)
                                        );
                                    } else {
                                        Marker start = NewVisitFragment.getMap().addMarker(new MarkerOptions().position(place2).title("Start")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                    }
                                    NewVisitFragment.setPlaceOld(place2);
                                    NewVisitFragment.storeLocation(mCurrentLocation,date);
                                } catch (Exception e) {
                                    Log.e("LocationService", "Error cannot write on map " + e.getMessage());
                                }
                            }
                        });
                    }
                }
            }

        }
    }
}

