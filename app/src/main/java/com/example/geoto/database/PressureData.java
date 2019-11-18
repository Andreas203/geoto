package com.example.geoto.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity()
@TypeConverters({Converters.class})
public class PressureData {
    @PrimaryKey(autoGenerate = true)
    @androidx.annotation.NonNull
    private int pressureId=0;
    private float pressure;
    private Date date;

    public PressureData(int pressureId, float pressure, Date date) {
        this.pressureId = pressureId;
        this.pressure = pressure;
        this.date = date;
    }

    @androidx.annotation.NonNull
    public int getPressureId() {
        return pressureId;
    }
    public void setPressureId(@androidx.annotation.NonNull int pressureId) {
        this.pressureId = pressureId;
    }

    public float getPressure() {
        return pressure;
    }
    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
