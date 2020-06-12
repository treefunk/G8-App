package com.myoptimind.g8_app.features.pinyourstore;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.myoptimind.g8_app.Utils;
import com.myoptimind.g8_app.db.AppDatabase;
import com.myoptimind.g8_app.features.shared.SharedPref;
import com.myoptimind.g8_app.features.shared.SingleLiveEvent;
import com.myoptimind.g8_app.features.timeinout.Coordinates;
import com.myoptimind.g8_app.models.Store;
import com.myoptimind.g8_app.models.User;
import com.myoptimind.g8_app.repositories.StoreRepository;
import com.myoptimind.g8_app.repositories.UserRepository;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class PinYourStoreViewModel extends AndroidViewModel {

    private StoreRepository mStoreRepository;
    private UserRepository mUserRepository;
    private SingleLiveEvent<String> message = new SingleLiveEvent();
    private MutableLiveData<Boolean> mCanEnterStore = new MutableLiveData<>();
    private MutableLiveData<List<Store>> mStores = new MutableLiveData<>();

    private String loggedIn;
    private User mUser;
    private Coordinates mCoordinates = null;

    public PinYourStoreViewModel(@NonNull Application application) {
        super(application);
        mStoreRepository = new StoreRepository(application);
        mUserRepository  = new UserRepository(application);
        mCanEnterStore.setValue(false);
        loggedIn = SharedPref.getInstance(application).getIdLoggedIn();

        initUser();
        initStores();
    }

    public void setCoordinates(Coordinates coordinates) {
        mCoordinates = coordinates;
        mCanEnterStore.setValue(mCoordinates != null);
    }

    private void initUser(){
        mUser = mUserRepository.getUser().subscribeOn(Schedulers.io())
                .blockingGet();
    }

    private void initStores() {
        if(mUser.isPromodiser()){
            mStores.setValue(mStoreRepository.getStoresOfUser(String.valueOf(mUser.getId()))
                    .subscribeOn(Schedulers.io())
                    .blockingGet());
        } else{
            mStores.setValue(mStoreRepository.getAllStores()
                    .subscribeOn(Schedulers.io())
                    .blockingGet());
        }
    }

    public void addStore(String storeName, String storeAddress){

        if(mCoordinates == null){
            message.setValue("Sorry, please enable your gps first before pinning a store");
            return;
        }

        if(storeName.trim().isEmpty()){
            message.setValue("Store name is required.");
            return;
        }

        Store store = new Store();
        store.setLatitude(mCoordinates.getLatitude());
        store.setLongitude(mCoordinates.getLongitude());
        store.setStoreName(storeName);
        store.setStoreAddress(storeAddress);
        store.setUserId(Integer.parseInt(loggedIn));


        String result = mStoreRepository.addStoreInformation(store);

        message.setValue(result);
    }

    public LiveData<Boolean> getCanEnterStore() {
        return mCanEnterStore;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public LiveData<List<Store>> getStores() {
        return mStores;
    }
}
