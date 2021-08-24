package com.kt.recycleapp.kotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraSettingViewModel:ViewModel() {
    var zoomCnt = MutableLiveData("1.0x")
}