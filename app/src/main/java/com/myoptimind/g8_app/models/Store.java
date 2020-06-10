package com.myoptimind.g8_app.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "store")
public class Store extends AppGeneratedEntity{

    @SerializedName("user_id")
    @ColumnInfo(name = "user_id")
    private int userId;

    @SerializedName("store_name")
    @ColumnInfo(name = "store_name")
    private String storeName;

    @SerializedName("store_address")
    @ColumnInfo(name = "store_address")
    private String storeAddress;

    private double longitude;

    private double latitude;

    @SerializedName("is_approved")
    @ColumnInfo(name = "is_approved")
    private int isApproved;


    public Store() {

    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }


}
