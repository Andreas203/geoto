package com.example.geoto.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhotoDAO {
    @Insert
    void insertAll(PhotoData... photoData);

    @Insert
    void insert(PhotoData photoData);

    @Delete
    void delete(PhotoData photoData);

    // it selects a random element
    @Query("SELECT * FROM photoData")
    LiveData<List<PhotoData>> getAllPhotos();

    @Delete
    void deleteAll(PhotoData... photoData);

    @Query("SELECT COUNT(*) FROM photoData")
    int howManyElements();
}
