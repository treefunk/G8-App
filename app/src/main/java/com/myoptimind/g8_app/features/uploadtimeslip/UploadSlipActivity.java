package com.myoptimind.g8_app.features.uploadtimeslip;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.myoptimind.g8_app.features.shared.SingleActivityFragment;

public class UploadSlipActivity extends SingleActivityFragment {

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,UploadSlipActivity.class);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return UploadSlipFragment.newInstance();
    }

    @Override
    protected String setLabel() {
        return "upload time slip";
    }
}