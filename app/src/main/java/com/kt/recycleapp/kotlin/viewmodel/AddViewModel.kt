package com.kt.recycleapp.kotlin.viewmodel

import android.net.Uri
import android.widget.ProgressBar
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kt.recycleapp.model.DatabaseReadModel

class AddViewModel:ViewModel() {
   companion object{
       var productList =ArrayList<String>()
       var barcode = ""
       var summit = MutableLiveData<String>()
       var products = ArrayList<String>()
       var addItems = ArrayList<HashMap<String,Any>>()
   }
    var photoUri: Uri? = null
    var itemList = ObservableArrayList<Int>()
    val model = DatabaseReadModel()

    fun uploadAll(multyPb: ProgressBar) {
        model.uploadAll(multyPb,photoUri)
    }

}
