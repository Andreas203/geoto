package com.example.geoto.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@androidx.room.Database(entities = {PhotoData.class, PathData.class, LocationData.class, TempData.class, PressureData.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MyRoomDatabase extends RoomDatabase {
    public abstract PhotoDAO photoDao();
    public abstract PathDAO pathDao();
    public abstract LocationDAO locationDao();
    public abstract TempDAO tempDao();
    public abstract PressureDAO pressureDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile MyRoomDatabase INSTANCE;

    public static MyRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = androidx.room.Room.databaseBuilder(context.getApplicationContext(),
                            MyRoomDatabase.class, "database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     *
     * If you want to populate the database only when the database is created for the 1st time,
     * override MyRoomDatabase.Callback()#onCreate
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // do any init operation about any initialisation here
        }
    };

}