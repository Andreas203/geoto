package com.example.geoto.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

/**
 * A class to represent the location objects we store
 */
@Entity()
@TypeConverters({Converters.class})
public class LocationData {
    @PrimaryKey(autoGenerate = true)
    @androidx.annotation.NonNull
    private int locationId=0;
    private double latitude;
    private double longitude;
    private float accuracy;
    private Date date;

    /**
     * A constructor to initialise the location data
     * @param latitude of the location
     * @param longitude of the location
     * @param accuracy of the location position
     * @param date of the location measurement
     */
    public LocationData(double latitude, double longitude, float accuracy, Date date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.date = date;
    }

    /**
     * @return the location id
     */
    @androidx.annotation.NonNull
    public int getLocationId() {
        return locationId;
    }

    /**
     * Set the location id
     * @param locationId
     */
    public void setLocationId(@androidx.annotation.NonNull int locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the location latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Set the location latitude
     * @param latitude
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the location longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Set the location longitude
     * @param longitude
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the location accuracy
     */
    public float getAccuracy() {
        return accuracy;
    }

    /**
     * Set the location accuracy
     * @param accuracy
     */
    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * @return the location date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the location date
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
