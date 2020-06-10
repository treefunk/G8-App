package com.myoptimind.g8_app.features.salesreport;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myoptimind.g8_app.R;

import java.util.List;

public class SalesDayAdapter extends RecyclerView.Adapter<SalesDayAdapter.SalesDayViewHolder> {

    private List<SalesDay> mSalesDays;

    public class SalesDayViewHolder extends RecyclerView.ViewHolder{

        private TextView tvDay;
        private EditText etSaleValue;

        public SalesDayViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDay       = itemView.findViewById(R.id.tv_day);
            etSaleValue = itemView.findViewById(R.id.et_sales_value);

            etSaleValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mSalesDays.get(getAdapterPosition()).setSalesValue(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        public void bind(final SalesDay salesDay){
            tvDay.setText(salesDay.getDay());
            etSaleValue.setText(salesDay.getSalesValue());
            if(salesDay.getDisabled() ){
                etSaleValue.setEnabled(false);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public SalesDayAdapter(List<SalesDay> salesDays) {
        mSalesDays = salesDays;
    }

    public void setSalesDays(List<SalesDay> salesDays) {
        mSalesDays = salesDays;
    }

    @NonNull
    @Override
    public SalesDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new SalesDayViewHolder(inflater.inflate(R.layout.item_sales_report,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesDayViewHolder holder, int position) {
        holder.bind(mSalesDays.get(position));
    }

    @Override
    public int getItemCount() {
        return mSalesDays.size();
    }

    public interface DayListener {
        void onDay(SalesDay salesDay);
    }
}
