package com.myoptimind.g8_app.features.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.myoptimind.g8_app.features.dashboard.DashboardViewModel;
import com.myoptimind.g8_app.features.shared.SingleActivityFragment;

public class SettingsActivity extends SingleActivityFragment {

    public static final String LABEL = "Settings";


    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,SettingsActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return SettingsFragment.newInstance();
    }

    @Override
    protected String setLabel() {
        return LABEL;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected String getFragmentTag() {
        return "SettingsFragment";
    }
}