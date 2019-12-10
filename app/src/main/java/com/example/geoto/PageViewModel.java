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

public class PageViewModel extends AndroidViewModel {
    private final Repository repository;

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();

    LiveData<PhotoData> photoDataToDisplay;
    LiveData<PathData> pathDataToDisplay;
    LiveData<LocationData> locationDataToDisplay;
    LiveData<PressureData> pressureDataToDisplay;
    LiveData<TempData> tempDataToDisplay;

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

    public void setIndex(int index) {
        mIndex.setValue(index);
    }
}