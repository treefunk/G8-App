package com.myoptimind.g8_app.features.syncing.response;

import com.myoptimind.g8_app.api.Meta;
import com.myoptimind.g8_app.models.TimeSlip;

public class PushUploadSlipResponse {
    private TimeSlip data;
    private Meta meta;

    public TimeSlip getData() {
        return data;
    }

    public void setData(TimeSlip data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
