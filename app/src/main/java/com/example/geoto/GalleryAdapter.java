/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.example.geoto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.geoto.database.LocationData;
import com.example.geoto.database.PathData;
import com.example.geoto.database.PhotoData;
import com.example.geoto.database.PressureData;
import com.example.geoto.database.TempData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.View_Holder> {
    static private Context context;
    private static List<PhotoData> photoItems;
    private static List<PathData> pathItems;
    private static List<LocationData> locationItems;
    private static List<PressureData> pressureItems;
    private static List<TempData> tempItems;

    public GalleryAdapter(List<PhotoData> items) {
        this.photoItems = items;
    }

    public GalleryAdapter() {
        super();
        photoItems = new ArrayList<>();
        pathItems = new ArrayList<>();
        locationItems = new ArrayList<>();
        pressureItems = new ArrayList<>();
        tempItems = new ArrayList<>();
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image,
                parent, false);
        View_Holder holder = new View_Holder(v);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the
        // current row on the RecyclerView
        if (holder!=null && photoItems.get(position)!=null) {
            if (photoItems.get(position).getAbsolutePath()!=null) {
                Bitmap myBitmap = BitmapFactory.decodeFile(photoItems.get(position).getAbsolutePath());
                holder.imageView.setImageBitmap(myBitmap);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowImageActivity.class);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        }
        //animate(holder);
    }


    // convenience method for getting data at click position
    PhotoData getItem(int id) {
        return photoItems.get(id);
    }

    @Override
    public int getItemCount() {
        return photoItems.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder  {
        ImageView imageView;

        View_Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_item);

        }
    }

    public void sortGallery(int sortCode) {
        if ((photoItems.size()) > 0) {
            if (sortCode == 0) {
                Collections.sort(photoItems);
            }
            if (sortCode == 1) {
                Collections.sort(photoItems, Collections.reverseOrder());
            }
        }
    }

    public static List<PhotoData> getPhotoItems() {
        return photoItems;
    }

    public static void setPhotoItems(List<PhotoData> photoItems) {
        GalleryAdapter.photoItems = photoItems;
    }

    public static List<PathData> getPathItems() {
        return pathItems;
    }

    public static void setPathItems(List<PathData> pathItems) {
        GalleryAdapter.pathItems = pathItems;
    }

    public static List<LocationData> getLocationItems() {
        return locationItems;
    }

    public static void setLocationItems(List<LocationData> locationItems) {
        GalleryAdapter.locationItems = locationItems;
    }

    public static List<PressureData> getPressureItems() {
        return pressureItems;
    }

    public static void setPressureItems(List<PressureData> pressureItems) {
        GalleryAdapter.pressureItems = pressureItems;
    }

    public static List<TempData> getTempItems() {
        return tempItems;
    }

    public static void setTempItems(List<TempData> tempItems) {
        GalleryAdapter.tempItems = tempItems;
    }
}