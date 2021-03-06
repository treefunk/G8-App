package com.myoptimind.g8_app.features.timeinout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.features.shared.LocationAwareFragment;
import com.myoptimind.g8_app.features.shared.LocationListener;
import com.myoptimind.g8_app.models.Store;
import com.myoptimind.g8_app.models.TimeInOut;



public class TimeInFragment extends Fragment implements LocationListener {

    public static final String TAG = "TimeInFragment";

    private TimeInViewModel timeInViewModel;
    Button btnEnterStoreInformation;
    Button btnTimeIn;
    TextView tvLocation;
    StoreDialogFragment storeDialogFragment;

    public static TimeInFragment newInstance() {
        Bundle args = new Bundle();
        TimeInFragment fragment = new TimeInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_in,container,false);

        btnEnterStoreInformation = view.findViewById(R.id.btn_enter_store_information);
        btnTimeIn                = view.findViewById(R.id.btn_time_in);
        tvLocation               = view.findViewById(R.id.tv_location);

        timeInViewModel = new ViewModelProvider(getActivity()).get(TimeInViewModel.class);

        timeInViewModel.getActiveStore().observe(getViewLifecycleOwner(), store -> {
            if(store != null){
                tvLocation.setText(store.getStoreName());
            }else{
                tvLocation.setText("");
            }
        });

        timeInViewModel.getCanTimeIn().observe(getViewLifecycleOwner(),
                canTimeIn -> {
                    btnTimeIn.setEnabled(canTimeIn);
                }
        );

        btnTimeIn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    .setTitle("Time In");
            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_input_sales_amount, (ViewGroup) getView(), false);
            final EditText input = (EditText) viewInflated.findViewById(R.id.input);
            builder.setView(viewInflated);
//            builder.setMessage("Confirm Time in?");
            builder.setPositiveButton("Yes",null).setNegativeButton("No", null);
            AlertDialog dialog = builder.create();

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {

                    Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(input.getText().toString().trim().isEmpty()){
                                Toast.makeText(requireContext(), "Please enter sales amount", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            timeInViewModel.recordTimeIn(TimeInOut.TYPE_TIMEIN, input.getText().toString());
                            dialog.dismiss();
                        }
                    });


                }
            });
            dialog.show();
        });

        btnEnterStoreInformation.setOnClickListener(v -> {
            storeDialogFragment = StoreDialogFragment.newInstance();
            storeDialogFragment.setTargetFragment(TimeInFragment.this,911);
            storeDialogFragment.show(getActivity().getSupportFragmentManager(),StoreDialogFragment.TAG);
        });

        // MESSAGES

        timeInViewModel.getMessage().observe(getViewLifecycleOwner(), alertMessage -> Toast.makeText(getActivity(),alertMessage,Toast.LENGTH_LONG).show());


        return view;
    }


    public void onRequestCoordinates(double lat_, double long_) {
        Coordinates coordinates = new Coordinates(lat_,long_);
        timeInViewModel.initCoordinates(coordinates);

        btnEnterStoreInformation.setOnClickListener(v -> {
            storeDialogFragment = StoreDialogFragment.newInstance(coordinates.getLatitude(),coordinates.getLongitude());
            storeDialogFragment.setTargetFragment(TimeInFragment.this,911);
            storeDialogFragment.show(getActivity().getSupportFragmentManager(),StoreDialogFragment.TAG);
        });

        if(storeDialogFragment != null){
            storeDialogFragment.setCoordinates(new Coordinates(lat_,long_));
        }
    }

    public void onCheckAvailability(Boolean isAvailable) {
        if(!isAvailable){
            timeInViewModel.initCoordinates(null);
            timeInViewModel.clearActiveStore();
            if(storeDialogFragment != null){
                storeDialogFragment.setCoordinates(null);
            }
            btnEnterStoreInformation.setEnabled(false);
        }else{
            btnEnterStoreInformation.setEnabled(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"Paused");
    }
}
