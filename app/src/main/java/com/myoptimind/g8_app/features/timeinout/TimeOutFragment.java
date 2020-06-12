package com.myoptimind.g8_app.features.timeinout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.features.shared.LocationAwareFragment;
import com.myoptimind.g8_app.features.shared.LocationListener;
import com.myoptimind.g8_app.models.TimeInOut;

public class TimeOutFragment extends Fragment implements LocationListener {

    public static final String TAG = "TimeOutFragment";

    TimeInViewModel timeInViewModel;
    TextView tvLocation;
    Button btnTimeOut;

    public static TimeOutFragment newInstance() {

        Bundle args = new Bundle();

        TimeOutFragment fragment = new TimeOutFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_time_out,container,false);

        tvLocation = v.findViewById(R.id.tv_location);

        btnTimeOut = v.findViewById(R.id.btn_time_out);

        timeInViewModel = new ViewModelProvider(getActivity()).get(TimeInViewModel.class);

        timeInViewModel.getCanTimeOut().observe(getViewLifecycleOwner(), canTimeOut -> {
                btnTimeOut.setEnabled(canTimeOut);
        });

        btnTimeOut.setOnClickListener(v1 -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Time Out")
                    .setMessage("Confirm Time out?")
                    .setPositiveButton("Yes", (dialog, which) -> timeInViewModel.recordTimeIn(TimeInOut.TYPE_TIMEOUT))
                    .setNegativeButton("No", null)
                    .show();
        });

        timeInViewModel.getActiveStore().observe(getViewLifecycleOwner(), store -> {
            if(store != null){
                tvLocation.setText(store.getStoreName());
            }else{
                tvLocation.setText("");
            }
        });

        return v;
    }


    public void onRequestCoordinates(double lat_, double long_) {
        timeInViewModel.initCoordinates(new Coordinates(lat_,long_));
    }

    public void onCheckAvailability(Boolean isAvailable) {
        if(!isAvailable){
            timeInViewModel.initCoordinates(null);
            timeInViewModel.clearActiveStore();
        }
    }
}
