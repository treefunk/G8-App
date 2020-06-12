package com.myoptimind.g8_app.features.announcementsmemos;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.myoptimind.g8_app.features.shared.SingleActivityFragment;
import com.myoptimind.g8_app.features.shared.TabbedFragment;
import com.myoptimind.g8_app.features.announcementsmemos.bulletin.BulletinFragment;
import com.myoptimind.g8_app.features.announcementsmemos.memos.MemoFragment;

public class AnnouncementsActivity extends SingleActivityFragment {

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,AnnouncementsActivity.class);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        TabbedFragment tabbedFragment = TabbedFragment.newInstance();

        tabbedFragment.setStateAdapter(new FragmentStateAdapter(getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if(position == 0) {
                    return BulletinFragment.newInstance();
                }
                return MemoFragment.newInstance();
            }
            @Override
            public int getItemCount() {
                return 2;
            }
        });

        tabbedFragment.setTitles(new String[]{"BULLETIN", "MEMO"});

        return tabbedFragment;
    }


    @Override
    protected String setLabel() {
        return "Announcements";
    }
}