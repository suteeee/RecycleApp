package com.kt.recycleapp.kotlin.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class MainViewModel:ViewModel() {

        companion object {
                var isPopupClose = MutableLiveData<String>()
        }
        var toolbarText = MutableLiveData<String>()
        var selectedFragment = MutableLiveData<String>()
        var searchFlag = MutableLiveData<String>()
        var isPopup = MutableLiveData(true)
        var isDrawerOpen = MutableLiveData(false)
        var fragmentStack = Stack<String>()

}