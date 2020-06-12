package com.myoptimind.g8_app.features.timeinout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.lifecycle.ViewModelProvider;

import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.features.pinyourstore.PinYourStoreViewModel;
import com.myoptimind.g8_app.features.shared.BaseDialogFragment;

import java.util.AbstractList;
import java.util.List;

public class StoreDialogFragment extends BaseDialogFragment {

    public static final String TAG = "StoreDialogFragment";


    private static final String ARGS_LAT  = "latitude_arg";
    private static final String ARGS_LONG = "longitude_arg";

    private Button btnSubmit;
    private EditText etStoreAddress;
    private AppCompatAutoCompleteTextView acStoreName;
    private Coordinates mCoordinates = null;
    PinYourStoreViewModel pinYourStoreViewModel;

    public void setCoordinates(Coordinates coordinates) {
        mCoordinates = coordinates;
        pinYourStoreViewModel.setCoordinates(coordinates);
    }

    public static StoreDialogFragment newInstance(Double latitude, Double longitude) {

        Bundle args = new Bundle();

        StoreDialogFragment fragment = new StoreDialogFragment();
        args.putDouble(ARGS_LAT,latitude);
        args.putDouble(ARGS_LONG,longitude);
        fragment.setArguments(args);

        return fragment;
    }

    public static StoreDialogFragment newInstance() {
        
        Bundle args = new Bundle();
        
        StoreDialogFragment fragment = new StoreDialogFragment();
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
        super.onCreateView(inflater,container,savedInstanceState);

        pinYourStoreViewModel = new ViewModelProvider(getActivity()).get(PinYourStoreViewModel.class);

        Bundle bundle = getArguments();

        if(bundle != null && (bundle.containsKey(ARGS_LAT) && bundle.containsKey(ARGS_LONG))){
            Coordinates coordinates = new Coordinates(
                    bundle.getDouble(ARGS_LAT),
                    bundle.getDouble(ARGS_LONG)
            );
            pinYourStoreViewModel.setCoordinates(coordinates);
        }

        return inflater.inflate(R.layout.dialog_enter_store_info,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSubmit      = view.findViewById(R.id.btn_submit);
        acStoreName    = view.findViewById(R.id.ac_store_name);
        etStoreAddress = view.findViewById(R.id.et_store_address);

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
    }

}
