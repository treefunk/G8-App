package com.myoptimind.g8_app.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "timeslip")
public class TimeSlip extends BaseEntity{

    @ColumnInfo(name = "user_id")
    private String userId;

    private String timecard;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimecard() {
        return timecard;
    }

    public void setTimecard(String timecard) {
        this.timecard = timecard;
    }
}
