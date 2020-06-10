package com.myoptimind.g8_app.features.shared;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private static final String SHARED_PREF_NAME       = "tito_shared_pref";
    private static final String LOGGED_IN              = "current_logged_in";
    public static final String LAST_SYNC_STORE         = "last_sync_store";
    public static final String LAST_SYNC_SALES         = "last_sync_sales";
    public static final String LAST_SYNC_TIMESLIP      = "last_sync_timeslip";
    public static final String LAST_SYNC_ANNOUNCEMENTS = "last_sync_announcements";
    public static final String LAST_SYNC_TIMEINOUT     = "last_sync_timeinout";

    public static SharedPreferences mSharedPreferences;
    public static SharedPref INSTANCE;

    public SharedPref(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
    }

    public static SharedPref getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = new SharedPref(context);
        }
        return INSTANCE;
    }

    public void putStringAndApply(String key, String val){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key,val);
        editor.apply();
    }

    public void clearLastSyncs(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(LAST_SYNC_STORE);
        editor.remove(LAST_SYNC_SALES);
        editor.remove(LAST_SYNC_TIMESLIP);
        editor.remove(LAST_SYNC_ANNOUNCEMENTS);
        editor.remove(LAST_SYNC_TIMEINOUT);
        editor.apply();
    }

    public void deleteKey(String key){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void putStringIfEmpty(String key, String val){
        if(!mSharedPreferences.contains(key)){
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(key,val);
            editor.apply();
        }
    }

    public Boolean loggedInExists(){
        return mSharedPreferences.contains(LOGGED_IN);
    }

    public String getValueByKey(String key){
        return mSharedPreferences.getString(key,"");
    }

    public String getIdLoggedIn(){
        return getValueByKey(LOGGED_IN);
    }

    public void setIdLoggedIn(String loggedInId){
        putStringAndApply(LOGGED_IN,loggedInId);
    }
}