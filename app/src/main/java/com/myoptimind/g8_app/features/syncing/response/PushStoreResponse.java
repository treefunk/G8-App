package com.myoptimind.g8_app.features.syncing.response;

import com.myoptimind.g8_app.api.Meta;
import com.myoptimind.g8_app.models.Store;

public class PushStoreResponse {
    public Store data;
    public Meta meta;

    public Store getData() {
        return data;
    }

    public void setData(Store data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
