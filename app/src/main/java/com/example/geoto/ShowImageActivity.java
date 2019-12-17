/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.example.geoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geoto.database.PhotoData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

public class ShowImageActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image_info);

        Bundle b = getIntent().getExtras();
        int position=-1;
        if(b != null) {
            position = b.getInt("position");
            if (position!=-1){
                ImageView imageView = (ImageView) findViewById(R.id.image);
                PhotoData element = GalleryAdapter.getItems().get(position);
                if (element.getAbsolutePath()!=null) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(element.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                }

                mapView = (MapView) findViewById(R.id.image_map);
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
