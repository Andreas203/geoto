/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.example.geoto;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Polyline;

public class ShowPathActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private Polyline line;

    private PageViewModel pageViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_path_info);

        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);

        Bundle b = getIntent().getExtras();
        int position=-1;
        if(b != null) {
            position = b.getInt("position");



            if (position!=-1){
                mapView = (MapView) findViewById(R.id.path_map);
                mapView.onCreate(savedInstanceState);
                mapView.onResume();
                mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment



            }

        }


    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
    }
}
