package com.kt.recycleapp.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;


/**
 * 데이터 저장 및 로드 클래스
 */

public class MyPreferenceManager {
    private final SharedPreferences prefs;

    public MyPreferenceManager(Context context) {
        prefs = context.getSharedPreferences(PREFERENCES_NAME,0);
    }

    public static final String PREFERENCES_NAME = "prefs";
    private static final String isNeverShowEdtKey = "neverShow";
    private static final String STORED_TIME = "123";

    public void setStoredTime(String date){
        prefs.edit().putString(STORED_TIME,date).apply();
    }

    public String getStoredTime(){
        return prefs.getString(STORED_TIME, "20000101");
    }



    public Boolean getIsNeverShow() {
        return prefs.getBoolean(isNeverShowEdtKey,false);
    }

    public void setIsNeverShow(Boolean b) {
        prefs.edit().putBoolean(isNeverShowEdtKey,b).apply();
    }

}