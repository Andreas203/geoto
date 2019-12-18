package com.example.geoto;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoto.database.LocationData;
import com.example.geoto.database.PathData;
import com.example.geoto.database.PhotoData;
import com.example.geoto.database.PressureData;
import com.example.geoto.database.TempData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class GalleryFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;

    private FloatingActionButton fab_sort_images;
    private View frame_layout_for_sort;
    private FloatingActionButton backButton;

    private int sort = 1;


    private static final int REQUEST_READ_EXTERNAL_STORAGE = 2987;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 7829;


    public static GalleryFragment newInstance(int index) {
        GalleryFragment fragment = new GalleryFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.gallery_main, container, false);

        // New stuff
        final RecyclerView mRecyclerView = root.findViewById(R.id.grid_recycler_view);
        final TextView emptyView = root.findViewById(R.id.empty_view);
        int numberOfColumns = 3;
        mRecyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), numberOfColumns));
        mAdapter = new GalleryAdapter();
        mRecyclerView.setAdapter(mAdapter);

        pageViewModel.getPhotoDataToDisplay().observe(this, new Observer<List<PhotoData>>(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(@Nullable final List<PhotoData> myPictureList) {
                if (myPictureList.isEmpty()) {
                    mRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
                mAdapter.setPhotoItems(myPictureList);
                mAdapter.sortGallery(sort);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(myPictureList.size() - 1);
            }});

        pageViewModel.getPathDataToDisplay().observe(this, new Observer<List<PathData>>(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(@Nullable final List<PathData> myPathList) {
                mAdapter.setPathItems(myPathList);
                mAdapter.notifyDataSetChanged();
            }});

        pageViewModel.getLocationDataToDisplay().observe(this, new Observer<List<LocationData>>(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(@Nullable final List<LocationData> myLocationList) {
                mAdapter.setLocationItems(myLocationList);
                mAdapter.notifyDataSetChanged();
            }});

        pageViewModel.getPressureDataToDisplay().observe(this, new Observer<List<PressureData>>(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(@Nullable final List<PressureData> myPressureList) {
                mAdapter.setPressureItems(myPressureList);
                mAdapter.notifyDataSetChanged();
            }});

        pageViewModel.getTempDataToDisplay().observe(this, new Observer<List<TempData>>(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(@Nullable final List<TempData> myTempList) {
                mAdapter.setTempItems(myTempList);
                mAdapter.notifyDataSetChanged();
            }});

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.menu_main, menu);
        //hide item (sort)
        menu.findItem(R.id.action_sort).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            //do your function here
////            Toast.makeText(getActivity(), "Settings", Toast.LENGTH_SHORT).show();
////            Toast.makeText(getContext(), "The settings button has been pressed!", Toast.LENGTH_SHORT).show();
//        }

        if (id == R.id.action_sort) {
//            Toast.makeText(getContext(), "The sort button has been pressed!", Toast.LENGTH_SHORT).show();
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Choose a sorting style");
            // add a radio button list
            String[] animals = {"Date: Old to New", "Date: New to Old"};
            final int checkedItem = sort; // cow
            builder.setSingleChoiceItems(animals, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sort = which;
                }
            });

            // add OK and Cancel buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // user clicked OK
                    mAdapter.sortGallery(sort);
                    mAdapter.notifyDataSetChanged();

                }
            });
            builder.setNegativeButton("Cancel", null);
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        return super.onOptionsItemSelected(item);
    }
    
}
