/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.example.geoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.geoto.database.LocationData;
import com.example.geoto.database.PathData;
import com.example.geoto.database.PhotoData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShowPathActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private Polyline line;

    private PageViewModel pageViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_path);

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
                        place2 = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                        if (googleMap != null)

                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 14.0f));

                        if(place1 != null)
                            googleMap.addPolyline(new PolylineOptions()
                                    .clickable(true)
                                    .add(
                                            place1,
                                            place2)
                            );
                        place1 = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
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
