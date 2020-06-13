package com.myoptimind.g8_app.features.syncing.response;

import com.google.gson.annotations.SerializedName;
import com.myoptimind.g8_app.api.Meta;
import com.myoptimind.g8_app.features.syncing.response.PaginationResponse;
import com.myoptimind.g8_app.models.Store;

import java.util.List;

public class PullStoreResponse {

    public List<Store> data;

    @SerializedName("last_update")
    public String lastUpdate;
    public PaginationResponse pagination;
    public Meta meta;

    public List<Store> getData() {
        return data;
    }

    public void setData(List<Store> data) {
        this.data = data;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public PaginationResponse getPagination() {
        return pagination;
    }

    public void setPagination(PaginationResponse pagination) {
        this.pagination = pagination;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
