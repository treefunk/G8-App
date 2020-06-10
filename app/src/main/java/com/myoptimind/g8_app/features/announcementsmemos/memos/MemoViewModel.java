package com.myoptimind.g8_app.features.announcementsmemos.memos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myoptimind.g8_app.models.Announcement;
import com.myoptimind.g8_app.repositories.AnnouncementRepository;

import java.util.List;

public class MemoViewModel extends AndroidViewModel {

    private AnnouncementRepository mAnnouncementRepository;
    private LiveData<List<Announcement>> mMemos;

    public MemoViewModel(@NonNull Application application) {
        super(application);
        mAnnouncementRepository = new AnnouncementRepository(application);
        mMemos = mAnnouncementRepository.getAllMemo();
    }

    public LiveData<List<Announcement>> getMemos() {
        return mMemos;
    }
}
