package com.myoptimind.g8_app.repositories;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.myoptimind.g8_app.db.AppDatabase;
import com.myoptimind.g8_app.db.dao.TimeInOutDao;
import com.myoptimind.g8_app.models.TimeInOut;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class TimeInOutRepository {

    private static final String TAG = "TimeInOutRepository";


    TimeInOutDao mTimeInOutDao;

    public TimeInOutRepository(Context application) {
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

    //for sync
    public Single<TimeInOut> getFirstCreated(String userId){ return mTimeInOutDao.getFirstCreated(userId); }
    public Single<TimeInOut> getByDateForSync(String userId,String datetime){ return mTimeInOutDao.getByDateSync(userId,datetime); }


    // Insert

    public Completable insertTimeInOut(TimeInOut timeInOut){
        return mTimeInOutDao.insertTimeInOut(timeInOut);
    }

    public Completable insertTimeInOut(List<TimeInOut> timeInOuts){
        return mTimeInOutDao.insertTimeInOutList(timeInOuts);
    }


    /**
     * Network Requests
     */


}
