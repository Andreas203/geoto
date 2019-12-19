package com.example.geoto;

import android.app.Application;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.AndroidViewModel;

import com.example.geoto.database.LocationData;
import com.example.geoto.database.PathData;
import com.example.geoto.database.PhotoData;
import com.example.geoto.database.PressureData;
import com.example.geoto.database.TempData;

import java.util.Date;
import java.util.List;

/**
 * Represents the data we want to display in our activity
 */
public class PageViewModel extends AndroidViewModel {
    private final Repository repository;
    private Date startDate;
    private  Date endDate;

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();

    // Live data lists to observe from our repository
    LiveData<List<PhotoData>> photoDataToDisplay;
    LiveData<List<PathData>> pathDataToDisplay;
    LiveData<List<LocationData>> locationDataToDisplay;
    LiveData<List<LocationData>> pathLocationToDisplay;
    LiveData<List<PressureData>> pressureDataToDisplay;
    LiveData<List<TempData>> tempDataToDisplay;

    /**
     * The constructor which initialises all of our data items
     * @param application out application instance
     */
    public PageViewModel (Application application) {
        super(application);
        // creation and connection to the Repository
        repository = new Repository(application);
        photoDataToDisplay = repository.getAllPhotos();
        pathDataToDisplay = repository.getAllPaths();
        locationDataToDisplay = repository.getAllLocations();
        pressureDataToDisplay = repository.getAllPressures();
        tempDataToDisplay = repository.getAllTemps();


        pathLocationToDisplay = repository.getPathLocations(startDate,endDate);
    }

    /**
     * Tells the repository to insert a photo
     * @param photo to insert
     */
    public void insertPhoto(PhotoData photo) {
        repository.insertPhoto(photo);
    }
    /**
     * Tells the repository to insert a path
     * @param path to insert
     */
    public void insertPath(PathData path) {
        repository.insertPath(path);
    }
    /**
     * Tells the repository to insert a location
     * @param location to insert
     */
    public void insertLocation(LocationData location) {
        repository.insertLocation(location);
    }
    /**
     * Tells the repository to insert a pressure
     * @param pressure to insert
     */
    public void insertPressure(PressureData pressure) {
        repository.insertPressure(pressure);
    }
    /**
     * Tells the repository to insert a temp
     * @param temp to insert
     */
    public void insertTemp(TempData temp) {
        repository.insertTemp(temp);
    }

    /**
     * Calls the repository for the current photo data
     * @return list of photoData
     */
    public LiveData<List<PhotoData>> getPhotoDataToDisplay() {
        if (photoDataToDisplay == null) {
            photoDataToDisplay = new MutableLiveData<List<PhotoData>>();
        }
        return photoDataToDisplay;
    }
    /**
     * Calls the repository for the current path data
     * @return list of pathData
     */
    public LiveData<List<PathData>> getPathDataToDisplay() {
        if (pathDataToDisplay == null) {
            pathDataToDisplay = new MutableLiveData<List<PathData>>();
        }
        return pathDataToDisplay;
    }
    /**
     * Calls the repository for the current location data
     * @return list of locationData
     */
    public LiveData<List<LocationData>> getLocationDataToDisplay() {
        if (locationDataToDisplay == null) {
            locationDataToDisplay = new MutableLiveData<List<LocationData>>();
        }
        return locationDataToDisplay;
    }
    /**
     * Calls the repository for the current path location data
     * @return list of locationData
     */
    public LiveData<List<LocationData>> getPathLocationToDisplay(Date startDate, Date endDate) {
        if (pathLocationToDisplay == null) {
            pathLocationToDisplay = new MutableLiveData<List<LocationData>>();
        }
        return pathLocationToDisplay;
    }
    /**
     * Calls the repository for the current pressure data
     * @return list of pressureData
     */
    public LiveData<List<PressureData>> getPressureDataToDisplay() {
        if (pressureDataToDisplay == null) {
            pressureDataToDisplay = new MutableLiveData<List<PressureData>>();
        }
        return pressureDataToDisplay;
    }
    /**
     * Calls the repository for the current temp data
     * @return list of tempData
     */
    public LiveData<List<TempData>> getTempDataToDisplay() {
        if (tempDataToDisplay == null) {
            tempDataToDisplay = new MutableLiveData<List<TempData>>();
        }
        return tempDataToDisplay;
    }

    /**
     * Set the current index value
     */
    public void setIndex(int index) {
        mIndex.setValue(index);
    }
}