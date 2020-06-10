package com.myoptimind.g8_app.features.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.Utils;
import com.myoptimind.g8_app.features.announcementpopup.AnnouncementDialogFragment;
import com.myoptimind.g8_app.features.announcementsmemos.AnnouncementsActivity;
import com.myoptimind.g8_app.features.salesreport.SalesReportActivity;
import com.myoptimind.g8_app.features.settings.SettingsActivity;
import com.myoptimind.g8_app.features.shared.SharedPref;
import com.myoptimind.g8_app.features.uploadtimeslip.UploadSlipActivity;
import com.myoptimind.g8_app.models.Announcement;
import com.myoptimind.g8_app.models.Store;

import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private String selectedStore = "";

    public static DashboardFragment newInstance() {

        Bundle args = new Bundle();

        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_dashboard,container,false);


        View boxTimeIn              = v.findViewById(R.id.box_inout);
        TextView tvWelcome          = v.findViewById(R.id.tv_welcome_user);
        Button btnLogout            = v.findViewById(R.id.btn_logout);

        View boxAnnouncements       = v.findViewById(R.id.box_announcements);
        View boxPinYourStore        = v.findViewById(R.id.box_pin_your_store);
        View boxSalesReport         = v.findViewById(R.id.box_sales_report);
        View boxUploadTimeSlip      = v.findViewById(R.id.box_upload_from_gallery);
        View boxMessageAdmin        = v.findViewById(R.id.box_message_to_admin);
        View boxViewPayslip         = v.findViewById(R.id.box_payslip);
        View boxSettings            = v.findViewById(R.id.box_settings);

        TextView lblMessageAdmin     = v.findViewById(R.id.label_message_to_admin);
        TextView lblViewPayslip      = v.findViewById(R.id.label_payslip);
        TextView lblPinYourStore     = v.findViewById(R.id.label_pin_your_store);

        ImageView ivMessageAdmin      = v.findViewById(R.id.img_message_to_admin);
        ImageView ivViewPaySlip       = v.findViewById(R.id.img_payslip);
        ImageView ivPinYourStore      = v.findViewById(R.id.img_pin_your_store);


        /**
         * Temporary disable other buttons
         */
        Utils.setEnableViews(
                new View[]{ ivMessageAdmin, ivViewPaySlip, lblViewPayslip, lblMessageAdmin },
                false
        );

        dashboardViewModel = new ViewModelProvider(getActivity()).get(DashboardViewModel.class);

        initAnnouncements();



/*        boxTimeIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = InOutActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });*/

        boxAnnouncements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AnnouncementsActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });


        dashboardViewModel.getStoresOfUser().observe(this.getViewLifecycleOwner(), new Observer<List<Store>>() {
            @Override
            public void onChanged(final List<Store> stores) {
                if(stores.size() > 0){

                    final CharSequence[] choices = new String[stores.size()];

                    for(int i = 0 ; i < stores.size() ; i++){
                        choices[i] = stores.get(i).getStoreName();
                    }



                    boxSalesReport.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedStore = stores.get(0).getUuid();

                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Select Store:")
                                    .setSingleChoiceItems(choices, 0, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            selectedStore = stores.get(which).getUuid();
                                        }
                                    })
                                    .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = SalesReportActivity.newIntent(getActivity(),selectedStore);
                                            startActivity(intent);
                                        }
                                    }).setNegativeButton("Cancel", null)
                                    .create()
                                    .show();

                        }
                    });
                }else{
                    boxSalesReport.setEnabled(false);

                }
            }
        });





        boxUploadTimeSlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = UploadSlipActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });

        boxSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SettingsActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });





/*        dashboardViewModel.getUser().observe(this.getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    tvWelcome.setText(getString(R.string.label_welcome,user.getFirstName()));

                    if(user.getPosition() == User.USER_TYPE_COORDINATOR){
                        boxPinYourStore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = PinYourStoreActivity.newIntent(getActivity());
                                startActivity(intent);
                            }
                        });
                    }else{
                        Utils.setEnableViews(
                                new View[]{ ivPinYourStore,lblPinYourStore },
                                false
                        );
                    }
                }
            }
        });*/

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref sharedPref = SharedPref.getInstance(getActivity());
                dashboardViewModel.clearUser();
                sharedPref.setIdLoggedIn(null);
                sharedPref.clearLastSyncs();
                getActivity().finish();
            }
        });



        return v;
    }

    private void initAnnouncements() {
        dashboardViewModel.getUnreadAnnouncements().observe(this.getViewLifecycleOwner(), new Observer<List<Announcement>>() {
            @Override
            public void onChanged(List<Announcement> announcements) {
                if(announcements.size() > 0){
                    AnnouncementDialogFragment announcementDialogFragment = AnnouncementDialogFragment.newInstance(announcements);
                    announcementDialogFragment.show(getActivity().getSupportFragmentManager(),"ANNOUNCEMENT_DIALOG");
                }
            }
        });
    }

}
