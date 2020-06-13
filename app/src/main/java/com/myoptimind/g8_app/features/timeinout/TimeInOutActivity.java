package com.myoptimind.g8_app.features.timeinout;

import com.myoptimind.g8_app.features.shared.LocationAwareActivity;
import com.myoptimind.g8_app.features.shared.LocationListener;
import com.myoptimind.g8_app.features.shared.SingleActivityFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.myoptimind.g8_app.features.shared.TabbedFragment;


public class TimeInOutActivity extends LocationAwareActivity{

    private LocationListener mLocationListener;

    private Fragment firstFragment;
    private Fragment secondFragment;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, TimeInOutActivity.class);
        return intent;
    }

    @Override
    protected String setLabel() {
        return "TIME IN/TIME OUT";
    }

    @Override
    protected Fragment createFragment() {
        TabbedFragment tabbedFragment = TabbedFragment.newInstance();

        tabbedFragment.setStateAdapter(new FragmentStateAdapter(getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if(position == 0) {
                    firstFragment = TimeInFragment.newInstance();
                    return firstFragment;
                }
                secondFragment = TimeOutFragment.newInstance();
                return secondFragment;
            }
            @Override
            public int getItemCount() {
                return 2;
            }
        });

        tabbedFragment.setTitles(new String[]{"TIME IN", "TIME OUT"});

        return tabbedFragment;
    }

    @Override
    public void onRequestCoordinates(double lat_, double long_) {
        ((TimeInFragment) firstFragment).onRequestCoordinates(lat_,long_);
        ((TimeOutFragment) secondFragment).onRequestCoordinates(lat_,long_);
    }

    @Override
    public void onCheckAvailability(Boolean isAvailable) {
        ((TimeInFragment) firstFragment).onCheckAvailability(isAvailable);
        ((TimeOutFragment) secondFragment).onCheckAvailability(isAvailable);
    }

    @Override
    protected String getFragmentTag() {
        return "TimeInOutFragment";
    }
}
