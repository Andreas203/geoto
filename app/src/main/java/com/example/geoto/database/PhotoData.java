package com.example.geoto.database;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.sql.Blob;
import java.util.Date;

/**
 * A class to represent the photo objects we store
 */
@Entity()
@TypeConverters({Converters.class})
public class PhotoData implements Comparable<PhotoData> {
    @PrimaryKey(autoGenerate = true)
    @androidx.annotation.NonNull
    private int id=0;
    private String absolutePath;
    private Date date;

    /**
     * A constructor to initialise the photo's absolute path and date taken
     * @param absolutePath
     * @param date
     */
    public PhotoData(String absolutePath, Date date) {
        this.absolutePath = absolutePath;
        this.date = date;
    }

    /**
     * Returns the photo id
     * @return id
     */
    @androidx.annotation.NonNull
    public int getId() {
        return id;
    }

    /**
     * Sets the photo id
     * @param id the photo id
     */
    public void setId(@androidx.annotation.NonNull int id) {
        this.id = id;
    }

    /**
     * Return the photo's absolute path
     * @return the absolute path
     */
    public String getAbsolutePath() {
        return absolutePath;
    }

    /**
     * Sets the photo's absolute path
     * @param absolutePath the absolute path of the photo
     */
    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    /**
     * Returns the photo date
     * @return date of the photo
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the photo date
     * @param date the date of the photo
     */
    public void setDate(Date date) {
        this.date = date;
    }


    /**
     * Compares two photo objects by their date
     * @param o a photo to compare to
     * @return a flag for each compare case
     */
    @Override
    public int compareTo(PhotoData o) {
        if (date.compareTo(o.getDate()) > 0) {
            return 1;
        } else if (date.compareTo(o.getDate()) <0) {
            return -1;
        } else {
            return 0;
        }
    }
}
