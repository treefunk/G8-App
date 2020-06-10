package com.myoptimind.g8_app.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "user_store")
public class UserStore extends BaseEntity{


    /**
     *
     * @param userId User id
     * @param storeUuid tagged store
     */
    public UserStore(String userId, String storeUuid) {
        this.userId = userId;
        this.storeUuid = storeUuid;
    }

    @SerializedName("user_id")
    @ColumnInfo(name = "user_id")
    private String userId;


    @SerializedName("store_uuid")
    @ColumnInfo(name = "store_uuid")
    private String storeUuid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreUuid() {
        return storeUuid;
    }

    public void setStoreUuid(String storeUuid) {
        this.storeUuid = storeUuid;
    }
}

