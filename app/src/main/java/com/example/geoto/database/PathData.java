package com.example.geoto.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

/**
 * A class to represent the path objects we store
 */
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

    /**
     * A constructor to initialise the path's data
     * @param title the path title
     * @param startDate the path start date
     * @param endDate the path end date
     * @param description teh path description
     */
    public PathData(String title, Date startDate, Date endDate, String description) {
        this.pathId = pathId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    /**
     * @return the path id
     */
    @androidx.annotation.NonNull
    public int getPathId() {
        return pathId;
    }

    /**
     * Set the path id
     * @param pathId
     */
    public void setPathId(@androidx.annotation.NonNull int pathId) {
        this.pathId = pathId;
    }

    /**
     * @return the path title
     */
    public String getTitle() {
        return title;
    }
    /**
     * Sets the path title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the path start date
     */
    public Date getStartDate() {
        return startDate;
    }
    /**
     * Sets the path start date
     * @param date
     */
    public void setStartDate(Date date) {
        this.startDate = date;
    }

    /**
     * @return the path end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the path end date
     * @param date
     */
    public void setEndDate(Date date) {
        this.endDate = date;
    }

    /**
     * @return the path description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the path description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Compares two path objects by their date
     * @param o a path to compare to
     * @return a flag for each compare case
     */
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
