package com.example.geoto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.geoto.database.LocationData;
import com.example.geoto.database.PathData;
import com.example.geoto.database.PhotoData;
import com.example.geoto.database.PressureData;
import com.example.geoto.database.TempData;
import com.google.android.gms.common.api.ApiException;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Fragment where new visits are started and current path is displayed
 * Photos can be taken and are placed on the path
 * Photos can be uploaded onto the path
 */
public class NewVisitFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private PendingIntent mLocationPendingIntent;

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int ACCESS_FINE_LOCATION = 123;

    private static PageViewModel pageViewModel;
    private MapView mapView;
    private static GoogleMap googleMap;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;

    private ExtendedFloatingActionButton mButtonStart;
    private static ExtendedFloatingActionButton mButtonEnd;
    private FloatingActionButton fabCamera;
    private FloatingActionButton fabGallery;

    private Barometer barometer;
    private Thermometer thermometer;

    private LatLng place1 = null;
    private LatLng place2 = null;

    private static LatLng placeOld = null;

    private Date startDate;
    private Date endDate;
    private static FragmentActivity activity;

    /**
     * Creates NewVisitFragment instance
     * @param index where the fragment should be placed
     * @return the fragment
     */
    public static NewVisitFragment newInstance(int index) {
        NewVisitFragment fragment = new NewVisitFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Run when the fragment is created from MainView
     * adds the fragment to the View Model
     * @param savedInstanceState get the instance of the fragments
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        setActivity(getActivity());
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    /**
     * Creates view and initialises sensors and buttons
     * Main onclick events are all defined here
     * @param inflater inflate the view to fill app
     * @param container the container containing the fragment
     * @param savedInstanceState contains the current state of the application
     * @return the root containing the inflated view and container
     */
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

                startDate = new Date();

                barometer.startSensingPressure();
                thermometer.startSensingTemperature();

                if (mButtonEnd != null)
                    mButtonEnd.setEnabled(true);
                mButtonStart.setEnabled(false);
                startLocationUpdates(getContext());
                mButtonStart.hide();
                mButtonEnd.show();

                fabGallery.show();

                //Only show camera icon if phone has camera
                if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                    // this device has a camera
                    fabCamera.show();
                }
            }
        });
        mButtonStart.setEnabled(true);


        mButtonEnd = (ExtendedFloatingActionButton) root.findViewById(R.id.button_end);
        mButtonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // GET A TITLE AND DESCRIPTION OF THE PATH FROM THE USER
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Visit details");
//                alertDialog.setMessage("Enter Path Information");

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

                //logic for when create is clicked
                alertDialog.setPositiveButton("Create",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String pathTitle = titleBox.getText().toString();
                                String pathDescr = descriptionBox.getText().toString();
                                endDate = new Date();

                                PathData pathData = new PathData(pathTitle, startDate, endDate, pathDescr);
                                pageViewModel.insertPath(pathData);

                                //stop location updates when create is clicked
                                stopLocationUpdates();

                                // Removes all markers, overlays, and polylines from the map.
                                googleMap.clear();
                                place1 = null;

                                barometer.stopBarometer();
                                thermometer.stopThermometer();


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

    /**
     * Initialise map after view is created
     * @param view the current  view displayed to the user
     * @param savedInstanceState contains the current state of the application
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment
    }

    /**
     * Retrieves previous location
     * @return previous location
     */
    public static LatLng getPlaceOld(){
        return placeOld;
    }

    /**
     * Sets the previous location retrieved
     * @param oldLocation the location before the current one
     */
    public static void setPlaceOld(LatLng oldLocation){
        placeOld = oldLocation;
    }

    /**
     * Gets the current activity of the fragment
     * @return static activity class variable
     */
    public static FragmentActivity getFragActivity() {
        return activity;
    }

    /**
     * Store static class Activity variable
     * @param activity set activity of the fragment
     */
    public static void setActivity(FragmentActivity activity) {
        NewVisitFragment.activity = activity;
    }

    /**
     * get map instance displayed on page
     * @return static googleMap to services
     */
    public static GoogleMap getMap() {
        return googleMap;
    }

    /**
     * Stores pressure and date in the database
     * @param pressure retrieved from the barometer
     * @param date store current date and time
     */
    public void storePressure(float pressure, Date date) {

        PressureData pressureData = new PressureData(pressure, date);
        pageViewModel.insertPressure(pressureData);
    }

    /**
     * Store temperature and date in the database
     * @param temp retrieved from the thermometer
     * @param date store current date and time
     */
    public void storeTemp(float temp, Date date) {

        TempData tempData = new TempData(temp, date);
        pageViewModel.insertTemp(tempData);
    }

    /**
     * Store location and date in the database
     * @param location retrieved from fusedLocation and split into latitude and longitude
     * @param date store current date and time
     */
    public static void storeLocation(Location location, Date date) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        float acc = location.getAccuracy();


        LocationData locationData = new LocationData(lat, lon, acc, date);
        pageViewModel.insertLocation(locationData);

    }

    /**
     * Stops the location updates
     */
    private void stopLocationUpdates(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    /**
     * Run the the app is resumed and gets a location request
     */
    @Override
    public void onResume() {
        super.onResume();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

    }

    private Location mCurrentLocation;
    private String mLastUpdateTime;

    /**
     * Callback for getting the location and plotting on map
     * If intent succeeds it will not run
     */
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
            googleMap.setMyLocationEnabled(true);
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

    /**
     * Starts the location updates and calls service to continue getting locations
     * @param context uses current application context to pass to  intent
     */
    private void startLocationUpdates(Context context) {
        final Intent intent = new Intent(context, LocationService.class);
        mLocationPendingIntent = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Void> locationTask = mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationPendingIntent);
            if (locationTask != null) {
                locationTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            Log.w("MapsActivity", ((ApiException) e).getStatusMessage());
                        } else {
                            Log.w("MapsActivity", e.getMessage());
                        }
                    }
                });

                locationTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("MapsActivity", "restarting gps successful!");
                    }
                });
            }
        }
    }

    /**
     * Check end button state to see if plotting still needs to occur
     * @return end button state
     */
    static Boolean getButtonState(){
        return mButtonEnd.isEnabled();
    }

    /**
     * Handles the permission request
     * @param requestCode the request coming in
     * @param permissions list of permission to check
     * @param grantResults results from permission check
     */
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

    /**
     * Initialise the EasyImage configuration
     */
    private void initEasyImage() {
        EasyImage.configuration(getContext())
                .setImagesFolderName("EasyImage sample")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(true);
    }

    /**
     * Check if the map is ready and assign it
     * Set up a listener for the markers
     * @param map
     */
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMyLocationEnabled(true);
    }

    /**
     * Option menu at the top right
     * @param menu drop down menu
     * @param inflater inflater making view fill the application screen
     *
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_sort).setVisible(false);
    }

    /**
     * Reacts if options are clicked
     * @param item items to be placed into the list
     * @return the items in the list
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            //do your function here
//            Toast.makeText(getActivity(), "Settings", Toast.LENGTH_SHORT).show();
//        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles EasyImage camera activity results when it finishes
     * @param requestCode the request code
     * @param resultCode the result code
     * @param data the data received from the activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                onPhotosReturned(imageFiles);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getActivity());
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    /**
     * add to the database
     * @param returnedPhotos
     */
    private void onPhotosReturned(List<File> returnedPhotos) {
        for (int i = 0; i < returnedPhotos.size(); i++) {
            String path = returnedPhotos.get(i).getAbsolutePath();
            Date date = new Date();

            PhotoData photo = new PhotoData(path, date);
            pageViewModel.insertPhoto(photo);

            if (i==0) {
                Drawable icon = getResources().getDrawable(R.drawable.ic_insert_photo_blue_24dp);
                Canvas canvas = new Canvas();
                Bitmap bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                canvas.setBitmap(bitmap);
                icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                icon.draw(canvas);

                Marker photoMarker = googleMap.addMarker(new MarkerOptions()
                        .position(getPlaceOld())
                        .title(mLastUpdateTime)
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

                photoMarker.setTag(path);
            }
        }

    }

    /**
     * When image marker is clicked so the image
     * @param marker the specific marker to show image for
     * @return
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        System.out.println("GOT TO 0");
        if (marker.getTag() != null) {
            System.out.println("GOT TO 1");
            String absolutePath = (String) marker.getTag();

            Intent intent = new Intent(getContext(), ShowBigImageActivity.class);
            intent.putExtra("path", absolutePath);
            this.startActivity(intent);

            return true;
        } else {
            return false;
        }

    }
}