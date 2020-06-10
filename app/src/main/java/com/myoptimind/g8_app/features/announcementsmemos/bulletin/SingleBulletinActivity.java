package com.myoptimind.g8_app.features.announcementsmemos.bulletin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.myoptimind.g8_app.SingleActivityFragment;

public class SingleBulletinActivity extends SingleActivityFragment {

    private static final String EXTRA_TITLE = "s_extra_title";
    private static final String EXTRA_DATE = "s_extra_date";
    private static final String EXTRA_BODY  = "s_extra_body";
    private static final String EXTRA_IMAGE = "s_extra_image";

    public static Intent newIntent(Context context, String title, String date, String body, String image){

        Intent intent = new Intent(context, SingleBulletinActivity.class);

        intent.putExtra(EXTRA_TITLE,title);
        intent.putExtra(EXTRA_DATE,date);
        intent.putExtra(EXTRA_BODY,body);
        intent.putExtra(EXTRA_IMAGE,image);

        return intent;

    }

    @Override
    protected Fragment createFragment() {

        Bundle bundle = getIntent().getExtras();

        return SingleBulletinFragment.newInstance(
                bundle.getString(EXTRA_TITLE),
                bundle.getString(EXTRA_DATE),
                bundle.getString(EXTRA_BODY),
                bundle.getString(EXTRA_IMAGE)
        );
    }

    @Override
    protected String setLabel() {
        return "BULLETIN";
    }
}
