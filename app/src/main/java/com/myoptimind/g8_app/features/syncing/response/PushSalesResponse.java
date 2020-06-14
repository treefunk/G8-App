package com.myoptimind.g8_app.features.syncing.response;

import com.myoptimind.g8_app.api.Meta;
import com.myoptimind.g8_app.models.SalesReport;

public class PushSalesResponse {
    public SalesReport data;
    public Meta meta;

    public SalesReport getData() {
        return data;
    }

    public void setData(SalesReport data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
