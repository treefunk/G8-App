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
import com.myoptimind.g8_app.features.syncing.Syncer;

public class MainActivity extends SingleActivityFragment {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar);
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);


/*        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                SampleWorkerC.class,15, TimeUnit.MINUTES
        ).build();

        PeriodicWorkRequest periodicWorkRequestb = new PeriodicWorkRequest.Builder(
                SampleWorkerD.class,15, TimeUnit.MINUTES
        ).build();

        WorkManager.getInstance(getApplicationContext()).enqueue(periodicWorkRequest);
        WorkManager.getInstance(getApplicationContext()).enqueue(periodicWorkRequestb);*/

        Syncer.getInstance(getApplicationContext()).start();

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
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Syncer.getInstance(getApplicationContext()).clearDisposables();
    }

    @Override
    protected String getFragmentTag() {
        return "DashboardFragment";
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onstop");

    }
}
