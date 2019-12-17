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
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ShowPathActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Polyline line;
    private MapView mapView;


    private PageViewModel pageViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_path_info);

        mapView = (MapView) findViewById(R.id.path_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        Bundle b = getIntent().getExtras();
        int position=-1;
        if(b != null) {
            position = b.getInt("position");
            if (position!=-1){
                PathData path = PathsAdapter.getPathItems().get(position);
                Date startDate = path.getStartDate();
                Date endDate = path.getEndDate();

                TextView descriptionView = (TextView) findViewById(R.id.path_description);
                descriptionView.setText(path.getDescription());

                List<LocationData> allLocationData = PathsAdapter.getLocationItems();
                List<LocationData> pathLocationData = new ArrayList<>();
                for (int i = 0; i < allLocationData.size(); i++) {
                    LocationData location = allLocationData.get(i);

                    if (location.getDate().after(startDate) && location.getDate().before(endDate)) {
                        pathLocationData.add(location);
                    }
                }

                List<PhotoData> allPhotoData = PathsAdapter.getPhotoItems();
                List<PhotoData> pathPhotoData = new ArrayList<>();
                for (int i = 0; i < allPhotoData.size(); i++) {
                    PhotoData photo = allPhotoData.get(i);

                    if (photo.getDate().after(startDate) && photo.getDate().before(endDate)) {
                        pathPhotoData.add(photo);
                    }
                }

                LatLng place1 = null;
                LatLng place2 = null;

                for (int i = 0; i < pathLocationData.size(); i++) {
                    LocationData location = pathLocationData.get(i);
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    place2 = new LatLng(lat, lon);

                    if (i==0){
                        googleMap.addMarker(new MarkerOptions().position(place2).title("Start"));
                    }
                    if (i==pathLocationData.size()-1){
                        googleMap.addMarker(new MarkerOptions().position(place2).title("End"));

                    }

                    if (googleMap != null) {
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(place2, 12);
                        googleMap.animateCamera(update);

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

                for (int i = 0; i < pathPhotoData.size(); i++) {
                    PhotoData photoData = pathPhotoData.get(i);

                    LocationData locationData;
                    if (pathLocationData.size() > 0) {
                        locationData = pathLocationData.get(0);
                    } else {
                        locationData  = new LocationData(0, 0, 0, new Date());
                    }
                    for (int j = 0; j < pathLocationData.size(); j++) {
                        Date locationDate = pathLocationData.get(j).getDate();

                        if (locationDate.before(photoData.getDate())) {
                            locationData = pathLocationData.get(j);
                        }
                    }
                    double lat = locationData.getLatitude();
                    double lon = locationData.getLongitude();
                    LatLng place = new LatLng(lat, lon);
                    googleMap.addMarker(new MarkerOptions().position(place));
                }

            }
        }

    }
}
