package com.myoptimind.g8_app.features.salesreport;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.myoptimind.g8_app.features.shared.SingleActivityFragment;
import com.myoptimind.g8_app.features.shared.TabbedFragment;

public class SalesReportActivity extends SingleActivityFragment {

    public static final String ARGS_STOREUUID = "args_storeuuid";

    public static Intent newIntent(Context context, String storeUuid){

        Intent intent = new Intent(context,SalesReportActivity.class);
        intent.putExtra(ARGS_STOREUUID,storeUuid);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        TabbedFragment tabbedFragment = TabbedFragment.newInstance();

        tabbedFragment.setStateAdapter(new FragmentStateAdapter(getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if(position == 0){
                    return SalesUpdateFragment.newInstance(getIntent().getExtras().getString(ARGS_STOREUUID));
                }
                return new Fragment();
            }
            @Override
            public int getItemCount() {
                return 1;
            }
        });

        tabbedFragment.setTitles(new String[]{"SALES UPATE", "UPLOAD PHOTO"});

        return tabbedFragment;
    }

    @Override
    protected String setLabel() {
        return "Sales Report";
    }
}
