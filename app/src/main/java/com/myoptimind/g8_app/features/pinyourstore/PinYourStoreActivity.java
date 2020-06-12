package com.myoptimind.g8_app.features.pinyourstore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myoptimind.g8_app.features.shared.SingleActivityFragment;

public class PinYourStoreActivity extends SingleActivityFragment {


    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,PinYourStoreActivity.class);


        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        return new PinYourStoreFragment();
    }

    @Override
    protected String setLabel() {
        return "Pin Your Store";
    }

}

