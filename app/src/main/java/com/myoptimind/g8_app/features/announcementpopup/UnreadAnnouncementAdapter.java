package com.myoptimind.g8_app.features.announcementpopup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
;import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.models.Announcement;

import java.util.List;

public class UnreadAnnouncementAdapter extends RecyclerView.Adapter<UnreadAnnouncementAdapter.UnreadAnnouncementHolder> {

    private static final String TAG = "UnreadAdapter";

    List<Announcement> mAnnouncements;
    BindListener mBindListener;

    public class UnreadAnnouncementHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle,tvBody;

        public UnreadAnnouncementHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_announcement_title);
            tvBody  = itemView.findViewById(R.id.tv_announcement_body);
        }

        public void bind(Announcement announcement){
            tvTitle.setText(announcement.getTitle());
            tvBody.setText(announcement.getContent());
        }
    }

    public UnreadAnnouncementAdapter(List<Announcement> announcements,BindListener bindListener) {
        mAnnouncements = announcements;
        mBindListener = bindListener;
    }

    @NonNull
    @Override
    public UnreadAnnouncementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new UnreadAnnouncementHolder(inflater.inflate(R.layout.item_dialog_announcement,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UnreadAnnouncementHolder holder, int position) {
        Announcement announcement = mAnnouncements.get(position);
        holder.bind(announcement);
        mBindListener.onBind(announcement,position);
    }

    @Override
    public int getItemCount() {
        return mAnnouncements.size();
    }

    public interface BindListener{
        void onBind(Announcement announcement,int position);
    }

}
