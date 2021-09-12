package com.kt.recycleapp.java.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LodingViewModel :ViewModel() {
    var permission = MutableLiveData<String>()
}