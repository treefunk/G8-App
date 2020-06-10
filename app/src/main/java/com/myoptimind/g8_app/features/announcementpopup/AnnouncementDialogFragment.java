package com.myoptimind.g8_app.features.announcementpopup;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.features.dashboard.DashboardViewModel;
import com.myoptimind.g8_app.features.shared.BaseDialogFragment;
import com.myoptimind.g8_app.models.Announcement;
import com.myoptimind.g8_app.models.UserAnnouncement;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

public class AnnouncementDialogFragment extends BaseDialogFragment {

    private static final String TAG = "AnnouncementDialog";

    private ViewPager2 mViewPager;
    private ImageView ivBack, ivNext;
    private Button btnBackground;
    private CheckBox cbConfirm;
    private List<Announcement> mAnnouncements;
    private TextView tvPageNum, tvConfirmText;

    UnreadAnnouncementAdapter unreadAnnouncementAdapter;

    public AnnouncementDialogFragment(List<Announcement> announcements) {
        mAnnouncements = announcements;

    }

    public AnnouncementDialogFragment() {

    }

    public static AnnouncementDialogFragment newInstance(@Nullable List<Announcement> announcements) {

        Bundle args = new Bundle();

        AnnouncementDialogFragment fragment = new AnnouncementDialogFragment(announcements);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.dialog_announcements, container, false);

        mViewPager = v.findViewById(R.id.vp_memo_body);
        ivBack = v.findViewById(R.id.img_back_icon);
        ivNext = v.findViewById(R.id.img_next_icon);
        btnBackground = v.findViewById(R.id.btn_mainmenu);
        cbConfirm = v.findViewById(R.id.cb_confirm);
        tvPageNum = v.findViewById(R.id.tv_page_num);
        tvConfirmText = v.findViewById(R.id.tv_confirm_text);


        tvConfirmText.setText(getActivity().getString(R.string.label_confirm_with_date, DateTimeFormat.forPattern("MMM dd, yyyy").print(new DateTime())));

        unreadAnnouncementAdapter = new UnreadAnnouncementAdapter(mAnnouncements,
                new UnreadAnnouncementAdapter.BindListener() {
                    @Override
                    public void onBind(Announcement announcement, int position) {

                    }
                });


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager.getCurrentItem() != 0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
                }
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager.getCurrentItem() != unreadAnnouncementAdapter.getItemCount() - 1) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                }
            }
        });


        final DashboardViewModel dashboardViewModel = new ViewModelProvider(getActivity()).get(DashboardViewModel.class);

        mViewPager.setAdapter(unreadAnnouncementAdapter);

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Announcement announcement = mAnnouncements.get(position);
                Log.v(TAG, "getcheck - " + mAnnouncements.get(position).getContent());
                Log.v(TAG, "getcheck - " + mAnnouncements.get(position).getChecked());
                dashboardViewModel.setActiveAnnouncementId(String.valueOf(announcement.getId()));
                tvPageNum.setText(position + 1 + "/" + mAnnouncements.size());
            }
        });

        dashboardViewModel.getActiveUserAnnouncement().observe(this.getViewLifecycleOwner(), new Observer<UserAnnouncement>() {
            @Override
            public void onChanged(UserAnnouncement userAnnouncement) {
                if (userAnnouncement != null) {
                    cbConfirm.setChecked(true);
                } else {
                    cbConfirm.setChecked(false);
                }
            }
        });

        cbConfirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Announcement announcement = mAnnouncements.get(mViewPager.getCurrentItem());
                if (isChecked) {
                    dashboardViewModel.insertUserAnnouncement(String.valueOf(announcement.getId()));
                } else {
                    dashboardViewModel.deleteUserAnnouncement(String.valueOf(announcement.getId()));
                }
                dashboardViewModel.updateAnnouncement(announcement);
            }
        });

        dashboardViewModel.getUnread().observe(this.getViewLifecycleOwner(), new Observer<List<Announcement>>() {
            @Override
            public void onChanged(List<Announcement> announcements) {
                btnBackground.setEnabled(announcements.size() == 0);
            }
        });

        btnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        setCancelable(false);

        return v;
    }
}
