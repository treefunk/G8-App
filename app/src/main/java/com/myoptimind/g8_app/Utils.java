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

        return Math.sqrt(
                Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)
        );
    }

    public static double rad(double x){
        return x * Math.PI / 180;
    }



    public static double getDistancev2(
            double aX,
            double aY,
            double bX,
            double bY
    ){
        int meanRadius = 6378137; // Earth’s mean radius in meter
        double dLat = rad(bX - aX);
        double dLong = rad(bY - aY);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(rad(aX)) * Math.cos(rad(bX)) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = meanRadius * c;
        d = d / 1000; // in km
        return d;
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
            if(!enable){
                view.setOnClickListener(null);
            }
        }
    }




}

