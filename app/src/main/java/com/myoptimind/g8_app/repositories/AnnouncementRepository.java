package com.myoptimind.g8_app.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.myoptimind.g8_app.db.AppDatabase;
import com.myoptimind.g8_app.db.dao.AnnouncementDao;
import com.myoptimind.g8_app.db.dao.StoreDao;
import com.myoptimind.g8_app.models.Announcement;
import com.myoptimind.g8_app.models.Store;
import com.myoptimind.g8_app.models.UserAnnouncement;
import com.myoptimind.g8_app.models.UserStore;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AnnouncementRepository {

    private AnnouncementDao mAnnouncementDao;
//    private AnnouncementService mAnnouncementService;

    public AnnouncementRepository(Application application){
        mAnnouncementDao = AppDatabase.getInstance(application).announcementDao();
//        mAnnouncementService = G8Api.createAuthService(); // TODO
    }

    /**
     * Database Requests
     */

    // Fetch

    public LiveData<List<Announcement>> getAllUnreadAnnouncements(String userId) {
        return mAnnouncementDao.getAllUnreadAnnouncementsForUser(userId);
    }

    public Single<UserAnnouncement> getUserAnnouncement(String userId, String announcementId){
        return mAnnouncementDao.getUserAnnouncement(userId,announcementId);
    }

    public LiveData<List<Announcement>> getAllBulletins(){
        return mAnnouncementDao.getAllBulletin();
    }

    public LiveData<List<Announcement>> getAllMemo(){
        return mAnnouncementDao.getAllMemo();
    }



    // Insert

    public Completable insertAnnouncement(Announcement announcement){
        return mAnnouncementDao.insertAnnouncement(announcement);
    }


    public void insertAnnouncement(List<Announcement> announcements){
        mAnnouncementDao.insertAnnouncement(announcements);
    }

    public Completable insertUserAnnouncement(String loggedInId, String announcementId){
        UserAnnouncement userAnnouncement = new UserAnnouncement();
        userAnnouncement.setUserId(loggedInId);
        userAnnouncement.setAnnouncementId(announcementId);
        return mAnnouncementDao.insertUserAnnouncement(userAnnouncement);
    }

    // Delete

    public Completable deleteUserAnnouncement(String loggedInId, String announcementId) {
        UserAnnouncement userAnnouncement = new UserAnnouncement();
        userAnnouncement.setUserId(loggedInId);
        userAnnouncement.setAnnouncementId(announcementId);
        return mAnnouncementDao.deleteUserAnnouncement(userAnnouncement);
    }


    /**
     * Network Requests
     */

}