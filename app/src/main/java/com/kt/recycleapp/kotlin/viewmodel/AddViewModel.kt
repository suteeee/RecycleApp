package com.kt.recycleapp.kotlin.viewmodel

import android.net.Uri
import android.widget.ProgressBar
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.kt.recycleapp.model.DatabaseReadModel

class AddViewModel:ViewModel() {
   companion object{
       var barcode = ""
       var summit = MutableLiveData<String>()
       var products = ArrayList<String>()
       var addItems = ArrayList<HashMap<String,Any>>()
       var infoText = ArrayList<HashMap<String,Any>>()
   }
    var productList =ArrayList<String>()
    val listLoadFinish = MutableLiveData<String>()
    var photoUri: Uri? = null
    var itemList = ObservableArrayList<Int>()
    val model = DatabaseReadModel.instance

    fun uploadAll() {
        model.uploadAll(photoUri)
    }

    fun loadingList() {
        productList = model.getProductsList(listLoadFinish)
    }

}
