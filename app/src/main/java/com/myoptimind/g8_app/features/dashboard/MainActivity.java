package com.myoptimind.g8_app.features.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.stetho.Stetho;
import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.features.shared.SingleActivityFragment;

public class MainActivity extends SingleActivityFragment {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar);
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
    }

    @Override
    protected Fragment createFragment() {
        return new DashboardFragment();
    }

    @Override
    protected String setLabel() {
        return "Dashboard";
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"ondestroy");
    }
}
