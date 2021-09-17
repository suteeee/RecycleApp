package com.kt.recycleapp.kotlin.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel:ViewModel() {
    var zoomCnt = MutableLiveData("1.0x")
}