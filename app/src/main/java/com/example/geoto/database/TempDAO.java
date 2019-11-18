package com.example.geoto.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TempDAO {
    @Insert
    void insertAll(TempData... tempData);

    @Insert
    void insert(TempData tempData);

    @Delete
    void delete(TempData tempData);

    // it selects a random element
    @Query("SELECT * FROM tempData ORDER BY RANDOM() LIMIT 1")
    LiveData<TempData> retrieveOneTemp();

    @Delete
    void deleteAll(TempData... tempData);

    @Query("SELECT COUNT(*) FROM tempData")
    int howManyElements();
}
