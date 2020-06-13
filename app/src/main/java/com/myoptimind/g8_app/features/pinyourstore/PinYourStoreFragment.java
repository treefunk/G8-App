package com.myoptimind.g8_app.features.pinyourstore;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.lifecycle.ViewModelProvider;

import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.features.shared.LocationAwareFragment;
import com.myoptimind.g8_app.features.syncing.Syncer;
import com.myoptimind.g8_app.features.timeinout.Coordinates;

import java.util.AbstractList;
import java.util.List;

public class PinYourStoreFragment extends LocationAwareFragment {

    private AppCompatAutoCompleteTextView acStoreName;
    private TextView etStoreAddress;
    private Button btnSubmit;
    PinYourStoreViewModel pinYourStoreViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_pin_your_store, container, false);
        acStoreName = v.findViewById(R.id.ac_store_name);
        etStoreAddress = v.findViewById(R.id.et_store_address);
        btnSubmit = v.findViewById(R.id.btn_submit);

        pinYourStoreViewModel = new ViewModelProvider(getActivity()).get(PinYourStoreViewModel.class);

        btnSubmit.setOnClickListener(btn -> {

            String storeName    = acStoreName.getText().toString();
            if(storeName.trim().isEmpty()){
                Toast.makeText(getActivity(),"Store name is required.",Toast.LENGTH_LONG).show();
                return;
            }
            String storeAddress = etStoreAddress.getText().toString();

            new AlertDialog.Builder(getActivity())
                    .setTitle("Confirm")
                    .setMessage("Are you sure you want to PIN " + acStoreName.getText().toString())
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pinYourStoreViewModel.addStore(storeName,storeAddress);
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .show();
        });

        pinYourStoreViewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
        });

        pinYourStoreViewModel.getCanEnterStore().observe(getViewLifecycleOwner(),canEnterStore -> {
            btnSubmit.setEnabled(canEnterStore);
        });

        pinYourStoreViewModel.getStores().observe(getViewLifecycleOwner(), stores -> {
            if(stores != null && stores.size() > 0){
                acStoreName.clearListSelection();

                List<String> names = new AbstractList<String>() {
                    @Override
                    public String get(int index) {
                        return stores.get(index).getStoreName();
                    }

                    @Override
                    public int size() {
                        return stores.size();
                    }
                };

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                        getActivity(),
                        R.layout.store_autocomplete_row,
                        names);
                acStoreName.setAdapter(arrayAdapter);
            }
        });

        Syncer.getInstance(getActivity().getApplicationContext()).getNum().observe(getViewLifecycleOwner(), m -> {
            Toast.makeText(getActivity(),m,Toast.LENGTH_SHORT).show();
        });

        return v;

    }


    @Override
    public void onRequestCoordinates(double lat_, double long_) {
        pinYourStoreViewModel.setCoordinates(new Coordinates(lat_,long_));
    }

    @Override
    public void onCheckAvailability(Boolean isAvailable) {
        if(!isAvailable){
            pinYourStoreViewModel.setCoordinates(null);
        }
    }
}
