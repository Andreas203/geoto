package com.example.geoto.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TempDAO {
    @Insert
    void insertAll(TempData... tempData);

    @Insert
    void insert(TempData tempData);

    @Delete
    void delete(TempData tempData);

    // it selects a random element
    @Query("SELECT * FROM tempData")
    LiveData<List<TempData>> getAllTemps();

    @Delete
    void deleteAll(TempData... tempData);

    @Query("SELECT COUNT(*) FROM tempData")
    int howManyElements();
}
