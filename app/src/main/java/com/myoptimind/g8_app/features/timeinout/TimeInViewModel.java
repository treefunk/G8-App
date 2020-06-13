package com.myoptimind.g8_app.features.timeinout;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.myoptimind.g8_app.Utils;
import com.myoptimind.g8_app.features.shared.SharedPref;
import com.myoptimind.g8_app.features.shared.SingleLiveEvent;
import com.myoptimind.g8_app.models.Store;
import com.myoptimind.g8_app.models.TimeInOut;
import com.myoptimind.g8_app.models.User;
import com.myoptimind.g8_app.repositories.StoreRepository;
import com.myoptimind.g8_app.repositories.TimeInOutRepository;
import com.myoptimind.g8_app.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TimeInViewModel extends AndroidViewModel {

    private static final String TAG = "TimeInViewModel";

    private CompositeDisposable mDisposable = new CompositeDisposable();

    // R E P O S I T O R I E S
    private StoreRepository mStoreRepository;
    private TimeInOutRepository mTimeInOutRepository;
    private UserRepository mUserRepository;


    private MutableLiveData<Store> mActiveStore = new MutableLiveData();
    private Store currentStore = null;
    private List<Store> mStores = new ArrayList<>();
    private MutableLiveData<Coordinates> mCoordinates = new MutableLiveData<>();
    private MutableLiveData<Boolean> mCanTimeIn = new MutableLiveData<>();
    private MutableLiveData<Boolean> mCanTimeOut = new MutableLiveData<>();
    private SingleLiveEvent<String> message = new SingleLiveEvent<>();

    private String loggedInId;
    private int mActiveTimeInCount;
    private int mActiveTimeOutCount;
    private int mTimeInOutLimit = 3;
    private int successfullTimeInOuts = 0;
    private User mUser;



    public TimeInViewModel(@NonNull Application application) {
        super(application);

        mStoreRepository = new StoreRepository(application);
        mTimeInOutRepository = new TimeInOutRepository(application);
        mUserRepository = new UserRepository(application);
        loggedInId = SharedPref.getInstance(application).getIdLoggedIn();
        mCoordinates.setValue(null);

        initUser();
        initTimeIns();
        initStores();
    }


    private void initTimeIns() {

        mActiveTimeInCount = mTimeInOutRepository.getTimeInCount(loggedInId, Utils.newDateSqlStringOnlyDate(Utils.newDate())) // gets number of time ins for this day
                .subscribeOn(Schedulers.io())
                .blockingGet();
        mActiveTimeOutCount = mTimeInOutRepository.getTimeOutCount(loggedInId, Utils.newDateSqlStringOnlyDate(Utils.newDate())) // gets number of time outs for this day
                .subscribeOn(Schedulers.io())
                .blockingGet();

        successfullTimeInOuts = (mActiveTimeOutCount - mActiveTimeInCount) + mActiveTimeInCount; // 1 time in & 1 time out = successful time-in time-out

        boolean currentStoreIsNotNull = currentStore != null;
        boolean hasNoLimit = mTimeInOutLimit == -1;

        mCanTimeIn.setValue( // conditions for timing in
                currentStoreIsNotNull && // check if store is detected
                mActiveTimeInCount <= mActiveTimeOutCount && // time in count is should be less than time out count
                ((mActiveTimeInCount < mTimeInOutLimit || hasNoLimit))  // 1. check if time in count exceeds the limit 2. check if this user type has unlimited time ins
        );


        mCanTimeOut.setValue( // conditions for timing out
                currentStoreIsNotNull &&
                        mActiveTimeInCount > mActiveTimeOutCount && // time in count should be greater than time out count
                        ((mActiveTimeOutCount < mTimeInOutLimit || hasNoLimit))
        );

        Log.d(TAG,"COMPLETED TIME INS = " + successfullTimeInOuts);

    }

    public void searchNearestStore() {
         if(mCoordinates.getValue() != null) {

             Coordinates coordinates = mCoordinates.getValue();
             Log.v(TAG, "retrieving store list..");
             double mostNear = -1;
             Store closestStore = null;

             int i = 0;
             for (Store store : mStores) {
                 i++;
                 boolean isFirstStoreDetected = mostNear == -1;
                 boolean isStoreWithoutCoordinates = store.getLatitude() == 0 && store.getLongitude() == 0;

                 if (isStoreWithoutCoordinates) { // skip stores that has 0 lat 0 long
                     continue;
                 }

                 double distance = Utils.getDistancev2( // distance in kilometers
                         coordinates.getLatitude(),
                         coordinates.getLongitude(),
                         store.getLatitude(),
                         store.getLongitude()
                 );

                 Log.d("GPS", "STORE #" + i);
                 Log.d("GPS", store.getStoreName());
                 Log.d("GPS", "distance: " + distance);

                 if (distance <= 1) { //checks if distance between current location and store location is less than the given num (IN KILOMETERS!)
                     if (isFirstStoreDetected || distance < mostNear) { // if it's first store detected there's no previous store to compare with
                         mostNear = distance;
                         closestStore = store;
                     }
                 }
             }
             if(closestStore != null){
                 Log.d("GPS", "Store detected: " + "https://www.google.com/maps/place/" + closestStore.getLatitude() + "," + closestStore.getLongitude());
             }

             mActiveStore.setValue(closestStore); // update live data for ui changes
             currentStore = closestStore; // update local currentStore for operations
             initTimeIns();
         }
    }

    public void initCoordinates(Coordinates coordinates){
        mCoordinates.setValue(coordinates);
        searchNearestStore();
    }

    private void initUser() {
        mUser = mUserRepository.getUser()
                .subscribeOn(Schedulers.io())
                .blockingGet();

        mTimeInOutLimit = mUser.getTimeInLimit();
    }

    private void initStores() {
        if(mUser.isPromodiser()){
            mStores = mStoreRepository.getStoresOfUser(String.valueOf(mUser.getId()))
            .subscribeOn(Schedulers.io())
            .blockingGet();
        } else{
            mStores = mStoreRepository.getAllStores()
            .subscribeOn(Schedulers.io())
            .blockingGet();
        }
    }

    public void clearActiveStore(){
        mActiveStore.setValue(null);
        currentStore = null;
        mCanTimeIn.setValue(false);
    }

    public void recordTimeIn(String type) {

        if(currentStore == null){
            message.setValue("No nearby Stores found.");
            initCoordinates(null);
            return;
        }

        TimeInOut latestTimeIn = mTimeInOutRepository.getByDate_(loggedInId, Utils.newDateSqlStringOnlyDate(Utils.newDate()),TimeInOut.TYPE_TIMEIN)
                .subscribeOn(Schedulers.io())
                .blockingGet();

        TimeInOut timeInOut = new TimeInOut();
        timeInOut.setType(Integer.parseInt(type));
        timeInOut.setStoreId(currentStore.getUuid());
        timeInOut.setUserId(Integer.parseInt(loggedInId));

        if(timeInOut.isTimingOut()){
            if(mUser.isCoordinator() || currentStore.getUuid().equals(latestTimeIn.getStoreId())){

                mTimeInOutRepository.insertTimeInOut(timeInOut)
                        .subscribeOn(Schedulers.io())
                        .blockingAwait();
                message.setValue("Successfully timed out");

            }else{
                message.setValue("Store must be the same from time in.");
            }


        }else{
            mTimeInOutRepository.insertTimeInOut(timeInOut)
                    .subscribeOn(Schedulers.io()).blockingAwait();
            message.setValue("Successfully timed in");
        }

        initTimeIns();
    }


/*    public void filterLocalStores(String s){

        ArrayList<String> filteredLocalStores = new ArrayList<>();
        LocalStoreList localStoreList = mStoreRepository.loadLocalStores(getApplication());

        filteredLocalStores.clear();

        if(!s.trim().isEmpty()){

            for(LocalStore localStore : localStoreList.getLocalStores()){
                if(localStore.getName() != null && localStore.getName().toLowerCase().contains(s.toLowerCase())){
                    filteredLocalStores.add(localStore.getName());
                }
            }

        }else{

            filteredLocalStores.clear();

        }

        mSuggestedStores.setValue(filteredLocalStores);
    }*/



    public LiveData<Store> getActiveStore() {
        return mActiveStore;
    }

    public LiveData<Boolean> getCanTimeIn() {
        return mCanTimeIn;
    }

    public LiveData<Boolean> getCanTimeOut() {
        return mCanTimeOut;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }


}
