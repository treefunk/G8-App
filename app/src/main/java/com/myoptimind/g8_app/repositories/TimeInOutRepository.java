package com.myoptimind.g8_app.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.myoptimind.g8_app.db.AppDatabase;
import com.myoptimind.g8_app.db.dao.TimeInOutDao;
import com.myoptimind.g8_app.models.TimeInOut;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class TimeInOutRepository {

    private static final String TAG = "TimeInOutRepository";


    TimeInOutDao mTimeInOutDao;

    public TimeInOutRepository(Application application) {
        mTimeInOutDao = AppDatabase.getInstance(application).timeInOutDao();
    }

    /**
     * Database Requests
     */

    // Fetch

    public Single<Integer> getTimeInCount(String userId, String datetime){
        return mTimeInOutDao.getByDateTimeIns(userId,"%" + datetime  + "%", TimeInOut.TYPE_TIMEIN);
    }

    public Single<Integer> getTimeOutCount(String userId, String datetime){
        return mTimeInOutDao.getByDateTimeIns(userId,"%" + datetime  + "%", TimeInOut.TYPE_TIMEOUT);
    }

    public LiveData<TimeInOut> getByDate(String userId, String datetime, String type){
        Log.d(TAG, "fetching user id" + userId);
        Log.d(TAG, "datetime " + datetime);
        Log.d(TAG, "type" + type);
        return mTimeInOutDao.getByDate(userId,"%" + datetime  + "%",type);
    }

    public Maybe<TimeInOut> getByDate_(String userId, String datetime, String type){
        Log.d(TAG, "fetching user id" + userId);
        Log.d(TAG, "datetime " + datetime);
        Log.d(TAG, "type" + type);
        return mTimeInOutDao.getByDate_(userId,"%" + datetime  + "%",type);
    }


    // Insert

    public Completable insertTimeInOut(TimeInOut timeInOut){
        return mTimeInOutDao.insertTimeInOut(timeInOut);
    }


    /**
     * Network Requests
     */


}
