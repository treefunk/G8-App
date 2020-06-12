package com.myoptimind.g8_app.features.settings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myoptimind.g8_app.features.shared.SharedPref;
import com.myoptimind.g8_app.models.Store;
import com.myoptimind.g8_app.models.User;
import com.myoptimind.g8_app.repositories.StoreRepository;
import com.myoptimind.g8_app.repositories.UserRepository;

import java.util.List;

public class SettingsViewModel extends AndroidViewModel
{
    private UserRepository mUserRepository;
    private StoreRepository mStoreRepository;

    private LiveData<User> mUser;
    private LiveData<List<Store>> mStores;
    private String mLoggedInId;

    public SettingsViewModel(@NonNull Application application) {

        super(application);

        mUserRepository  = new UserRepository(application);
        mStoreRepository = new StoreRepository(application);

        mLoggedInId = SharedPref.getInstance(application).getIdLoggedIn();

        mUser   = mUserRepository.getUserLivedata();
        mStores = mStoreRepository.getStoresOfUserLive(mLoggedInId);
    }

    public LiveData<User> getUser() {
        return mUser;
    }

    public LiveData<List<Store>> getStores() {
        return mStores;
    }
}

