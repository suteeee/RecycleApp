package com.kt.recycleapp.kotlin

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object Internet {
    val WIFI = 0
    val MOBILE_DATA = 1
    val NOT_CONNECT = 2
    val CONNECTED = 3

    fun getStatus(context: Context):Int {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = manager.activeNetwork ?: return NOT_CONNECT
            val actNetwork = manager.getNetworkCapabilities(network) ?: return NOT_CONNECT

            return when {
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> WIFI
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> MOBILE_DATA
                else -> NOT_CONNECT
            }
        }
        else {
            val info = manager.activeNetworkInfo ?: return NOT_CONNECT
            return if(info.isConnected) {
                CONNECTED
            }else {
                NOT_CONNECT
            }
        }
    }
}