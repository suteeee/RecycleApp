package com.kt.recycleapp.kotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlertViewModel:ViewModel() {
    companion object {
        var imageLoadFinish = MutableLiveData(false)
        var imageUploadResult = MutableLiveData(false)
    }
    val str = MutableLiveData<String>()

}