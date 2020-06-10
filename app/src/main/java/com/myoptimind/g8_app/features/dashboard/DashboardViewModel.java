package com.myoptimind.g8_app.features.dashboard;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.myoptimind.g8_app.features.shared.SharedPref;
import com.myoptimind.g8_app.models.Announcement;
import com.myoptimind.g8_app.models.Store;
import com.myoptimind.g8_app.models.User;
import com.myoptimind.g8_app.models.UserAnnouncement;
import com.myoptimind.g8_app.repositories.AnnouncementRepository;
import com.myoptimind.g8_app.repositories.StoreRepository;
import com.myoptimind.g8_app.repositories.UserRepository;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DashboardViewModel extends AndroidViewModel {

    private static final String TAG = "DashboardViewModel";

    private CompositeDisposable mDisposable = new CompositeDisposable();

    private AnnouncementRepository mAnnouncementRepository;
    private UserRepository mUserRepository;
    private MediatorLiveData<List<Announcement>> mUnreadAnnouncements;
    private LiveData<List<Announcement>> mUnread;
    private LiveData<User> mUser;
    private LiveData<List<Store>> mStoresOfUser;
    private String loggedInId;


    private MutableLiveData<UserAnnouncement> mActiveUserAnnouncement;
    private StoreRepository mStoreRepository;

    private MutableLiveData<String> mActiveAnnouncementId;

    public DashboardViewModel(@NonNull Application application) {
        super(application);

        mAnnouncementRepository = new AnnouncementRepository(application);
        mUserRepository         = new UserRepository(application);
        mStoreRepository        = new StoreRepository(application);

        mActiveAnnouncementId = new MutableLiveData<>();
        mActiveUserAnnouncement = new MutableLiveData<>();
        loggedInId = SharedPref.getInstance(application).getIdLoggedIn();


        mUnread = mAnnouncementRepository.getAllUnreadAnnouncements(loggedInId);

        mUnreadAnnouncements = new MediatorLiveData<>();
        mUnreadAnnouncements.addSource(mUnread, new Observer<List<Announcement>>() {
            @Override
            public void onChanged(List<Announcement> announcements) {
                mUnreadAnnouncements.setValue(announcements);
                mUnreadAnnouncements.removeSource(mUnread);
            }
        });

        mUser = mUserRepository.getUserLivedata();
        mStoresOfUser = mStoreRepository.getStoresOfUser(loggedInId);
    }

    public void setActiveAnnouncementId(String activeAnnouncementId) {
        mActiveAnnouncementId.setValue(activeAnnouncementId);

        mDisposable.add(mAnnouncementRepository.getUserAnnouncement(loggedInId, activeAnnouncementId)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<UserAnnouncement>() {
                    @Override
                    public void accept(UserAnnouncement userAnnouncement) throws Exception {
                        mActiveUserAnnouncement.postValue(userAnnouncement);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mActiveUserAnnouncement.postValue(null);
                    }
                })
        );
    }

    public void updateAnnouncement(Announcement announcement){
        mAnnouncementRepository.insertAnnouncement(announcement)
        .subscribeOn(Schedulers.io())
        .subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"UPDATED");
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public LiveData<UserAnnouncement> getActiveUserAnnouncement() {
        return mActiveUserAnnouncement;
    }



    public void clearUser() {
        mUserRepository.clearUser();
    }

    public void insertUserAnnouncement(String announcementId) {
        mAnnouncementRepository.insertUserAnnouncement(loggedInId, announcementId)
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG,"inserted user announcement");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void deleteUserAnnouncement(String announcementId) {
        mAnnouncementRepository.deleteUserAnnouncement(loggedInId, announcementId)
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG,"DELETED USER ANNOUNCEMENT");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public LiveData<List<Announcement>> getUnreadAnnouncements() {
        return mUnreadAnnouncements;
    }

    public LiveData<User> getUser() {
        return mUser;
    }

    public LiveData<List<Announcement>> getUnread() {
        return mUnread;
    }

    public LiveData<List<Store>> getStoresOfUser() {
        return mStoresOfUser;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}