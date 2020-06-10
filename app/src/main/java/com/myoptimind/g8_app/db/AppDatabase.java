package com.myoptimind.g8_app.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.myoptimind.g8_app.db.dao.AnnouncementDao;
import com.myoptimind.g8_app.db.dao.SalesReportDao;
import com.myoptimind.g8_app.db.dao.StoreDao;
import com.myoptimind.g8_app.db.dao.TimeInOutDao;
import com.myoptimind.g8_app.db.dao.TimeSlipDao;
import com.myoptimind.g8_app.db.dao.UserDao;
import com.myoptimind.g8_app.models.Announcement;
import com.myoptimind.g8_app.models.SalesReport;
import com.myoptimind.g8_app.models.Store;
import com.myoptimind.g8_app.models.TimeInOut;
import com.myoptimind.g8_app.models.TimeSlip;
import com.myoptimind.g8_app.models.User;
import com.myoptimind.g8_app.models.UserAnnouncement;
import com.myoptimind.g8_app.models.UserStore;


@Database(entities = {
        Store.class,
        TimeInOut.class,
        User.class,
        Announcement.class,
        UserAnnouncement.class,
        SalesReport.class,
        TimeSlip.class,
        UserStore.class
},version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "g8-db";

    private static AppDatabase sMainDatabase;

    public abstract StoreDao storeDao();

    public abstract UserDao userDao();

    public abstract TimeInOutDao timeInOutDao();

    public abstract AnnouncementDao announcementDao();

    public abstract SalesReportDao salesReportDao();

    public abstract TimeSlipDao timeSlipDao();

    public static AppDatabase getInstance(final Context context){

        synchronized (AppDatabase.class){

            if(sMainDatabase == null){
                sMainDatabase = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME
                ).build();
            }
        }

        return sMainDatabase;
    }



}

