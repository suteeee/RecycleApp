package com.kt.recycleapp.kt.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraSettingFragmenViewModel:ViewModel() {
    var zoomCnt = MutableLiveData<Float>(1F)
    var zoomState = MutableLiveData<Float>()

}