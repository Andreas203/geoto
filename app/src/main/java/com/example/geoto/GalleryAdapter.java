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
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.geoto.database.LocationData;
import com.example.geoto.database.PathData;
import com.example.geoto.database.PhotoData;
import com.example.geoto.database.PressureData;
import com.example.geoto.database.TempData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class to define how the gallery displays a list of photo data
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.View_Holder> {
    static private Context context;
    private static List<PhotoData> photoItems;
    private static List<PathData> pathItems;
    private static List<LocationData> locationItems;
    private static List<PressureData> pressureItems;
    private static List<TempData> tempItems;

    /**
     * A constructor to initialise the adapter items
     */
    public GalleryAdapter() {
        super();
        photoItems = new ArrayList<>();
        pathItems = new ArrayList<>();
        locationItems = new ArrayList<>();
        pressureItems = new ArrayList<>();
        tempItems = new ArrayList<>();
    }

    /**
     * Assigns a view to the view holder
     * @param parent the parent view
     * @param viewType
     * @return the updated holder
     */
    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image,
                parent, false);
        View_Holder holder = new View_Holder(v);
        context = parent.getContext();

        return holder;
    }

    /**
     * Populates the view with adapted photo items
     * @param holder a single photo holder
     * @param position the position in the grid
     */
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


    /**
     * Convenience method for getting data at click position
     */
    PhotoData getItem(int id) {
        return photoItems.get(id);
    }

    /**
     * Get the number of photos displayed
     * @return the number of photos
     */
    @Override
    public int getItemCount() {
        return photoItems.size();
    }

    /**
     * An inner class to represent each displayed photo
     */
    public class View_Holder extends RecyclerView.ViewHolder  {
        ImageView imageView;

        View_Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_item);

        }
    }

    /**
     * Sorts the photo items based on date
     * @param sortCode the direction of the sort
     */
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

    /**
     * Returns the photo data list
     * @return photoItems
     */
    public static List<PhotoData> getPhotoItems() {
        return photoItems;
    }

    /**
     * Sets the photo data list
     * @param photoItems
     */
    public static void setPhotoItems(List<PhotoData> photoItems) {
        GalleryAdapter.photoItems = photoItems;
    }

    /**
     * Returns the path data list
     * @return pathItems
     */
    public static List<PathData> getPathItems() {
        return pathItems;
    }
    /**
     * Sets the path data list
     * @param pathItems
     */
    public static void setPathItems(List<PathData> pathItems) {
        GalleryAdapter.pathItems = pathItems;
    }

    /**
     * Returns the location data list
     * @return locationItems
     */
    public static List<LocationData> getLocationItems() {
        return locationItems;
    }
    /**
     * Sets the location data list
     * @param locationItems
     */
    public static void setLocationItems(List<LocationData> locationItems) {
        GalleryAdapter.locationItems = locationItems;
    }

    /**
     * Returns the pressure data list
     * @return pressureItems
     */
    public static List<PressureData> getPressureItems() {
        return pressureItems;
    }
    /**
     * Sets the pressure data list
     * @param pressureItems
     */
    public static void setPressureItems(List<PressureData> pressureItems) {
        GalleryAdapter.pressureItems = pressureItems;
    }

    /**
     * Returns the temp data list
     * @return tempItems
     */
    public static List<TempData> getTempItems() {
        return tempItems;
    }
    /**
     * Sets the temp data items
     * @param tempItems
     */
    public static void setTempItems(List<TempData> tempItems) {
        GalleryAdapter.tempItems = tempItems;
    }
}