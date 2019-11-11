package com.example.geoto.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class PathData {
    @PrimaryKey(autoGenerate = true)
    @androidx.annotation.NonNull
    private int id=0;
    private int number;

    public PathData(int number) {
        this.number= number;
    }

    @androidx.annotation.NonNull
    public int getId() {
        return id;
    }
    public void setId(@androidx.annotation.NonNull int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
