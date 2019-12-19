package com.example.geoto.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

/**
 * A class to represent the temperature objects we store
 */
@Entity()
@TypeConverters({Converters.class})
public class TempData {
    @PrimaryKey(autoGenerate = true)
    @androidx.annotation.NonNull
    private int tempId=0;
    private float temp;
    private Date date;

    /**
     * A constructor to initialise the temperature data
     * @param temp the temperature reading
     * @param date the measurement date
     */
    public TempData(float temp, Date date) {
        this.temp = temp;
        this.date = date;
    }

    /**
     * @return the temp id
     */
    @androidx.annotation.NonNull
    public int getTempId() {
        return tempId;
    }

    /**
     * Sets the temp id
     * @param tempId
     */
    public void setTempId(@androidx.annotation.NonNull int tempId) {
        this.tempId = tempId;
    }

    /**
     * @return the temp reading
     */
    public float getTemp() {
        return temp;
    }

    /**
     * Set the temp reading
     * @param temp
     */
    public void setTemp(float temp) {
        this.temp = temp;
    }

    /**
     * @return the temp date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the temp date
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }
}


