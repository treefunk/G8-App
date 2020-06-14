package com.myoptimind.g8_app.features.syncing;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.room.EmptyResultSetException;

import com.myoptimind.g8_app.Utils;
import com.myoptimind.g8_app.api.G8Api;
import com.myoptimind.g8_app.features.shared.SharedPref;
import com.myoptimind.g8_app.features.syncing.response.LastPushDateResponse;
import com.myoptimind.g8_app.features.syncing.response.PaginationResponse;
import com.myoptimind.g8_app.features.syncing.response.PushSalesResponse;
import com.myoptimind.g8_app.features.syncing.response.PushStoreResponse;
import com.myoptimind.g8_app.features.syncing.response.PushTimeInResponse;
import com.myoptimind.g8_app.models.SalesReport;
import com.myoptimind.g8_app.models.Store;
import com.myoptimind.g8_app.models.TimeInOut;
import com.myoptimind.g8_app.repositories.SalesReportRepository;
import com.myoptimind.g8_app.repositories.StoreRepository;
import com.myoptimind.g8_app.repositories.TimeInOutRepository;
import com.myoptimind.g8_app.repositories.UserRepository;

import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class Syncer {

    private static final String TAG = "Syncer";
    private static final String TAG_STORE = TAG + "/stores";
    private static final String TAG_TIMEIN = TAG + "/timeinout";
    private static final String TAG_SALES = TAG + "/sales";

    private static final String EMPTY_DATE = "0000-00-00 00:00:00";

    private static Syncer INSTANCE;

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private MutableLiveData<String> num = new MutableLiveData<>();
    private Context mContext;

    // REPOSITORIES
    private UserRepository mUserRepository;
    private StoreRepository mStoreRepository;
    private TimeInOutRepository mTimeInOutRepository;
    private SalesReportRepository mSalesReportRepository;

    private SharedPref mSharedPref;
    private String loggedInId;
    private SyncService syncService;


    public Syncer(Context context) {
        mContext = context;

        // Initialize repositories
        mUserRepository = new UserRepository(context);
        mStoreRepository = new StoreRepository(context);
        mTimeInOutRepository = new TimeInOutRepository(context);
        mSalesReportRepository = new SalesReportRepository(context);

        mSharedPref = SharedPref.getInstance(context);
        loggedInId = mSharedPref.getIdLoggedIn();
        syncService = G8Api.createSyncService();
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
        // Sync every 2 minutes
        Observable.interval(5, TimeUnit.MINUTES)
                .startWith(10L)
                .doOnNext(n -> {
                        mDisposable.clear();
                        sync();
                })
                .subscribe();
    }

    public void sync(){
        Log.d(TAG,"syncing..");
        // SYNC STORES
        pullStores(mSharedPref.getValueByKey(SharedPref.LAST_SYNC_STORE),0,1000);
        pushStores();
        // SYNC TIME IN TIME OUT
        pullTimeIns(getLastSyncTimeIn());
        pushTimeIns();
        // SYNC SALES
        pushSingleSalesReport();
    }


    /**
     * STORES
     */

    /**
     * Fetch stores from server
     */
    // TODO API REUPLOAD PRESETS OF STORES ADD 10 SEC TO CREATED AT
    public void pullStores(final String startDate, int offset, int limit){
        Log.d(TAG_STORE,"Retrieving stores online..");

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
                    if (pullStoreOffset < paginationResponse.getTotalCount()) {
                        Log.d(TAG,"fetching stores: " +paginationResponse.getOffset() + "/" +paginationResponse.totalCount);
                        pullStores(startDate, pullStoreOffset, paginationResponse.limit);
                    }else{
                        Log.d(TAG, "Store list is up to date.");
                    }
                    if(paginationResponse.getTotalCount() > 0){
                        Store lastStore = pullStoreResponse.getData().get(pullStoreResponse.getData().size() - 1);
                        String latestUpdateDate;
                        if(lastStore.getUpdatedAt().equals(EMPTY_DATE)){
                            latestUpdateDate = lastStore.getCreatedAt();
                        }else{
                            latestUpdateDate = lastStore.getUpdatedAt();
                        }
                        if(!latestUpdateDate.equals(startDate)){
                            Log.d(TAG_STORE,"last update date - " + latestUpdateDate);
                        }
                        String newLastUpdate = Utils.formatDateTimeForSql(Utils.parseSQLDate(latestUpdateDate).plusSeconds(1));
                        mSharedPref.putStringAndApply(SharedPref.LAST_SYNC_STORE,newLastUpdate);
                    }
                })
                .subscribe(pullStoreResponse -> {
                }, throwable -> {
                    Log.e(TAG,"");
                    Log.e(TAG,throwable.getMessage());
                })
        );
    }

    private Observable<LastPushDateResponse> getLastPushOfTable(String tablename){
        return syncService.getLastPushDate(tablename,loggedInId);
    }

    private void pushStores(){
        Log.d(TAG_STORE,"Pushing Stores...");
        pushSingleStore();
    }

    /**
     * Send stores to server
     */
    public void pushSingleStore(){
        mDisposable.add(getLastPushOfTable("store")
                .delaySubscription(2,TimeUnit.SECONDS)
                .concatMapSingle(lastPushDateResponse -> {
                    String lastPushDate = lastPushDateResponse.getData().getLastPushDate();
                    if(!lastPushDate.equals(EMPTY_DATE)){
                        return mStoreRepository.getByDateForSync(loggedInId,lastPushDate);
                    }
                    return mStoreRepository.getFirstCreated(loggedInId);
                }).concatMap(this::getStorePushObservable)
                .subscribeOn(Schedulers.io())
                .subscribe(pushStoreResponse -> {
                    Log.d(TAG_STORE,"pushed " + pushStoreResponse.getData().getStoreName());
                    Log.d(TAG_STORE, "message: " + pushStoreResponse.getMeta().getMessage());
                    pushSingleStore();
                }, e -> {
                    if(e instanceof EmptyResultSetException){
                        Log.d(TAG_STORE, "No stores to push.");
                    }else{
                        Log.e(TAG_STORE,e.getMessage());
                    }
                }, () -> {
                    Log.d(TAG,"Pushing Stores Completed.");
                })
        );
    }

    private Observable<PushStoreResponse> getStorePushObservable(Store store) {
        return syncService.pushStore(
                store.getUuid(),
                store.getStoreName(),
                store.getStoreAddress(),
                String.valueOf(store.getLongitude()),
                String.valueOf(store.getLatitude()),
                String.valueOf(store.getUserId()),
                store.getCreatedAt(),
                store.getUpdatedAt()
        ).subscribeOn(Schedulers.io());
    }

    /**
     *  TIME IN OUT
     */

    private void pushTimeIns(){
        Log.d(TAG_TIMEIN, "pushing timein..");
        pushSingleTimeInOut();
    }

    private void pushSingleTimeInOut() {
        mDisposable.add(syncService.getLastPushDate("time_in_out",loggedInId)
                .delaySubscription(3,TimeUnit.SECONDS)
                .concatMapSingle(lastPushDateResponse -> {
                    String lastPushDate = lastPushDateResponse.getData().getLastPushDate();
                    if (!lastPushDate.equals(EMPTY_DATE)) {
                        return mTimeInOutRepository.getByDateForSync(loggedInId, lastPushDate);
                    }
                    return mTimeInOutRepository.getFirstCreated(loggedInId);
                }).concatMap(this::getTimeInOutPushObservable)
                .subscribeOn(Schedulers.io())
                .subscribe(pushTimeInResponse -> {
                    TimeInOut timeInOut = pushTimeInResponse.getData();
                    Log.d(TAG_TIMEIN, "pushed " + timeInOut.getCreatedAt() + " - " + (timeInOut.getType() == 1 ? "IN" : "OUT"));
                    Log.d(TAG_TIMEIN, "message: " + pushTimeInResponse.getMeta().getMessage());
                    pushSingleTimeInOut();
                }, e -> {
                    if (e instanceof EmptyResultSetException) {
                        Log.d(TAG_TIMEIN, "No Time in to push.");
                    } else {
                        Log.e(TAG_TIMEIN, e.getMessage());
                    }
                }, () -> {
                    Log.d(TAG, "Pushing Time in Completed.");
                })
        );

    }

    private Observable<PushTimeInResponse> getTimeInOutPushObservable(TimeInOut timeInOut) {
        return syncService.pushTimeInOut(
                String.valueOf(timeInOut.getUserId()),
                String.valueOf(timeInOut.getStoreId()),
                timeInOut.getUuid(),
                String.valueOf(timeInOut.getType()),
                timeInOut.getCreatedAt()
        ).subscribeOn(Schedulers.io());
    }

