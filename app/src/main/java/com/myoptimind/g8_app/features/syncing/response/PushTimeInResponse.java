package com.myoptimind.g8_app.features.syncing.response;

import com.myoptimind.g8_app.api.Meta;
import com.myoptimind.g8_app.models.TimeInOut;

public class PushTimeInResponse {
    public TimeInOut data;
    public Meta meta;

    public TimeInOut getData() {
        return data;
    }

    public void setData(TimeInOut data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
