package com.example.geoto.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * An interface to access the pressures in the db
 */
@Dao
public interface PressureDAO {
    @Insert
    void insertAll(PressureData... pressureData);

    @Insert
    void insert(PressureData pressureData);

    @Delete
    void delete(PressureData pressureData);

    // it selects a random element
    @Query("SELECT * FROM pressureData")
    LiveData<List<PressureData>> getAllPressures();

    @Delete
    void deleteAll(PressureData... pressureData);

    @Query("SELECT COUNT(*) FROM pressureData")
    int howManyElements();
}
