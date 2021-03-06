package com.myoptimind.g8_app.features.shared;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.myoptimind.g8_app.R;

import io.reactivex.Single;

public abstract class SingleActivityFragment extends AppCompatActivity {

    protected abstract Fragment createFragment();
    protected abstract String setLabel();
    protected abstract String getFragmentTag();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment, getFragmentTag()).commit();
        }

        final ActionBar abar = getSupportActionBar();



        if(abar != null && setLabel() != null){
            View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_layout, null);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            TextView textviewTitle =  viewActionBar.findViewById(R.id.tv_actionbar_title);
            textviewTitle.setText(setLabel());
            abar.setCustomView(viewActionBar, params);
            abar.setDisplayShowCustomEnabled(true);
            abar.setDisplayShowTitleEnabled(false);
            abar.setHomeButtonEnabled(true);
        }


    }




}