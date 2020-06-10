package com.myoptimind.g8_app.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.myoptimind.g8_app.Utils;

import java.util.UUID;

public class AppGeneratedEntity {

    AppGeneratedEntity() {
        this.uuid = UUID.randomUUID().toString();
        this.createdAt = Utils.newDateSqlString();
        this.updatedAt = "";
    }

    @NonNull
    @PrimaryKey
    protected String uuid;

    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    private String createdAt;

    @SerializedName("updated_at")
    @ColumnInfo(name = "updated_at")
    private String updatedAt;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
