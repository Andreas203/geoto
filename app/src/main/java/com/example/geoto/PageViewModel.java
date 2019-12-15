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

import java.util.List;

public class PageViewModel extends AndroidViewModel {
    private final Repository repository;

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();

    LiveData<List<PhotoData>> photoDataToDisplay;
    LiveData<List<PathData>> pathDataToDisplay;
    LiveData<List<LocationData>> locationDataToDisplay;
    LiveData<List<PressureData>> pressureDataToDisplay;
    LiveData<List<TempData>> tempDataToDisplay;

    public PageViewModel (Application application) {
        super(application);
        // creation and connection to the Repository
        repository = new Repository(application);
        photoDataToDisplay = repository.getAllPhotos();
        pathDataToDisplay = repository.getAllPaths();
        locationDataToDisplay = repository.getAllLocations();
        pressureDataToDisplay = repository.getAllPressures();
        tempDataToDisplay = repository.getAllTemps();
    }

    public void insertPhoto(PhotoData photo) {
        repository.insertPhoto(photo);
    }
    public void insertPath(PathData path) {
        repository.insertPath(path);
    }
    public void insertLocation(LocationData location) {
        repository.insertLocation(location);
    }
    public void insertPressure(PressureData pressure) {
        repository.insertPressure(pressure);
    }
    public void insertTemp(TempData temp) {
        repository.insertTemp(temp);
    }

    public LiveData<List<PhotoData>> getPhotoDataToDisplay() {
        if (photoDataToDisplay == null) {
            photoDataToDisplay = new MutableLiveData<List<PhotoData>>();
        }
        return photoDataToDisplay;
    }

    public LiveData<List<PathData>> getPathDataToDisplay() {
        if (pathDataToDisplay == null) {
            pathDataToDisplay = new MutableLiveData<List<PathData>>();
        }
        return pathDataToDisplay;
    }

    public LiveData<List<LocationData>> getLocationDataToDisplay() {
        if (locationDataToDisplay == null) {
            locationDataToDisplay = new MutableLiveData<List<LocationData>>();
        }
        return locationDataToDisplay;
    }

    public LiveData<List<PressureData>> getPressureDataToDisplay() {
        if (pressureDataToDisplay == null) {
            pressureDataToDisplay = new MutableLiveData<List<PressureData>>();
        }
        return pressureDataToDisplay;
    }

    public LiveData<List<TempData>> getTempDataToDisplay() {
        if (tempDataToDisplay == null) {
            tempDataToDisplay = new MutableLiveData<List<TempData>>();
        }
        return tempDataToDisplay;
    }

    public void setIndex(int index) {
        mIndex.setValue(index);
    }
}