//    new DateTime().withTime(0,0,0,0).minusDays(1).toString("yyyy-MM-dd")
    private void pullTimeIns(String startDate){
        Log.d(TAG_TIMEIN,"Retrieving timeins online..");
        mDisposable.add(syncService.pullTimeIns("time_in_out",startDate,loggedInId)
                .subscribeOn(Schedulers.io())
                .delaySubscription(4,TimeUnit.SECONDS)
                .doOnSuccess(pullResponse -> {
                    mTimeInOutRepository.insertTimeInOut(pullResponse.getData())
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                    PaginationResponse paginationResponse = pullResponse.pagination;
                    if(paginationResponse.getTotalCount() > 0){
                        Log.d(TAG_TIMEIN,"fetched " + paginationResponse.getTotalCount() + " timeins.");
                        TimeInOut lastTimeInOut = pullResponse.getData().get(pullResponse.getData().size() - 1);
                        String latestUpdateDate;
                        if(lastTimeInOut.getUpdatedAt().equals(EMPTY_DATE)){
                            latestUpdateDate = lastTimeInOut.getCreatedAt();
                        }else{
                            latestUpdateDate = lastTimeInOut.getUpdatedAt();
                        }
                        String newLastUpdate = Utils.formatDateTimeForSql(Utils.parseSQLDate(latestUpdateDate).plusSeconds(1));
                        mSharedPref.putStringAndApply(SharedPref.LAST_SYNC_TIMEINOUT,newLastUpdate);
                    }else{
                        Log.d(TAG_TIMEIN, "timeins is up to date.");
                    }
                })
                .subscribe(pullStoreResponse -> {
                }, throwable -> {
                    Log.e(TAG_TIMEIN,"");
                    Log.e(TAG_TIMEIN,throwable.getMessage());
                })
        );
    }

    private String getLastSyncTimeIn(){
        if(!mSharedPref.timeInOutExists()){
             return new DateTime().withTime(0,0,0,0).minusDays(1).toString("yyyy-MM-dd HH:mm:ss");
        }
        return  Utils.parseSQLDate(mSharedPref.getValueByKey(SharedPref.LAST_SYNC_TIMEINOUT)).toString("yyyy-MM-dd HH:mm:ss");
    }

    /**
     *
     *  SALES REPORT
     */

    private void pushSalesReports(){
        pushSingleSalesReport();
    }

    private void pushSingleSalesReport(){
        mDisposable.add(getLastPushOfTable("sales")
                .delaySubscription(5,TimeUnit.SECONDS)
                .concatMapSingle(lastPushDateResponse -> {
                    String lastPushDate = lastPushDateResponse.getData().getLastPushDate();
                    if(!lastPushDate.equals(EMPTY_DATE)){
                        return mSalesReportRepository.getByDateSync(loggedInId,lastPushDate);
                    }
                    return mSalesReportRepository.getFirstCreated(loggedInId);
                }).concatMap(this::getSalesReportObservable)
                .subscribeOn(Schedulers.io())
                .subscribe(pushStoreResponse -> {
                    Log.d(TAG_SALES,"pushed " + pushStoreResponse.getData().getSales() + " sales " + pushStoreResponse.getData().getDatetime() );
                    Log.d(TAG_SALES, "message: " + pushStoreResponse.getMeta().getMessage());
                    pushSingleSalesReport();
                }, e -> {
                    if(e instanceof EmptyResultSetException){
                        Log.d(TAG_SALES, "No salesreport to push.");
                    }else{
                        Log.e(TAG_SALES,e.getMessage());
                    }
                }, () -> {
                    Log.d(TAG,"Pushing Sales Report Completed.");
                })
        );
    }

    private Observable<PushSalesResponse> getSalesReportObservable(SalesReport salesReport) {
        return syncService.pushSales(
                salesReport.getUuid(),
                salesReport.getUserId(),
                salesReport.getDatetime(),
                salesReport.getSales(),
                salesReport.getStoreUuid(),
                salesReport.getCreatedAt(),
                ""
        ).subscribeOn(Schedulers.io());
    }






    public void clearDisposables(){
        mDisposable.clear();
    }
}
