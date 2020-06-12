package com.myoptimind.g8_app.features.login;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.stetho.Stetho;
import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.features.shared.SingleActivityFragment;

public class LoginActivity extends SingleActivityFragment {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar);
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
    }

    @Override
    protected String setLabel() {
        return null;
    }

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }

}
