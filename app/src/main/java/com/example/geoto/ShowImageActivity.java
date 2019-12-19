/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.example.geoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowImageActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private Boolean visible = false;
    private String t_and_p = " ";

    private FloatingActionButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image_info);



        mapView = (MapView) findViewById(R.id.path_description);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment

        backButton = findViewById(R.id.fab_backbutton2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void screenTapped(View view) {
        if (!visible) {
            TextView pressureView = (TextView) findViewById(R.id.photo_pressure);
            pressureView.setVisibility(pressureView.VISIBLE);
            TextView titleView = (TextView) findViewById(R.id.path_title);
            titleView.setVisibility(titleView.VISIBLE);
            TextView tempView = (TextView) findViewById(R.id.photo_temp);
            tempView.setVisibility(tempView.VISIBLE);
            // Tap to hide
            TextView tapView = (TextView) findViewById(R.id.hide_text);
            tapView.setVisibility(tapView.VISIBLE);
            tapView.setText("Tap to hide");
            tapView.setTextColor(Color.BLACK);
            visible = true;
        } else{
            TextView pressureView = (TextView) findViewById(R.id.photo_pressure);
            pressureView.setVisibility(pressureView.INVISIBLE);
            TextView titleView = (TextView) findViewById(R.id.path_title);
            titleView.setVisibility(titleView.INVISIBLE);
            TextView tempView = (TextView) findViewById(R.id.photo_temp);
            tempView.setVisibility(tempView.INVISIBLE);
            TextView tapView = (TextView) findViewById(R.id.hide_text);
            tapView.setText("Tap to show");
            tapView.setTextColor(Color.WHITE);
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
                List<PhotoData> allPhotoData = GalleryAdapter.getPhotoItems();
                PhotoData photoData = allPhotoData.get(position);
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
                Date startDate = pathData.getStartDate();
                Date endDate = pathData.getEndDate();

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
                pressureView.setText(Float.toString(pressureData.getPressure()));

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
                tempView.setText(Float.toString(tempData.getTemp()));

                List<LocationData> allLocationData = GalleryAdapter.getLocationItems();
                List<LocationData> pathLocationData = new ArrayList<>();
                for (int i = 0; i < allLocationData.size(); i++) {
                    LocationData location = allLocationData.get(i);

                    if (location.getDate().after(startDate) && location.getDate().before(endDate)) {
                        pathLocationData.add(location);
                    }
                }

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
                    PhotoData pathPhoto = pathPhotoData.get(i);

                    LocationData locationData;
                    if (pathLocationData.size() > 0) {
                        locationData = pathLocationData.get(0);
                    } else {
                        locationData  = new LocationData(0, 0, 0, new Date());
                    }
                    for (int j = 0; j < pathLocationData.size(); j++) {
                        Date locationDate = pathLocationData.get(j).getDate();

                        if (locationDate.before(pathPhoto.getDate())) {
                            locationData = pathLocationData.get(j);
                        }
                    }
                    double lat = locationData.getLatitude();
                    double lon = locationData.getLongitude();
                    LatLng place = new LatLng(lat, lon);

                    Drawable icon = getResources().getDrawable(R.drawable.ic_insert_photo_blue_24dp);
                    Canvas canvas = new Canvas();

                    if (pathPhoto.equals(photoData)) {
                        Bitmap bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth()*2, icon.getIntrinsicHeight()*2, Bitmap.Config.ARGB_8888);
                        canvas.setBitmap(bitmap);
                        icon.setBounds(0, 0, icon.getIntrinsicWidth()*2, icon.getIntrinsicHeight()*2);
                        icon.draw(canvas);
                        googleMap.addMarker(new MarkerOptions().position(place).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                    } else {
                        Bitmap bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        canvas.setBitmap(bitmap);
                        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                        icon.draw(canvas);
                        googleMap.addMarker(new MarkerOptions().position(place).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                    }
                }
            }
        }
    }
}
