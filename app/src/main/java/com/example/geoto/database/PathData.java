package com.example.geoto.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity()
@TypeConverters({Converters.class})
public class PathData implements Comparable<PathData> {
    @PrimaryKey(autoGenerate = true)
    @androidx.annotation.NonNull
    public int pathId=0;
    public String title;
    public Date startDate;
    public Date endDate;
    public String description;


    public PathData(String title, Date startDate, Date endDate, String description) {
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

    @Override
    public int compareTo(PathData o) {
        if (startDate.compareTo(o.getStartDate()) > 0) {
            return 1;
        } else if (startDate.compareTo(o.getStartDate()) <0) {
            return -1;
        } else {
            return 0;
        }
    }
}
