package com.myoptimind.g8_app.features.salesreport;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myoptimind.g8_app.R;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class SalesUpdateFragment extends Fragment {

    private static final String BUNDLE_STORE_UUID = "BUNDLE_STORE_UUID";

    public static SalesUpdateFragment newInstance(String storeUuid) {

        Bundle args = new Bundle();
        SalesUpdateFragment fragment = new SalesUpdateFragment();
        args.putString(BUNDLE_STORE_UUID, storeUuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sales_update, container, false);

        RecyclerView rvSalesReport = v.findViewById(R.id.rv_sales_report);
        ImageButton ibLeftArrow = v.findViewById(R.id.ib_left_arrow);
        ImageButton ibRightArrow = v.findViewById(R.id.ib_right_arrow);
        TextView tvMonthYear = v.findViewById(R.id.tv_month_year);
        Button btnUpdate = v.findViewById(R.id.btn_update);
        ImageView ivBackUpdate = v.findViewById(R.id.back_update);

        SalesViewModelFactory factory = new SalesViewModelFactory(requireActivity().getApplication(), getArguments().getString(BUNDLE_STORE_UUID));

        final SalesViewModel salesViewModel = new ViewModelProvider(getActivity(), factory).get(SalesViewModel.class);

        ibLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salesViewModel.decrementMonth();
            }
        });

        ibRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salesViewModel.incrementMonth();
            }
        });

        salesViewModel.getStringMonthYear().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String monthYear) {
                tvMonthYear.setText(monthYear);
                if (!monthYear.equals(new DateTime().toString("MMMM yyyy"))) {
                    btnUpdate.setVisibility(View.GONE);
                    ivBackUpdate.setVisibility(View.GONE);
                } else {
                    btnUpdate.setVisibility(View.VISIBLE);
                    ivBackUpdate.setVisibility(View.VISIBLE);
                }
            }
        });


        rvSalesReport.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        SalesDayAdapter salesDayAdapter = new SalesDayAdapter(new ArrayList<>());
        rvSalesReport.setAdapter(salesDayAdapter);

        salesViewModel.getSaleDays().observe(this.getViewLifecycleOwner(), new Observer<List<SalesDay>>() {
            @Override
            public void onChanged(List<SalesDay> salesDays) {
                salesDayAdapter.setSalesDays(salesDays);
                salesDayAdapter.notifyDataSetChanged();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salesViewModel.updateSales();
                new AlertDialog.Builder(getActivity())
                        .setMessage("Sales Report Successfully Updated.")
                        .setPositiveButton("Ok", null)
                        .create()
                        .show();

            }
        });


        return v;
    }
}