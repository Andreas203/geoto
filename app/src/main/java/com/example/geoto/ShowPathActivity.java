/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.example.geoto;

import android.location.Location;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.geoto.database.LocationData;

import com.example.geoto.database.PathData;
import com.example.geoto.database.PhotoData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ShowPathActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Polyline line;

    private LatLng place1 = null;
    private LatLng place2 = null;

    private PageViewModel pageViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_path_info);

        Bundle b = getIntent().getExtras();
        int position=-1;
        if(b != null) {
            position = b.getInt("position");
            if (position!=-1){
                MapView mapView = (MapView) findViewById(R.id.path_map);
                TextView descriptionView = (TextView) findViewById(R.id.path_description);

                PathData path = PathsAdapter.getPathItems().get(position);
                Date startDate = path.getStartDate();
                Date endDate = path.getEndDate();
                List<LocationData> allLocationData = PathsAdapter.getLocationItems();
                List<LocationData> pathLocationData = new ArrayList<>();

                for (int i = 0; i < allLocationData.size(); i++) {
                    LocationData location = allLocationData.get(i);

                    if (location.getDate().after(startDate) && location.getDate().before(endDate)) {
                        pathLocationData.add(location);
                    }
                }

                //mapView.onCreate(savedInstanceState);
                //mapView.onResume();
                mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment

                for (int i = 0; i < pathLocationData.size(); i++) {
                    LocationData location = pathLocationData.get(i);
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    place2 = new LatLng(lat, lon);
                    if (googleMap != null) {
                        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new place2.getLatitude(), place2.getLongitude()), 14.0f);

                        if (place1 != null) {
                            googleMap.addPolyline(new PolylineOptions()
                                    .clickable(true)
                                    .add(
                                            place1,
                                            place2)
                            );
                        }
                        place1 = place2;
                    }
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
    }
}
