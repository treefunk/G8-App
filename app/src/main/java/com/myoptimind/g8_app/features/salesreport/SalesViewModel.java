package com.myoptimind.g8_app.features.salesreport;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.myoptimind.g8_app.Utils;
import com.myoptimind.g8_app.features.shared.SharedPref;
import com.myoptimind.g8_app.models.SalesReport;
import com.myoptimind.g8_app.repositories.SalesReportRepository;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SalesViewModel extends AndroidViewModel {

    private static final String TAG = "SalesViewModel";
    private CompositeDisposable mDisposable = new CompositeDisposable();

    SalesReportRepository mSalesReportRepository;

    private MutableLiveData<List<SalesDay>> mSaleDays;
    private MutableLiveData<DateTime> mActiveMonthYear;
    private MediatorLiveData<String> mStringMonthYear;
    private String loggedInId;
    private String selectedStoreUuid;

    public SalesViewModel(@NonNull Application application, String storeUuid) {
        super(application);
        selectedStoreUuid = storeUuid;
        mSaleDays        = new MutableLiveData<>();
        mActiveMonthYear = new MutableLiveData<>();
        mStringMonthYear = new MediatorLiveData<>();

//        mSaleDays.setValue(new ArrayList<SalesDay>());

        loggedInId = SharedPref.getInstance(application).getIdLoggedIn();
        mSalesReportRepository = new SalesReportRepository(application);

/*        doSync(application, new SuccessListener() {
            @Override
            public void onSuccess(Boolean success) {

            }
        });*/


        mStringMonthYear.addSource(mActiveMonthYear, new Observer<DateTime>() {
            @Override
            public void onChanged(DateTime activeMonthYear) {
                mStringMonthYear.setValue(activeMonthYear.toString("MMMM yyyy"));
            }
        });

        mActiveMonthYear.setValue(new DateTime());

        setCurrentMonth(new DateTime().getMonthOfYear(),new DateTime().getYear());


    }


   /* private void pullSales(final Context context, String lastSyncDate, final SuccessListener successListener) {
        ApiHandler.getInstance().pullFromTable(
                "sales",
                lastSyncDate,
                null,
                -1,
                -1,
                String.valueOf(loggedInId),
                new ApiHandler.SyncSuccessListener() {
                    @Override
                    public void onSuccess(JSONArray responseArray, JSONObject pagination, String dateExecuted) {
                        Gson gson = new Gson();
                        SalesReport[] salesReports = gson.fromJson(responseArray.toString(), SalesReport[].class);

                        for(SalesReport salesReport : salesReports){
                            salesReport.setHasSynced(true);
                        }

                        mSalesReportRepository.insertSalesReport(Arrays.asList(salesReports));
                        SharedPref.putStringAndApply(context, SharedPref.LAST_SYNC_SALES, dateExecuted);
                        successListener.onSuccess(true);
                    }

                    @Override
                    public void onError(String message) {
                        Log.v(TAG, message);
                        successListener.onSuccess(false);
                    }
                });
    }

    public void doSync(final Application application, final SuccessListener successListener) {
        pullSales(application, SharedPref.getValueByKey(application, SharedPref.LAST_SYNC_SALES), new SuccessListener() {
            @Override
            public void onSuccess(Boolean success) {
                setCurrentMonth(new DateTime().getMonthOfYear(),new DateTime().getYear());
                pushSalesReport(application, new SuccessListener() {
                    @Override
                    public void onSuccess(Boolean success) {
                        Log.v(TAG,"finished syncing.");
                        successListener.onSuccess(true);
                    }
                });
            }
        });
    }

    public void pushSalesReport(Application application, final SuccessListener successListener){
        pushSingleSalesReport(application,successListener);
    }

    private void pushSingleSalesReport(final Application application, final SuccessListener successListener) {
        ApiHandler.getInstance().getLastSyncDate("sales",
                SharedPref.getIdLoggedIn(application),
                new ApiHandler.SyncDateListener() {
                    @Override
                    public void onSuccess(String date) {
                        SalesReport salesReport = null;
                        if(!date.equals("0000-00-00 00:00:00")){
                            salesReport = mSalesReportRepository.getByDateSync(loggedInId,date,selectedStoreUuid);
                        }else{
                            salesReport = mSalesReportRepository.getFirstCreated(loggedInId,selectedStoreUuid);
                        }
                        if(salesReport != null){
                            ApiHandler.getInstance().pushSalesReport(salesReport, new ApiHandler.SalesReportListener() {
                                @Override
                                public void onSuccess(SalesReport sr) {
                                    if(sr != null){
                                        pushSingleSalesReport(application,successListener);
                                    }else{
                                        successListener.onSuccess(true);
                                    }
                                }

                                @Override
                                public void onError(String message) {
                                    Log.v(TAG,"pushsalesreport" + message);
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

    public void setSelectedStoreUuid(String selectedStoreUuid) {
        this.selectedStoreUuid = selectedStoreUuid;
    }

    public void setCurrentMonth(int month, int year) {
//        DateTime startDate = new DateTime(year,month,1,0,0);
//        DateTime endDate = startDate.dayOfMonth().withMaximumValue();



        List<SalesDay> salesDays = new ArrayList<>();
        mSaleDays.setValue(new ArrayList<SalesDay>());


        DateTime startDate = new DateTime(year,month,1,0,0);
        DateTime endDate   = startDate.dayOfMonth().withMaximumValue();
        while(startDate.compareTo(endDate) < 1){

            mSalesReportRepository.getByDate(loggedInId, startDate, selectedStoreUuid, new SalesReportRepository.SalesFetchListener() {
                @Override
                public void onFetchSales(SalesReport salesReport,DateTime dt) {
                    SalesDay salesDay = new SalesDay();
                    salesDay.setDateTime(dt);
                    salesDay.setDay(dt.toString("MMMM d"));

                    if(salesReport != null){
                        salesDay.setSalesValue(salesReport.getSales());
                        salesDay.setSalesReportId(String.valueOf(salesReport.getUuid()));
                        salesDay.setDisabled(salesReport.getHasSynced());
                        salesDay.setDayCreated(salesReport.getCreatedAt());
                    }

                    List<SalesDay> salesDaysPending = mSaleDays.getValue();
                    salesDaysPending.add(salesDay);
                    mSaleDays.postValue(salesDaysPending);

                }
            });

            startDate = startDate.plusDays(1);

        }

//        mSaleDays.setValue(salesDays);
    }



    public void incrementMonth(){
        mActiveMonthYear.setValue(mActiveMonthYear.getValue().plusMonths(1));
        setCurrentMonth(
                mActiveMonthYear.getValue().getMonthOfYear(),
                mActiveMonthYear.getValue().getYear()
        );
    }

    public void decrementMonth(){
        mActiveMonthYear.setValue(mActiveMonthYear.getValue().minusMonths(1));
        setCurrentMonth(
                mActiveMonthYear.getValue().getMonthOfYear(),
                mActiveMonthYear.getValue().getYear()
        );
    }

    public void updateSales(){
        Log.v(TAG,"update sales");
        List<SalesReport> salesReports = new ArrayList<>();
        if(mSaleDays.getValue() != null){
            int i = 1;
            for(SalesDay salesDay : mSaleDays.getValue()){
                if(!salesDay.getDisabled()){
                    SalesReport salesReport = new SalesReport();
                    salesReport.setDatetime(Utils.newDateSqlStringOnlyDate(salesDay.getDateTime().plusSeconds(i)));
                    salesReport.setSales(salesDay.getSalesValue());
                    salesReport.setUserId(loggedInId);
                    salesReport.setStoreUuid(selectedStoreUuid);
                    if(!salesDay.getSalesValue().isEmpty()){
                        Log.v(TAG,"TES");
                    }
                    if(salesDay.getSalesReportId() != null){
                        salesReport.setUuid(salesDay.getSalesReportId());
                        salesReport.setUpdatedAt(Utils.formatDateTimeForSql(new DateTime().plusSeconds(i+2)));
                        salesReport.setCreatedAt(salesDay.dayCreated);
                        if(salesDay.getSalesValue().isEmpty()){
                            i++;
                            mSalesReportRepository.removeSalesReport(salesReport)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new CompletableObserver() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            mDisposable.add(d);
                                        }

                                        @Override
                                        public void onComplete() {
                                            Log.d(TAG, "successfully deleted ");
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }
                                    });
                            continue;
                        }
                    }else{
                        salesReport.setCreatedAt(Utils.formatDateTimeForSql(new DateTime().plusSeconds(i+2)));
                    }


                    if(salesDay.getSalesValue().isEmpty() && salesDay.getSalesReportId() == null){
                        i++;
                        continue;
                    }

                    salesReports.add(salesReport);
                }
                i++;
            }
        }
        mSalesReportRepository.insertSalesReport(salesReports)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "updated");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
        setCurrentMonth(new DateTime().getMonthOfYear(),new DateTime().getYear());
//        for(SalesDay salesDay : mSaleDays.getValue())
    }

    public LiveData<List<SalesDay>> getSaleDays() {
        return mSaleDays;
    }

    public LiveData<String> getStringMonthYear() {
        return mStringMonthYear;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}

