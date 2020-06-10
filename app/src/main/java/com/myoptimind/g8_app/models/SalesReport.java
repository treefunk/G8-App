package com.myoptimind.g8_app.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "sales_report")
public class SalesReport extends AppGeneratedEntity {

    public SalesReport(){
        hasSynced = false;
    }

    @SerializedName("user_id")
    @ColumnInfo(name = "user_id")
    private String userId;

    @SerializedName("sale_date")
    @ColumnInfo(name = "datetime")
    private String datetime;

    @ColumnInfo(name = "sales")
    private String sales;

    @ColumnInfo(name = "has_synced")
    private Boolean hasSynced;

    @SerializedName("store_uuid")
    @ColumnInfo(name = "store_uuid")
    private String storeUuid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public Boolean getHasSynced() {
        return hasSynced;
    }

    public void setHasSynced(Boolean hasSynced) {
        this.hasSynced = hasSynced;
    }

    public String getStoreUuid() {
        return storeUuid;
    }

    public void setStoreUuid(String storeUuid) {
        this.storeUuid = storeUuid;
    }
}
