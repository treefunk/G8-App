package com.myoptimind.g8_app.features.settings;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.myoptimind.g8_app.SingleActivityFragment;

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
}