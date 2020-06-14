package com.myoptimind.g8_app.repositories;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.myoptimind.g8_app.db.AppDatabase;
import com.myoptimind.g8_app.db.dao.TimeSlipDao;
import com.myoptimind.g8_app.models.TimeSlip;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class TimeSlipRepository {

    TimeSlipDao mTimeSlipDao;


    public TimeSlipRepository(Context application) {
        mTimeSlipDao = AppDatabase.getInstance(application).timeSlipDao();
    }

    /**
     * Database Requests
     */

    // Fetch

    public LiveData<TimeSlip> getByDate(String userId, String date){
        return mTimeSlipDao.getByDate(userId,"%" + date + "%");
    }

    public Single<TimeSlip> getByDateSync(String userId, String date){
        return mTimeSlipDao.getByDateSync(userId,date);
    }

    public Single<TimeSlip> getFirstCreated(String userId){
        return mTimeSlipDao.getFirstCreated(userId);
    }

    // Insert

    public Maybe<Long> insertTimeSlip(TimeSlip timeSlip){
        return mTimeSlipDao.insertTimeSlip(timeSlip);
    }

    /**
     * Network Requests
     */

}
