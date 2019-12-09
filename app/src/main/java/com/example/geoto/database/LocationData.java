package com.example.geoto.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity()
@TypeConverters({Converters.class})
public class LocationData {
    @PrimaryKey(autoGenerate = true)
    @androidx.annotation.NonNull
    private int locationId=0;
    private float latitude;
    private float longitude;
    private float accuracy;
    private Date date;

    public LocationData(float latitude, float longitude, float accuracy, Date date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.date = date;
    }

    @androidx.annotation.NonNull
    public int getLocationId() {
        return locationId;
    }
    public void setLocationId(@androidx.annotation.NonNull int locationId) {
        this.locationId = locationId;
    }

    public float getLatitude() {
        return latitude;
    }
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getAccuracy() {
        return accuracy;
    }
    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
