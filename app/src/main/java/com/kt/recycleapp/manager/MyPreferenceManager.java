package com.kt.recycleapp.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


/**
 * 데이터 저장 및 로드 클래스
 */

public class MyPreferenceManager {
    private Context context;

    public MyPreferenceManager(Context context) {
        this.context = context;
        Log.d(context.toString(),"123456789");
    }

    public static final String PREFERENCES_NAME = "prefs";
    private static final String isNeverShowEdtKey = "neverShow";


    private final SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_NAME,0);


    public Boolean getIsNeverShow() {
        return prefs.getBoolean(isNeverShowEdtKey,false);
    }

    public void setIsNeverShow(Boolean b) {
        prefs.edit().putBoolean(isNeverShowEdtKey,b).apply();
    }

}