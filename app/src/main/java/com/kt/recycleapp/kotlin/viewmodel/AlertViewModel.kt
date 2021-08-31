package com.kt.recycleapp.kotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlertViewModel:ViewModel() {
    val str = MutableLiveData<String>()

}