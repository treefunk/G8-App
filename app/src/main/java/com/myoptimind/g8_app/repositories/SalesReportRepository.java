package com.myoptimind.g8_app.repositories;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.myoptimind.g8_app.Utils;
import com.myoptimind.g8_app.db.AppDatabase;
import com.myoptimind.g8_app.db.dao.SalesReportDao;
import com.myoptimind.g8_app.models.SalesReport;

import org.joda.time.DateTime;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class SalesReportRepository {

    SalesReportDao mSalesReportDao;

    public SalesReportRepository(Context application) {
        mSalesReportDao = AppDatabase.getInstance(application).salesReportDao();
    }



    /**
     * Database Requests
     */

    // Fetch

    public void getByDate(final String userId, final DateTime date,final String storeUuid, final SalesFetchListener salesFetchListener){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                SalesReport salesReport = mSalesReportDao.getByDate(userId,"%" + Utils.newDateSqlStringOnlyDate(date) + "%",storeUuid);
                salesFetchListener.onFetchSales(salesReport,date);
            }
        });
    }

    public interface SalesFetchListener{
        void onFetchSales(SalesReport salesReport, DateTime datetime);
    }

    // Insert

    public Completable insertSalesReport(List<SalesReport> salesReports) {
        return mSalesReportDao.insertSalesReportList(salesReports);
    }


    // Delete
    public Completable removeSalesReport(SalesReport salesReport) {
        return mSalesReportDao.removeSalesReport(salesReport);
    }


    /**
     * Network Requests
     */

    public Single<SalesReport> getByDateSync(String userId, String date){
        return mSalesReportDao.getByDateSync(userId,date);
    }

    public Single<SalesReport> getFirstCreated(String userId){
        return mSalesReportDao.getFirstCreated(userId);
    }


}
