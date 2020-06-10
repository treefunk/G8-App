package com.myoptimind.g8_app.features.uploadtimeslip;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myoptimind.g8_app.features.shared.SharedPref;
import com.myoptimind.g8_app.models.TimeSlip;
import com.myoptimind.g8_app.repositories.TimeSlipRepository;

import org.joda.time.DateTime;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UploadSlipViewModel extends AndroidViewModel {

    private static final String TAG = "UploadSlipViewModel";
    private CompositeDisposable mDisposable = new CompositeDisposable();

    TimeSlipRepository mTimeSlipRepository;
    LiveData<TimeSlip> mActiveTimeSlip;

    String loggedInId;

    public UploadSlipViewModel(@NonNull Application application) {

        super(application);
        mTimeSlipRepository = new TimeSlipRepository(application);
        loggedInId = SharedPref.getInstance(application).getIdLoggedIn();

//        doSync(application);

        mActiveTimeSlip = mTimeSlipRepository.getByDate(loggedInId, new DateTime().toString("yyyy-MM-dd"));
    }

/*    public void doSync(Application application) {
        pushUploadSlip(application, new SuccessListener() {
            @Override
            public void onSuccess(Boolean success) {
                Log.v(TAG,"Finished Syncing");
            }
        });
    }*/

/*    public void pushSingleUploadSlip(final Application application, final SuccessListener successListener){
        ApiHandler.getInstance().getLastSyncDate("timeslips",
                SharedPref.getIdLoggedIn(application),
                new ApiHandler.SyncDateListener() {
                    @Override
                    public void onSuccess(String date) {
                        TimeSlip timeSlip = null;
                        if(!date.equals("0000-00-00 00:00:00")){
                            timeSlip = mTimeSlipRepository.getByDateSync(loggedInId,date);
                        }else{
                            timeSlip = mTimeSlipRepository.getFirstCreated(loggedInId);
                        }
                        if(timeSlip != null){
                            ApiHandler.getInstance().pushTimeSlip(timeSlip, new ApiHandler.TimeSlipListener() {
                                @Override
                                public void onSuccess(TimeSlip ts) {
                                    if(ts != null){
                                        pushSingleUploadSlip(application,successListener);
                                    }else{
                                        successListener.onSuccess(true);
                                    }
                                }

                                @Override
                                public void onError(String message) {
                                    Log.v(TAG,"pushtimein" + message);
                                }
                            });
                        }else{
                            successListener.onSuccess(true);
                        }

                    }

                    @Override
                    public void onError(String message) {
                        Log.v(TAG,"syncdate " + message);
                    }
                });
    }*/

/*    public void pushUploadSlip(Application application, final SuccessListener successListener){
        pushSingleUploadSlip(application, new SuccessListener() {
            @Override
            public void onSuccess(Boolean success) {
                if(success){
                    successListener.onSuccess(true);
                }
            }
        });
    }*/

    public void insertTimeSlip(String imageName){

        TimeSlip timeSlip = new TimeSlip();
        timeSlip.setUserId(loggedInId);
        timeSlip.setTimecard(imageName);

        mDisposable.add(
                mTimeSlipRepository.insertTimeSlip(timeSlip).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG,"successfully inserted timeslip");
                    }
                })
        );
    }

    public LiveData<TimeSlip> getActiveTimeSlip() {
        return mActiveTimeSlip;
    }
}