package com.myoptimind.g8_app;

import android.view.View;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

public class Utils {


    /**
     * Distance between two points
     */

    public static double getDistance(double x1,
                                     double y1,
                                     double x2,
                                     double y2){

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }


    public static DateTime newDate(){
        return new DateTime(DateTimeZone.forID("Asia/Hong_Kong"));
    }

    public static String formatDateTimeForSql(DateTime dt){
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(dt);
    }


    public static String newDateSqlString(){
        return Utils.formatDateTimeForSql(Utils.newDate());
    }

    public static String newDateSqlStringOnlyDate(DateTime dt){
        return DateTimeFormat.forPattern("yyyy-MM-dd").print(dt);
    }

    public static void setEnableViews(View[] views, Boolean enable){
        for(View view : views) {
            view.setEnabled(enable);
        }
    }




}

