package com.myoptimind.g8_app.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "time_in_out")
public class TimeInOut extends AppGeneratedEntity {


    @SerializedName("user_id")
    @ColumnInfo(name = "user_id")
    private int userId;

    @SerializedName("store_id")
    @ColumnInfo(name = "store_id")
    private String storeId;

    @ColumnInfo(name = "type") // 1 - In, 2 - Out
    private int type;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
