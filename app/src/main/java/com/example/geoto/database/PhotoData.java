package com.example.geoto.database;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.sql.Blob;
import java.util.Date;

@Entity()
@TypeConverters({Converters.class})
public class PhotoData {
    @PrimaryKey(autoGenerate = true)
    @androidx.annotation.NonNull
    private int id=0;
    private String absolutePath;
    private Date date;


    public PhotoData(String absolutePath, Date date) {
        this.absolutePath = absolutePath;
        this.date = date;
        //this.blob = createBlob(picture);
    }

    @androidx.annotation.NonNull
    public int getId() {
        return id;
    }
    public void setId(@androidx.annotation.NonNull int id) {
        this.id = id;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }
    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

//    private Blob createBlob(Bitmap picture) {
//
//        return newBlob;
//    }
}
