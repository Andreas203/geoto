package com.example.geoto.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PressureDAO {
    @Insert
    void insertAll(PressureData... pressureData);

    @Insert
    void insert(PressureData pressureData);

    @Delete
    void delete(PressureData pressureData);

    // it selects a random element
    @Query("SELECT * FROM pressureData ORDER BY RANDOM() LIMIT 1")
    LiveData<PressureData> retrieveOnePressure();

    @Delete
    void deleteAll(PressureData... pressureData);

    @Query("SELECT COUNT(*) FROM pressureData")
    int howManyElements();
}
