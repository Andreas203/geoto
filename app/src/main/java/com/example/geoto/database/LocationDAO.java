package com.example.geoto.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationDAO {
    @Insert
    void insertAll(LocationData... locationData);

    @Insert
    void insert(LocationData locationData);

    @Delete
    void delete(LocationData locationData);

    // it selects a random element
    @Query("SELECT * FROM locationData")
    LiveData<List<LocationData>> getAllLocations();

    @Delete
    void deleteAll(LocationData... locationData);

    @Query("SELECT COUNT(*) FROM locationData")
    int howManyElements();
}
