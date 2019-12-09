package com.example.geoto.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PathDAO {
    @Insert
    void insertAll(PathData... pathData);

    @Insert
    void insert(PathData pathData);

    @Delete
    void delete(PathData pathData);

    // it selects a random element
    @Query("SELECT * FROM pathData")
    LiveData<PathData> getAllPaths();

    @Delete
    void deleteAll(PathData... pathData);

    @Query("SELECT COUNT(*) FROM pathData")
    int howManyElements();
}
