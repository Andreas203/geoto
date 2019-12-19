package com.example.geoto;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.geoto.database.LocationDAO;
import com.example.geoto.database.LocationData;
import com.example.geoto.database.MyRoomDatabase;
import com.example.geoto.database.PathDAO;
import com.example.geoto.database.PathData;
import com.example.geoto.database.PhotoDAO;
import com.example.geoto.database.PhotoData;
import com.example.geoto.database.PressureDAO;
import com.example.geoto.database.PressureData;
import com.example.geoto.database.TempDAO;
import com.example.geoto.database.TempData;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * A class to represent the model of the system data
 */
class Repository extends ViewModel {
    private final PhotoDAO photoDao;
    private final PathDAO pathDao;
    private final LocationDAO locationDao;
    private final PressureDAO pressureDao;
    private final TempDAO tempDao;

    /**
     * A constructor to initialise the room database and repository data
     * @param application
     */
    public Repository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        photoDao = db.photoDao();
        pathDao = db.pathDao();
        locationDao = db.locationDao();
        pressureDao = db.pressureDao();
        tempDao = db.tempDao();
    }

    /**
     * it gets all types of photo data when changed in the db and returns it to the ViewModel
     * @return a list of all the photos in the db
     */
    public LiveData<List<PhotoData>> getAllPhotos() {
        return photoDao.getAllPhotos();
    }
    /**
     * it gets all types of path data when changed in the db and returns it to the ViewModel
     * @return a list of all the paths in the db
     */
    public LiveData<List<PathData>> getAllPaths() {
        return pathDao.getAllPaths();
    }
    /**
     * it gets all locations for one path, when changed in the db and returns it to the ViewModel
     * @parmam startDate the start of the path
     * @parmam endDate the end of the path
     * @return a list of all the locations for a given path
     */
    public LiveData<List<LocationData>> getPathLocations(Date startDate,Date endDate) {return locationDao.getPathLocations(startDate,endDate);}
    /**
     * it gets all types of location data when changed in the db and returns it to the ViewModel
     * @return a list of all the locations in the db
     */
    public LiveData<List<LocationData>> getAllLocations() {
        return locationDao.getAllLocations();
    }
    /**
     * it gets all types of pressure data when changed in the db and returns it to the ViewModel
     * @return a list of all the pressures in the db
     */
    public LiveData<List<PressureData>> getAllPressures() {
        return pressureDao.getAllPressures();
    }
    /**
     * it gets all types of temp data when changed in the db and returns it to the ViewModel
     * @return a list of all the temps in the db
     */
    public LiveData<List<TempData>> getAllTemps() {
        return tempDao.getAllTemps();
    }

    /**
     * Starts an async task to insert a photo in the db
     * @param photo to be inserted
     */
    public void insertPhoto(PhotoData photo) { new insertPhotoAsyncTask(photoDao).execute(photo);    }
    /**
     * Starts an async task to insert a path in the db
     * @param path to be inserted
     */
    public void insertPath(PathData path) { new insertPathAsyncTask(pathDao).execute(path);    }
    /**
     * Starts an async task to insert a location in the db
     * @param location to be inserted
     */
    public void insertLocation(LocationData location) { new insertLocationAsyncTask(locationDao).execute(location);    }
    /**
     * Starts an async task to insert a pressure in the db
     * @param pressure to be inserted
     */
    public void insertPressure(PressureData pressure) { new insertPressureAsyncTask(pressureDao).execute(pressure);    }
    /**
     * Starts an async task to insert a temp in the db
     * @param temp to be inserted
     */
    public void insertTemp(TempData temp) { new insertTempAsyncTask(tempDao).execute(temp);    }

    /**
     * Inserts a photo
     */
    private static class insertPhotoAsyncTask extends AsyncTask<PhotoData, Void, Void> {
        private PhotoDAO mAsyncTaskDao;
        private LiveData<PhotoData> photoData;

        insertPhotoAsyncTask(PhotoDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final PhotoData... params) {
            mAsyncTaskDao.insert(params[0]);
            Log.i("MyRepository", "photo added");
            // you may want to uncomment this to check if numbers have been inserted
            //            int ix=mAsyncTaskDao.howManyElements();
            //            Log.i("TAG", ix+"");
            return null;
        }
    }

    /**
     * Inserts a path
     */
    private static class insertPathAsyncTask extends AsyncTask<PathData, Void, Void> {
        private PathDAO mAsyncTaskDao;
        private LiveData<PathData> pathData;

        insertPathAsyncTask(PathDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final PathData... params) {
            mAsyncTaskDao.insert(params[0]);
            Log.i("MyRepository", "path added");
            // you may want to uncomment this to check if numbers have been inserted
            //            int ix=mAsyncTaskDao.howManyElements();
            //            Log.i("TAG", ix+"");
            return null;
        }
    }

    /**
     * Inserts a location
     */
    private static class insertLocationAsyncTask extends AsyncTask<LocationData, Void, Void> {
        private LocationDAO mAsyncTaskDao;
        private LiveData<LocationData> locationData;

        insertLocationAsyncTask(LocationDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final LocationData... params) {
            mAsyncTaskDao.insert(params[0]);
            Log.i("MyRepository", "location added");
            // you may want to uncomment this to check if numbers have been inserted
            //            int ix=mAsyncTaskDao.howManyElements();
            //            Log.i("TAG", ix+"");
            return null;
        }
    }

    /**
     * Inserts a pressure
     */
    private static class insertPressureAsyncTask extends AsyncTask<PressureData, Void, Void> {
        private PressureDAO mAsyncTaskDao;
        private LiveData<PressureData> pressureData;

        insertPressureAsyncTask(PressureDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final PressureData... params) {
            mAsyncTaskDao.insert(params[0]);
            Log.i("MyRepository", "pressure added");
            // you may want to uncomment this to check if numbers have been inserted
            //            int ix=mAsyncTaskDao.howManyElements();
            //            Log.i("TAG", ix+"");
            return null;
        }
    }

    /**
     * Inserts a temp
     */
    private static class insertTempAsyncTask extends AsyncTask<TempData, Void, Void> {
        private TempDAO mAsyncTaskDao;
        private LiveData<TempData> tempData;

        insertTempAsyncTask(TempDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final TempData... params) {
            mAsyncTaskDao.insert(params[0]);
            Log.i("MyRepository", "temp added");
            // you may want to uncomment this to check if numbers have been inserted
            //            int ix=mAsyncTaskDao.howManyElements();
            //            Log.i("TAG", ix+"");
            return null;
        }
    }
}
