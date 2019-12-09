package com.example.geoto.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity()
@TypeConverters({Converters.class})
public class TempData {
    @PrimaryKey(autoGenerate = true)
    @androidx.annotation.NonNull
    private int tempId=0;
    private float temp;
    private Date date;

    public TempData(float temp, Date date) {
        this.temp = temp;
        this.date = date;
    }

    @androidx.annotation.NonNull
    public int getTempId() {
        return tempId;
    }
    public void setTempId(@androidx.annotation.NonNull int tempId) {
        this.tempId = tempId;
    }

    public float getTemp() {
        return temp;
    }
    public void setTemp(float temp) {
        this.temp = temp;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
