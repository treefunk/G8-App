package com.myoptimind.g8_app.features.announcementsmemos.bulletin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myoptimind.g8_app.models.Announcement;
import com.myoptimind.g8_app.repositories.AnnouncementRepository;

import java.util.List;

public class BulletinViewModel extends AndroidViewModel {

    AnnouncementRepository mAnnouncementRepository;
    LiveData<List<Announcement>> mBulletins;


    public BulletinViewModel(@NonNull Application application) {
        super(application);
        mAnnouncementRepository = new AnnouncementRepository(application);
        mBulletins = mAnnouncementRepository.getAllBulletins();
    }

    public LiveData<List<Announcement>> getBulletins() {
        return mBulletins;
    }
}

