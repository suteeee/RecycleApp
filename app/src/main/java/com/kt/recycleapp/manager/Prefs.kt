package com.kt.recycleapp.manager

import android.content.Context

class Prefs(context: Context) {

    val PREFERENCES_NAME = "prefs"
    private val isNeverShowEdtKey = "neverShow"

    private val prefs = context.getSharedPreferences(MyPreferenceManager.PREFERENCES_NAME, 0)

    fun getIsNeverShow(): Boolean? {
        return prefs.getBoolean(isNeverShowEdtKey, false)
    }

    fun setIsNeverShow(b: Boolean?) {
        prefs.edit().putBoolean(isNeverShowEdtKey, b!!).apply()
    }
}