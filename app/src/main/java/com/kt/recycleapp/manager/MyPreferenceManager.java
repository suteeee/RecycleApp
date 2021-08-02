package com.kt.recycleapp.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.kt.recycleapp.kt.etc.FavoriteData;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;


/*
 * 데이터 저장 및 로드 클래스
 */

public class MyPreferenceManager {
    private final SharedPreferences prefs;


    //context (앱의 정보 갖고있는 애)에서 prefs를 갖고옴
    public MyPreferenceManager(Context context) {
        prefs = context.getSharedPreferences(PREFERENCES_NAME,0);
    }

    public static final String PREFERENCES_NAME = "prefs";
    private static final String isNeverShowEdtKey = "neverShow";
    private static final String STORED_TIME = "123";
    private static final String FAVORITE_KEY = "F";

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


    public ArrayList<String> getFavoriteList() throws JSONException {
        String json = prefs.getString(FAVORITE_KEY,null);
        ArrayList<String> urls = new ArrayList<String>();

        if(json != null) {
            JSONArray a = new JSONArray(json);
            for (int i = 0; i < a.length(); i++) {
                String url = a.optString(i);
                urls.add(url);
            }
        }

        return urls;
    }

    public void setFavoriteList(ArrayList<String> values) {
        JSONArray a = new JSONArray();
        for(int i = 0; i< values.size(); i++){
            a.put(values.get(i));
        }
        prefs.edit().putString(FAVORITE_KEY,a.toString()).apply();
    }


}