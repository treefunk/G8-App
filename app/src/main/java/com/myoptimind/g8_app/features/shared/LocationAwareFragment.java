package com.myoptimind.g8_app.features.shared;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.myoptimind.g8_app.R;

import java.util.concurrent.TimeUnit;

public abstract class LocationAwareFragment extends Fragment implements LocationListener {

    private static final String TAG = "LocationAwareFragment";

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback locationCallback;
    private static final int SECONDS_PER_UPDATE_INTERVAL = 30;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissionLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    private void startLocationUpdates(){

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(getActivity(),getString(R.string.dialog_text_turn_gps),Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    double lat_ = location.getLatitude();
                    double long_ = location.getLongitude();
                    Log.d("GPS","lat : " + location.getLatitude());
                    Log.d("GPS", "long : " + location.getLongitude());
                    Log.d("GPS", "location link: " + "https://www.google.com/maps/place/" + lat_ + "," + long_);
                    LocationAwareFragment.this.onRequestCoordinates(lat_,long_);
                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                LocationAwareFragment.this.onCheckAvailability(locationAvailability.isLocationAvailable());
            }
        };

        mFusedLocationProviderClient.requestLocationUpdates(
                createLocationRequest(),
                locationCallback,
                Looper.getMainLooper()
        );

    }

    private void stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(TimeUnit.SECONDS.toMillis(SECONDS_PER_UPDATE_INTERVAL));
        locationRequest.setFastestInterval(TimeUnit.SECONDS.toMillis(SECONDS_PER_UPDATE_INTERVAL-5));
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void checkPermissionLocation(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(
                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION },111

            );
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(),"Please enable your gps first",Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
