package com.myoptimind.g8_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class TabbedFragment extends Fragment {

    private FragmentStateAdapter stateAdapter;
    private String[] titles;

    public static TabbedFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TabbedFragment fragment = new TabbedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setStateAdapter(FragmentStateAdapter stateAdapter) {
        this.stateAdapter = stateAdapter;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two_tab_pager,container,false);

        ViewPager2 mViewPager = view.findViewById(R.id.vp_main);

        mViewPager.setAdapter(stateAdapter);


        TabLayout mTabLayout = view.findViewById(R.id.tab_layout);

        new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(
                        titles[position]
                );
;            }
        }).attach();




        return view;

    }


}
