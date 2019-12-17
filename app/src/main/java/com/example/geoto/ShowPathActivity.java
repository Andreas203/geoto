/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.example.geoto;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.geoto.database.LocationData;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Date;
import java.util.List;


public class ShowPathActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
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
           // position = b.getInt("position");
            Date startDate = new Date();
            Date endDate = new Date();

            startDate.setTime(b.getLong("startDate",-1));
            startDate.setTime(b.getLong("endDate",-1));
//            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//            Date startDate=df.parse(sDate);
//            Date endDate=df.parse(eDate);

            mapView = (MapView) findViewById(R.id.path_map);
            //mapView.onCreate(savedInstanceState);
            //mapView.onResume();
            mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment

            //if (position!=-1){
            pageViewModel.getPathLocationToDisplay(startDate, endDate).observe(this, new Observer<List<LocationData>>(){
                @Override
                public void onChanged(@Nullable final List<LocationData> locList) {
                    for (int i = 0; i < locList.size(); i++) {
                        LocationData locationData = locList.get(i);
                        double lat = locationData.getLatitude();
                        double lon = locationData.getLongitude();
                        place2 = new LatLng(lat, lon);
                        if (googleMap != null)

                            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new place2.getLatitude(), place2.getLongitude()), 14.0f);

                        if(place1 != null)
                            googleMap.addPolyline(new PolylineOptions()
                                    .clickable(true)
                                    .add(
                                            place1,
                                            place2)
                            );
                        place1 = place2;
                    }
                }});



            //}

        }


    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
    }
}
