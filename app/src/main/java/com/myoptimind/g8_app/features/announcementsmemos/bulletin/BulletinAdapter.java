package com.myoptimind.g8_app.features.announcementsmemos.bulletin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.models.Announcement;

import java.util.List;

public class BulletinAdapter extends RecyclerView.Adapter<BulletinAdapter.BulletinHolder> {

    List<Announcement> mAnnouncements;
    private ClickListener mClickListener;

    public class BulletinHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle,tvBody;
        private ImageView ivImage;


        public BulletinHolder(@NonNull View itemView, final ClickListener clickListenerHolder) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_bulletin_title);
            tvBody  = itemView.findViewById(R.id.tv_bulletin_body);
            ivImage = itemView.findViewById(R.id.iv_bulletin_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListenerHolder.onClickItem(mAnnouncements.get(getAdapterPosition()),getAdapterPosition());
                }
            });
        }

        public void bind(Announcement announcement){
            tvTitle.setText(announcement.getTitle());
            tvBody.setText(announcement.getExcerpt());
            if(announcement.getFeaturedImage().isEmpty()){
                ivImage.setImageResource(R.drawable.sample);
            }
        }

    }

    public BulletinAdapter(List<Announcement> announcements,ClickListener clickListener) {
        mAnnouncements = announcements;
        mClickListener = clickListener;
    }

    @NonNull
    @Override
    public BulletinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new BulletinHolder(inflater.inflate(R.layout.item_bulletin,parent,false),mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BulletinHolder holder, int position) {
        holder.bind(mAnnouncements.get(position));
    }

    @Override
    public int getItemCount() {
        return mAnnouncements.size();
    }

    public interface ClickListener{
        void onClickItem(Announcement announcement,int position);
    }
}
