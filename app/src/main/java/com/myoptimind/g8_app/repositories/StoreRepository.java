package com.myoptimind.g8_app.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.myoptimind.g8_app.api.G8Api;
import com.myoptimind.g8_app.db.AppDatabase;
import com.myoptimind.g8_app.db.dao.StoreDao;
import com.myoptimind.g8_app.models.Store;
import com.myoptimind.g8_app.models.UserStore;

import java.util.List;

import io.reactivex.Completable;

public class StoreRepository {

    private StoreDao mStoreDao;
    public static final int STRAT_REPLACE = 1;
    public static final int STRAT_IGNORE  = 2;
//    private StoreService mStoreService;

    public StoreRepository(Application application){
        mStoreDao = AppDatabase.getInstance(application).storeDao();
//        mAuthService = G8Api.createAuthService(); // TODO
    }

    /**
     * Database Requests
     */

    // Fetch
    public LiveData<List<Store>> getStoresOfUser(String userId) { return mStoreDao.getUserStores(userId); }


    // Insert

    public Long insertStore(Store store){
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
