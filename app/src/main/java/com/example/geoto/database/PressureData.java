package com.example.geoto.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

/**
 * A class to represent the pressure objects we store
 */
@Entity()
@TypeConverters({Converters.class})
public class PressureData {
    @PrimaryKey(autoGenerate = true)
    @androidx.annotation.NonNull
    private int pressureId=0;
    private float pressure;
    private Date date;

    /**
     * A constructor to initialise the pressure data
     * @param pressure the pressure reading
     * @param date the measurement date
     */
    public PressureData(float pressure, Date date) {
        this.pressure = pressure;
        this.date = date;
    }

    /**
     * @return the pressure id
     */
    @androidx.annotation.NonNull
    public int getPressureId() {
        return pressureId;
    }

    /**
     * Set the pressure id
     * @param pressureId
     */
    public void setPressureId(@androidx.annotation.NonNull int pressureId) {
        this.pressureId = pressureId;
    }

    /**
     * @return the pressure value
     */
    public float getPressure() {
        return pressure;
    }

    /**
     * Set the pressure value
     * @param pressure
     */
    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    /**
     * @return the pressure date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the pressure date
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
