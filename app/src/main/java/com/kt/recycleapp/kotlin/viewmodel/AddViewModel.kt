package com.kt.recycleapp.kotlin.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddViewModel:ViewModel() {
   companion object{
       var productList =ArrayList<String>()
       var barcode = ""
       var summit = MutableLiveData<String>()
       var products = ArrayList<String>()
       var addItems = ArrayList<HashMap<String,Any>>()
   }

    var itemList = ObservableArrayList<Int>()
}
