/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.example.geoto;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.geoto.database.LocationData;
import com.example.geoto.database.PathData;
import com.example.geoto.database.PhotoData;
import com.example.geoto.database.PressureData;
import com.example.geoto.database.TempData;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.List;

public class ShowImageActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private Boolean visible = true;
    private String t_and_p = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image_info);



        mapView = (MapView) findViewById(R.id.image_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment
    }

    public void screenTapped(View view) {
        if (!visible) {

            TextView pressureView = (TextView) findViewById(R.id.photo_pressure);

            pressureView.setVisibility(pressureView.VISIBLE);
            TextView titleView = (TextView) findViewById(R.id.path_title);
            titleView.setVisibility(titleView.VISIBLE);
            TextView tempView = (TextView) findViewById(R.id.photo_temp);
            tempView.setVisibility(tempView.VISIBLE);
            visible = true;
        } else{
            TextView pressureView = (TextView) findViewById(R.id.photo_pressure);
            pressureView.setVisibility(pressureView.INVISIBLE);
            TextView titleView = (TextView) findViewById(R.id.path_title);
            titleView.setVisibility(titleView.INVISIBLE);
            TextView tempView = (TextView) findViewById(R.id.photo_temp);
            tempView.setVisibility(tempView.INVISIBLE);
            visible = false;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        Bundle b = getIntent().getExtras();
        int position=-1;
        if(b != null) {
            position = b.getInt("position");
            if (position!=-1){
                PhotoData photoData = GalleryAdapter.getPhotoItems().get(position);
                ImageView imageView = (ImageView) findViewById(R.id.image);
                if (photoData.getAbsolutePath()!=null) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(photoData.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                }

                PathData pathData = null;
                List<PathData> allPathData = GalleryAdapter.getPathItems();
                for (int i = 0; i < allPathData.size(); i++) {
                    Date startDate = allPathData.get(i).getStartDate();
                    Date endDate = allPathData.get(i).getEndDate();

                    if (photoData.getDate().after(startDate) && photoData.getDate().before(endDate)) {
                        pathData = allPathData.get(i);
                    }
                }
                TextView titleView = (TextView) findViewById(R.id.path_title);
                titleView.setText(pathData.getTitle());

                PressureData pressureData;
                List<PressureData> allPressureData = GalleryAdapter.getPressureItems();
                if (allPressureData.size() > 0) {
                    pressureData = allPressureData.get(0);
                } else {
                    pressureData = new PressureData(0, new Date());
                }
                for (int j = 0; j < allPressureData.size(); j++) {
                    Date pressureDate = allPressureData.get(j).getDate();

                    if (pressureDate.before(photoData.getDate())) {
                        pressureData = allPressureData.get(j);
                    }
                }
                TextView pressureView = (TextView) findViewById(R.id.photo_pressure);
                String pressure_var = Float.toString(pressureData.getPressure()) + " Pa";
                pressureView.setText(pressure_var);

                TempData tempData;
                List<TempData> allTempData = GalleryAdapter.getTempItems();
                if (allTempData.size() > 0) {
                    tempData = allTempData.get(0);
                } else {
                    tempData = new TempData(0, new Date());
                }
                for (int j = 0; j < allTempData.size(); j++) {
                    Date tempDate = allTempData.get(j).getDate();

                    if (tempDate.before(photoData.getDate())) {
                        tempData = allTempData.get(j);
                    }
                }
                TextView tempView = (TextView) findViewById(R.id.photo_temp);
                String temp_var = Float.toString(tempData.getTemp()) + " 'C";
                tempView.setText(temp_var);



                LocationData locationData;
                List<LocationData> allLocationData = GalleryAdapter.getLocationItems();
                if (allLocationData.size() > 0) {
                    locationData = allLocationData.get(0);
                } else {
                    locationData = new LocationData(0, 0, 0, new Date());
                }
                for (int j = 0; j < allLocationData.size(); j++) {
                    Date locationDate = allLocationData.get(j).getDate();

                    if (locationDate.before(photoData.getDate())) {
                        locationData = allLocationData.get(j);
                    }
                }
                double lat = locationData.getLatitude();
                double lon = locationData.getLongitude();
                LatLng place = new LatLng(lat, lon);
                googleMap.addMarker(new MarkerOptions().position(place));
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(place, 12);
            }
        }
    }
}
