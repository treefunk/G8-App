package com.myoptimind.g8_app.features.shared;

public interface LocationListener {
        void onRequestCoordinates(double lat_, double long_);
        void onCheckAvailability(Boolean isAvailable);
}
