package com.myoptimind.g8_app.features.syncing.response;

import com.google.gson.annotations.SerializedName;

public class PaginationResponse {
    public int offset,limit;

    @SerializedName("total_count")
    public int totalCount;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}

