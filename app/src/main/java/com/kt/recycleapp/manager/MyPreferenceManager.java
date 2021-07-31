package com.kt.recycleapp.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


/**
 * 데이터 저장 및 로드 클래스
 */

public class MyPreferenceManager {
    private final SharedPreferences prefs;

    public MyPreferenceManager(Context context) {
        prefs = context.getSharedPreferences(PREFERENCES_NAME,0);
        Log.d(context.toString(),"123456789");
    }

    public static final String PREFERENCES_NAME = "prefs";
    private static final String isNeverShowEdtKey = "neverShow";


    public Boolean getIsNeverShow() {
        return prefs.getBoolean(isNeverShowEdtKey,false);
    }

    public void setIsNeverShow(Boolean b) {
        prefs.edit().putBoolean(isNeverShowEdtKey,b).apply();
    }

}