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

import java.util.List;
import java.util.Random;

class Repository extends ViewModel {
    private final PhotoDAO photoDao;
    private final PathDAO pathDao;
    private final LocationDAO locationDao;
    private final PressureDAO pressureDao;
    private final TempDAO tempDao;

    public Repository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        photoDao = db.photoDao();
        pathDao = db.pathDao();
        locationDao = db.locationDao();
        pressureDao = db.pressureDao();
        tempDao = db.tempDao();
    }

    /**
     * it gets all types of data when changed in the db and returns it to the ViewModel
     * @return
     */
    public LiveData<List<PhotoData>> getAllPhotos() {
        return photoDao.getAllPhotos();
    }
    public LiveData<List<PathData>> getAllPaths() {
        return pathDao.getAllPaths();
    }
    public LiveData<List<LocationData>> getAllLocations() {
        return locationDao.getAllLocations();
    }
    public LiveData<List<PressureData>> getAllPressures() {
        return pressureDao.getAllPressures();
    }
    public LiveData<List<TempData>> getAllTemps() {
        return tempDao.getAllTemps();
    }

    public void insertPhoto(PhotoData photo) { new insertPhotoAsyncTask(photoDao).execute(photo);    }
    public void insertPath(PathData path) { new insertPathAsyncTask(pathDao).execute(path);    }
    public void insertLocation(LocationData location) { new insertLocationAsyncTask(locationDao).execute(location);    }
    public void insertPressure(PressureData pressure) { new insertPressureAsyncTask(pressureDao).execute(pressure);    }
    public void insertTemp(TempData temp) { new insertTempAsyncTask(tempDao).execute(temp);    }

    // Insert photo
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

    // Insert path
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

    // Insert location
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

    // Insert pressure
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

    // Insert temp
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
