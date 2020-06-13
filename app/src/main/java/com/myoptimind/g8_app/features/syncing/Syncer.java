package com.myoptimind.g8_app.features.syncing;

import android.content.Context;
import android.util.Log;
import android.util.MalformedJsonException;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.myoptimind.g8_app.Utils;
import com.myoptimind.g8_app.api.ErrorResponse;
import com.myoptimind.g8_app.api.G8Api;
import com.myoptimind.g8_app.features.shared.SharedPref;
import com.myoptimind.g8_app.features.syncing.response.LastPushDateResponse;
import com.myoptimind.g8_app.features.syncing.response.PaginationResponse;
import com.myoptimind.g8_app.models.Store;
import com.myoptimind.g8_app.repositories.StoreRepository;
import com.myoptimind.g8_app.repositories.UserRepository;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class Syncer {

    private static final String TAG = "Syncer";
    private static final String TAG_STORE = TAG + "/stores";

    private static Syncer INSTANCE;

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private MutableLiveData<String> num = new MutableLiveData<>();
    private Context mContext;
    private UserRepository mUserRepository;
    private StoreRepository mStoreRepository;
    private SharedPref mSharedPref;
    private Boolean isPulling = false;
    private String loggedInId;


    public Syncer(Context context) {
        mContext = context;
        mUserRepository = new UserRepository(context);
        mStoreRepository = new StoreRepository(context);
        mSharedPref = SharedPref.getInstance(context);
        loggedInId = mSharedPref.getIdLoggedIn();
    }

    public static Syncer getInstance(final Context context) {
        if(INSTANCE == null){
            synchronized (Syncer.class) {
                if(INSTANCE == null){
                    INSTANCE = new Syncer(context);
                }
            }
        }
        return INSTANCE;
    }



    public void start() {
        if(!isPulling){
            pullStores(mSharedPref.getValueByKey(SharedPref.LAST_SYNC_STORE),0,1);
        }

//        Log.d(TAG,getLastPushDate("store"););
        getLastPushDate("store");


    }

    // TODO API REUPLOAD PRESETS OF STORES ADD 10 SEC TO CREATED AT
    public void pullStores(final String startDate, int offset, int limit){
        Log.d(TAG_STORE,"Retrieving stores online..");
        isPulling = true;
        String off = String.valueOf(offset);
        String lim  = String.valueOf(limit);

        mDisposable.add(mStoreRepository.getStoresFromRemote(startDate,off,lim)
                .subscribeOn(Schedulers.io())
                .delaySubscription(1,TimeUnit.SECONDS)
                .doOnSuccess(pullStoreResponse -> {
                    mStoreRepository.insertStore(pullStoreResponse.getData())
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                    PaginationResponse paginationResponse = pullStoreResponse.pagination;
                    int pullStoreOffset = paginationResponse.getOffset() + paginationResponse.getLimit() + 1;
                    Log.d(TAG,"offset - " +paginationResponse.getOffset() + " count - " +paginationResponse.totalCount);
                    if (pullStoreOffset < paginationResponse.getTotalCount()) {
                        pullStores(startDate, pullStoreOffset, paginationResponse.limit);
                    }
                    if(paginationResponse.getTotalCount() > 0){
                        Store lastStore = pullStoreResponse.getData().get(pullStoreResponse.getData().size() - 1);
                        String latestUpdateDate;
                        num.postValue(pullStoreResponse.pagination.getOffset() + pullStoreResponse.pagination.getLimit() + 1 +"/" + pullStoreResponse.pagination.totalCount);
                        if(lastStore.getUpdatedAt().equals("0000-00-00 00:00:00")){
                            latestUpdateDate = lastStore.getCreatedAt();
                        }else{
                            latestUpdateDate = lastStore.getUpdatedAt();
                        }
                        if(!latestUpdateDate.equals(startDate)){
                            Log.d(TAG_STORE,"last update date - " + latestUpdateDate);
                        }
                        String newLastUpdate = Utils.formatDateTimeForSql(Utils.parseSQLDate(latestUpdateDate).plusSeconds(1));
                        mSharedPref.putStringAndApply(SharedPref.LAST_SYNC_STORE,newLastUpdate);
                    }else{
                        num.postValue(null);
                    }
                })
                .subscribe(pullStoreResponse -> {
                }, throwable -> {
                    Log.e(TAG,"");
                    Log.e(TAG,throwable.getMessage());
                })
        );
    }

    public void getLastPushDate(String tablename){
        SyncService syncService = G8Api.createSyncService();
/*        mDisposable.add(syncService.getLastPushDate(tablename,loggedInId)
                .concatMap(new Function<LastPushDateResponse, Publisher<?>>() {
                    @Override
                    public Publisher<?> apply(LastPushDateResponse lastPushDateResponse) throws Exception {
                        return null;
                    }
                })*/

//        syncService.getLastPushDate(tablename,loggedInId)
//                .flatMap(lastPushDateResponse -> {
//                    return Observable.just(lastPushDateResponse.getData().getLastPushDate());
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Observable<String>>() {
//                    @Override
//                    public void accept(Observable<String> stringObservable) throws Exception {
//
//                    }
//                });
    }




    public LiveData<String> getNum() {
        return num;
    }

    public void clearDisposables(){
        isPulling = false;
        mDisposable.clear();
    }
}
