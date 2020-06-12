package com.myoptimind.g8_app.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.myoptimind.g8_app.api.G8Api;
import com.myoptimind.g8_app.db.AppDatabase;
import com.myoptimind.g8_app.db.dao.UserDao;
import com.myoptimind.g8_app.features.login.AuthService;
import com.myoptimind.g8_app.models.User;

import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class UserRepository {

    private UserDao mUserDao;
    private AuthService mAuthService;

    public UserRepository(Application application){
        mUserDao = AppDatabase.getInstance(application).userDao();
        mAuthService = G8Api.createAuthService();
    }

    /**
     * Database Requests
     */

    //fetch

    public LiveData<User> getUserLivedata() {
        return mUserDao.getUserLive();
    }

    public Single<User> getUser(){ return mUserDao.getUser(); }

    //insert
    public Long insertUser(User user){
        return mUserDao.insertUser(user);
    }


    //delete
    public Completable clearUser() {
        return mUserDao.clearUsers();
    }


    /**
     * Network Requests
     */

    public Flowable<AuthService.AuthResponse> authenticateUser(
            String employeeNumber,
            String password
    ){
        return mAuthService.authenticateUser(employeeNumber,password);
    }


}
