package com.kt.recycleapp.model

import android.content.Context
import android.content.SharedPreferences

class MySharedPrefs(context: Context) {
    private val prefsFileName = "prefs"


    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFileName, 0)
}