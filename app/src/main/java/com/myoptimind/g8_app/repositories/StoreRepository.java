package com.myoptimind.g8_app.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.myoptimind.g8_app.Utils;
import com.myoptimind.g8_app.api.G8Api;
import com.myoptimind.g8_app.db.AppDatabase;
import com.myoptimind.g8_app.db.dao.StoreDao;
import com.myoptimind.g8_app.models.LocalStore;
import com.myoptimind.g8_app.models.LocalStoreList;
import com.myoptimind.g8_app.models.Store;
import com.myoptimind.g8_app.models.UserStore;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

public class StoreRepository {

    private StoreDao mStoreDao;
    public static final int STRAT_REPLACE = 1;
    public static final int STRAT_IGNORE  = 2;
    private static final String LOCAL_STORE_JSON = "stores.json";
//    private StoreService mStoreService;

    public StoreRepository(Application application){
        mStoreDao = AppDatabase.getInstance(application).storeDao();
//        mAuthService = G8Api.createAuthService(); // TODO
    }

    /**
     *  Local Files
     */

    public LocalStoreList loadLocalStores(Application application){

        String json = null;

        try{
            InputStream is = application.getAssets().open(LOCAL_STORE_JSON);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, StandardCharsets.UTF_8);

        }catch (IOException ioException){
            ioException.printStackTrace();
            return null;
        }


        LocalStore[] localStores = new Gson().fromJson(json, LocalStore[].class);

        LocalStoreList localStoreList = new LocalStoreList();
        localStoreList.setLocalStores(Arrays.asList(localStores));


        return localStoreList;
    }

    /**
     * Database Requests
     */

    // Fetch
    public LiveData<List<Store>> getAllStoresLive(){ return mStoreDao.getAllLive(); }
    public LiveData<List<Store>> getStoresOfUserLive(String userId) { return mStoreDao.getUserStoresLive(userId); }

    public Maybe<List<Store>> getAllStores(){ return mStoreDao.getAll(); }
    public Maybe<List<Store>> getStoresOfUser(String userId) { return mStoreDao.getUserStores(userId); }
    public Maybe<Store> getStoreByName(String storename){ return mStoreDao.getStoreByName(storename); }


    // Insert

    public String addStoreInformation(Store store){

        Store existingStore = getStoreByName(store.getStoreName())
                .subscribeOn(Schedulers.io())
                .blockingGet();

        if(existingStore != null){
            store.setUuid(existingStore.getUuid());
            store.setCreatedAt(existingStore.getCreatedAt());
            store.setUpdatedAt(Utils.newDateSqlString());
        }

        insertStore(store)
                .subscribeOn(Schedulers.io())
                .blockingAwait();

        return "Store Successfully added!";


    }

    public Completable insertStore(Store store){
        return mStoreDao.insertStore(store);
    }

    public void insertStore(List<Store> stores){
        mStoreDao.insertStoreList(stores);
    }

    public void insertStore(List<Store> stores, int stratType){
        if(stratType == STRAT_REPLACE){
            mStoreDao.insertStoreList(stores);
        }else if(stratType == STRAT_IGNORE){
            mStoreDao.insertStoreListIgnoreStrat(stores);
        }
    }


    public void insertUserStore(List<UserStore> userStores){
        mStoreDao.insertUserStore(userStores);
    }

    public void insertUserStore(UserStore userStores){
        mStoreDao.insertUserStore(userStores);
    }

    public void clearUserStores(){
        mStoreDao.clearStoreOwners();
    }


    /**
     * Network Requests
     */

}
