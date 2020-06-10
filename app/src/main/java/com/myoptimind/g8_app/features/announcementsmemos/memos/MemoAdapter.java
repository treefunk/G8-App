package com.myoptimind.g8_app.features.announcementsmemos.memos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.models.Announcement;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoHolder> {

    private final static int UNREAD_MEMO = 1;
    private final static int READ_MEMO   = 2;

    private List<Announcement> mMemos;
    private ClickListener mClickListener;

    public class MemoHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle,tvBody,tvTime;

        public MemoHolder(@NonNull View itemView, final ClickListener clickListener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_memo_title);
            tvBody  = itemView.findViewById(R.id.tv_memo_body);
            tvTime  = itemView.findViewById(R.id.tv_memo_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClickItem(mMemos.get(getAdapterPosition()),getAdapterPosition());
                }
            });

        }

        public void bind(Announcement announcement){
            tvTitle.setText(announcement.getTitle());
            tvBody.setText(announcement.getExcerpt());
            tvTime.setText(announcement.getDuration());
        }
    }

    public MemoAdapter(List<Announcement> memos,ClickListener clickListener) {

        mMemos = memos;
        mClickListener = clickListener;
    }

    public void setMemos(List<Announcement> memos) {
        mMemos = memos;
    }

    @Override
    public int getItemViewType(int position) {
//        switch(mMemos.get(position).getId() == 1){
//            //TODO update viewtypes
//        }

        if(position == 0){
            return READ_MEMO;
        }else{
            return UNREAD_MEMO;
        }
    }

    @NonNull
    @Override
    public MemoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = null;
        switch (viewType){
            case UNREAD_MEMO:
                return new MemoHolder(inflater.inflate(R.layout.item_memo_unread,parent,false),mClickListener);
            case READ_MEMO:
                return new MemoHolder(inflater.inflate(R.layout.item_memo,parent,false),mClickListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MemoHolder holder, int position) {
        holder.bind(mMemos.get(position));
    }

    @Override
    public int getItemCount() {
        return mMemos.size();
    }

    public interface ClickListener{
        void onClickItem(Announcement announcement, int position);
    }

}
