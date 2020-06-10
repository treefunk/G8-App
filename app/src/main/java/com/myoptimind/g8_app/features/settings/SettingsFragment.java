package com.myoptimind.g8_app.features.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.features.dashboard.DashboardViewModel;
import com.myoptimind.g8_app.features.login.LoginActivity;
import com.myoptimind.g8_app.features.shared.SharedPref;
import com.myoptimind.g8_app.models.Store;
import com.myoptimind.g8_app.models.User;

import java.util.List;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";


    public static SettingsFragment newInstance() {

        SettingsFragment settingsFragment = new SettingsFragment();
        Bundle bundle = new Bundle();

        settingsFragment.setArguments(bundle);

        return settingsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView lblMyStore = view.findViewById(R.id.label_change_store);
        TextView tvMyName = view.findViewById(R.id.tv_my_name);
        TextView tvStores = view.findViewById(R.id.tv_stores);
        Button btnLogout = view.findViewById(R.id.btn_logout);
        View boxMyStore = view.findViewById(R.id.box_my_store);
        ImageView imgStore = view.findViewById(R.id.img_store);


        SettingsViewModel settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);


        settingsViewModel.getUser().observe(this.getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    tvMyName.setText(user.getFullname());
                }
            }
        });


        settingsViewModel.getStores().observe(this.getViewLifecycleOwner(), new Observer<List<Store>>() {
            @Override
            public void onChanged(List<Store> stores) {
                if (stores != null && stores.size() > 0) {
                    StringBuilder storesString = new StringBuilder();

                    lblMyStore.setText(getActivity()
                            .getResources()
                            .getQuantityString(R.plurals.label_my_store, stores.size(), stores.size()));

                    for (Store store : stores) {
                        storesString.append(store.getStoreName() + "\n\n");
                    }

                    tvStores.setText(storesString.toString());
                } else {
                    // no stores for this user
                    tvStores.setVisibility(View.GONE);
                    lblMyStore.setVisibility(View.GONE);
                    boxMyStore.setVisibility(View.GONE);
                    imgStore.setVisibility(View.GONE);
                }
            }
        });

        final DashboardViewModel dashboardViewModel = new ViewModelProvider(getActivity()).get(DashboardViewModel.class);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardViewModel.clearUser();
                SharedPref sharedPref = SharedPref.getInstance(getActivity());
                sharedPref.setIdLoggedIn(null);
                sharedPref.clearLastSyncs();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });


        return view;
    }
}