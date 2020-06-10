package com.myoptimind.g8_app.models;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.myoptimind.g8_app.Utils;

public class BaseEntity {

    public BaseEntity() {
        this.createdAt = Utils.newDateSqlString();
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    private String createdAt;

    @SerializedName("updated_at")
    @ColumnInfo(name = "updated_at")
    private String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
