package com.myoptimind.g8_app.features.announcementsmemos.bulletin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.models.Announcement;

import java.util.ArrayList;
import java.util.List;

public class BulletinFragment extends Fragment {

    private RecyclerView rvBulletin;

    public static BulletinFragment newInstance() {

        Bundle args = new Bundle();

        BulletinFragment fragment = new BulletinFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bulletin, container, false);

        BulletinViewModel bulletinViewModel = new ViewModelProvider(getActivity()).get(BulletinViewModel.class);

        rvBulletin = v.findViewById(R.id.rv_bulletin);

        rvBulletin.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        BulletinAdapter bulletinAdapter = new BulletinAdapter(new ArrayList<>(), new BulletinAdapter.ClickListener() {
            @Override
            public void onClickItem(Announcement announcement, int position) {
                Intent intent = SingleBulletinActivity.newIntent(
                        getActivity(),
                        announcement.getTitle(),
                        announcement.getCreatedAt(),
                        announcement.getContent(),
                        announcement.getFeaturedImage()
                );
                startActivity(intent);
            }
        });

        rvBulletin.setAdapter(bulletinAdapter);

        bulletinViewModel.getBulletins().observe(this.getViewLifecycleOwner(), new Observer<List<Announcement>>() {
            @Override
            public void onChanged(List<Announcement> announcements) {
                bulletinAdapter.setAnnouncements(announcements);
                bulletinAdapter.notifyDataSetChanged();
            }
        });

        return v;
    }

}