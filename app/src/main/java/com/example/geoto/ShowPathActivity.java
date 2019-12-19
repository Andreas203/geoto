/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.example.geoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geoto.database.LocationData;

import com.example.geoto.database.PathData;
import com.example.geoto.database.PhotoData;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * The activity to display a single path's information
 */
public class ShowPathActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap googleMap;
    private Polyline line;
    private MapView mapView;

    private PageViewModel pageViewModel;

    private FloatingActionButton backButton;

    /**
     * Called when the show path activity is first created.
     * Initialises the google map to display.
     * Creates a return button.
     * @param savedInstanceState the current state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_path_info);

        mapView = (MapView) findViewById(R.id.path_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment

        backButton = findViewById(R.id.fab_backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    /**
     * Resumes the activity
     */
    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * When an image marker is clicked the image at that location is displayed
     * @param marker the clicked marker
     * @return whether it was handled
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.getTag() != null) {
            String absolutePath = (String) marker.getTag();

            Intent intent = new Intent(this, ShowBigImageActivity.class);
            intent.putExtra("path", absolutePath);
            this.startActivity(intent);

            return true;
        } else {
            return false;
        }

    }

    /**
     * The callback for the map initialisation.
     * Displays the path map.
     * Displays the path description.
     * @param map the google map to display
     */
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setOnMarkerClickListener(this);

        Bundle b = getIntent().getExtras();
        int position=-1;
        if(b != null) {
            position = b.getInt("position");
            if (position!=-1){
                // Retrieves the current path and relevant dates
                PathData path = PathsAdapter.getPathItems().get(position);
                Date startDate = path.getStartDate();
                Date endDate = path.getEndDate();

                // Displays the path description
                TextView descriptionView = (TextView) findViewById(R.id.path_description);
                descriptionView.setText(path.getDescription());

                // Retrieves the path's locations
                List<LocationData> allLocationData = PathsAdapter.getLocationItems();
                List<LocationData> pathLocationData = new ArrayList<>();
                for (int i = 0; i < allLocationData.size(); i++) {
                    LocationData location = allLocationData.get(i);

                    if (location.getDate().after(startDate) && location.getDate().before(endDate)) {
                        pathLocationData.add(location);
                    }
                }

                LatLng place1 = null;
                LatLng place2 = null;
                // Displays the photo's path on the map
                for (int i = 0; i < pathLocationData.size(); i++) {
                    LocationData location = pathLocationData.get(i);
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    place2 = new LatLng(lat, lon);

                    // Display start and end markers
                    if (i==0){
                        Marker start = googleMap.addMarker(new MarkerOptions().position(place2).title("Start")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        start.setTag(null);
                    }
                    if (i==pathLocationData.size()-1){
                        Marker end = googleMap.addMarker(new MarkerOptions().position(place2).title("End"));
                        end.setTag(null);
                    }

                    // Plotting the path line
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

                // Retrieves all the path photos
                List<PhotoData> allPhotoData = PathsAdapter.getPhotoItems();
                List<PhotoData> pathPhotoData = new ArrayList<>();
                for (int i = 0; i < allPhotoData.size(); i++) {
                    PhotoData photo = allPhotoData.get(i);

                    if (photo.getDate().after(startDate) && photo.getDate().before(endDate)) {
                        pathPhotoData.add(photo);
                    }
                }

                // Displays all of the path photos on the map as markers
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
                    Drawable icon = getResources().getDrawable(R.drawable.ic_insert_photo_blue_24dp);
                    Canvas canvas = new Canvas();
                    Bitmap bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    canvas.setBitmap(bitmap);
                    icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                    icon.draw(canvas);

                    Marker marker = googleMap.addMarker(new MarkerOptions().position(place).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                    marker.setTag(photoData.getAbsolutePath());

                }

            }
        }

    }
}
