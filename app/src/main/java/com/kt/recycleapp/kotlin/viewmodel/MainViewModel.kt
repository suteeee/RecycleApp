package com.kt.recycleapp.kotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class MainViewModel:ViewModel() {
        var toolbarText = MutableLiveData<String>()
        var selectedFragment = MutableLiveData<String>()
        var searchFlag = MutableLiveData<String>()
        var isPopup = MutableLiveData(true)
        var isDrawerOpen = MutableLiveData(false)
        var fragmentStack = Stack<String>()
}