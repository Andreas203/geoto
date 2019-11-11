package com.example.geoto.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity()
public class PathData {
    @PrimaryKey(autoGenerate = true)
    @androidx.annotation.NonNull
    private int pathId=0;
    private String title;
    private Date startDate;
    private Date endDate;
    private String description;


    public PathData(int pathId, String title, Date date, Date time, String description) {
        this.pathId = pathId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    @androidx.annotation.NonNull
    public int getPathId() {
        return pathId;
    }
    public void setPathId(@androidx.annotation.NonNull int pathId) {
        this.pathId = pathId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date date) {
        this.startDate = date;
    }

    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date date) {
        this.endDate = date;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
