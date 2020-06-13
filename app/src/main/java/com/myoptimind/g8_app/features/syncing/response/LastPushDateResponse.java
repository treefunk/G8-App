package com.myoptimind.g8_app.features.syncing.response;

import com.google.gson.annotations.SerializedName;
import com.myoptimind.g8_app.api.Meta;

public class LastPushDateResponse {

    private Data data;

    public class Data {
        @SerializedName("user_id")
        String userId;

        @SerializedName("last_sync_date")
        String lastPushDate;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getLastPushDate() {
            return lastPushDate;
        }

        public void setLastPushDate(String lastPushDate) {
            this.lastPushDate = lastPushDate;
        }
    }

    private Meta meta;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
