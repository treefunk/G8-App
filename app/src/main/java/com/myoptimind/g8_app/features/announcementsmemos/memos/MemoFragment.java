package com.myoptimind.g8_app.features.announcementsmemos.memos;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.features.announcementsmemos.bulletin.SingleBulletinActivity;
import com.myoptimind.g8_app.models.Announcement;

import java.util.ArrayList;
import java.util.List;

public class MemoFragment extends Fragment {

    private RecyclerView rvMemo;
    public static MemoFragment newInstance() {

        Bundle args = new Bundle();

        MemoFragment fragment = new MemoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_memo,container,false);

        rvMemo = v.findViewById(R.id.rv_memo);

        rvMemo.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        MemoViewModel memoViewModel = new ViewModelProvider(getActivity()).get(MemoViewModel.class);

        MemoAdapter memoAdapter = new MemoAdapter(new ArrayList<>(), new MemoAdapter.ClickListener() {
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

        rvMemo.setAdapter(memoAdapter);


        memoViewModel.getMemos().observe(this.getViewLifecycleOwner(), new Observer<List<Announcement>>() {
            @Override
            public void onChanged(List<Announcement> announcements) {
                memoAdapter.setMemos(announcements);
                memoAdapter.notifyDataSetChanged();
            }
        });

        return v;

    }
}
