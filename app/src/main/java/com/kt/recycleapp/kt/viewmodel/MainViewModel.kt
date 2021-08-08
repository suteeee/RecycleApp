package com.kt.recycleapp.kt.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {
        var toolbarText = MutableLiveData<String>()
        var selectedFragment = MutableLiveData<String>()
        var searchFlag = MutableLiveData<String>()
        var isPopup = MutableLiveData<String>("false")
        var isDrawerOpen = MutableLiveData(false)
}