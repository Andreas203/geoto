package com.example.geoto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.geoto.database.LocationData;
import com.example.geoto.database.PathData;
import com.example.geoto.database.PhotoData;
import com.example.geoto.database.PressureData;
import com.example.geoto.database.TempData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewVisitFragment extends Fragment implements OnMapReadyCallback {

    Bitmap.Config conf = Bitmap.Config.ARGB_8888;
    Bitmap bmp = Bitmap.createBitmap(80, 80, conf);

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int ACCESS_FINE_LOCATION = 123;

    private PageViewModel pageViewModel;
    private MapView mapView;
    private GoogleMap googleMap;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private Polyline line;

    private ExtendedFloatingActionButton mButtonStart;
    private ExtendedFloatingActionButton mButtonEnd;
    private FloatingActionButton fabCamera;
    private FloatingActionButton fabGallery;

    private Barometer barometer;
    private Thermometer thermometer;

    private LatLng place1 = null;
    private LatLng place2 = null;
    private Date startDate;
    private Date endDate;


    public static NewVisitFragment newInstance(int index) {
        NewVisitFragment fragment = new NewVisitFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void storePressure(float pressure, Date date) {

        PressureData pressureData = new PressureData(pressure, date);
        pageViewModel.insertPressure(pressureData);
    }

    public void storeTemp(float temp, Date date) {

        TempData tempData = new TempData(temp, date);
        pageViewModel.insertTemp(tempData);
    }

    public void storeLocation(Location location, Date date) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        float acc = location.getAccuracy();


        LocationData locationData = new LocationData(lat, lon, acc, date);
        pageViewModel.insertLocation(locationData);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }



    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            return;
        }

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null /* Looper */);
    }


    /**
     * it stops the location updates
     */
    private void stopLocationUpdates(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
//        startLocationUpdates();
//        barometer.startSensingPressure();
//        thermometer.startSensingTemperature();
    }


    private Location mCurrentLocation;
    private String mLastUpdateTime;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            mCurrentLocation = locationResult.getLastLocation();
            Date date = new Date();
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            Log.i("MAP", "new location " + mCurrentLocation.toString());
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
                if (mCurrentLocation != null) {
                    storeLocation(mCurrentLocation, date);
                }
        }
    };


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, null /* Looper */);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_visit_main, container, false);

        barometer = new Barometer(getContext(), this);
        thermometer = new Thermometer(getContext(), this);

        fabCamera = (FloatingActionButton) root.findViewById(R.id.fab_camera);
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openCamera(getActivity(), 0);
            }
        });
        fabCamera.hide();

        fabGallery = (FloatingActionButton) root.findViewById(R.id.fab_gallery);
        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openGallery(getActivity(), 0);
            }
        });
        fabGallery.hide();

        mButtonStart = (ExtendedFloatingActionButton) root.findViewById(R.id.button_start);

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "New visit started!", Toast.LENGTH_SHORT).show();
                startDate = new Date();
                startLocationUpdates();
                googleMap.setMyLocationEnabled(true);
                barometer.startSensingPressure();
                thermometer.startSensingTemperature();

                if (mButtonEnd != null)
                    mButtonEnd.setEnabled(true);
                mButtonStart.setEnabled(false);
                mButtonStart.hide();
                mButtonEnd.show();
                fabCamera.show();
                fabGallery.show();
            }
        });
        mButtonStart.setEnabled(true);


        mButtonEnd = (ExtendedFloatingActionButton) root.findViewById(R.id.button_end);
        mButtonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // GET A TITLE AND DESCRIPTION OF THE PATH FROM THE USER ////////////////////////////
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Path Information");
                alertDialog.setMessage("Enter Path Information");

                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                // Add a TextView here for the "Title" label, as noted in the comments
                final EditText titleBox = new EditText(getContext());
                titleBox.setHint("Title");
                layout.addView(titleBox); // Notice this is an add method

                // Add another TextView here for the "Description" label
                final EditText descriptionBox = new EditText(getContext());
                descriptionBox.setHint("Description");
                layout.addView(descriptionBox); // Another add method

                alertDialog.setView(layout);

                alertDialog.setPositiveButton("Create",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String pathTitle = titleBox.getText().toString();
                                String pathDescr = descriptionBox.getText().toString();
                                PathData pathData = new PathData(pathTitle, startDate, endDate, pathDescr);
                                pageViewModel.insertPath(pathData);

                                Toast.makeText(getContext(), "Visit stopped!", Toast.LENGTH_SHORT).show();
                                stopLocationUpdates();
                                // Removes all markers, overlays, and polylines from the map.
                                googleMap.clear();
                                place1 = null;

                                barometer.stopBarometer();
                                thermometer.stopThermometer();
                                endDate = new Date();
                                if (mButtonStart != null)
                                    mButtonStart.setEnabled(true);
                                mButtonEnd.setEnabled(false);
                                mButtonEnd.hide();
                                mButtonStart.show();
                                fabCamera.hide();
                                fabGallery.hide();
                            }
                        });

                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                onResume();
                            }
                        });

                alertDialog.show();
                //Log.d("title",pathTitle);

            }
        });
        mButtonEnd.hide();
        mButtonEnd.setEnabled(false);

        initEasyImage();

        return root;
    }

    private void initEasyImage() {
        EasyImage.configuration(getContext())
                .setImagesFolderName("EasyImage sample")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(true);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_sort).setVisible(false);
    }

    public void  setMarkerImg(PhotoData photoData){
        Bitmap bmp = BitmapFactory.decodeFile(photoData.getAbsolutePath());
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                .title(mLastUpdateTime)
                .icon(BitmapDescriptorFactory.fromBitmap(bmp)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //do your function here
            Toast.makeText(getActivity(), "Settings", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }
}