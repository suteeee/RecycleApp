package com.kt.recycleapp.kotlin.upload

import android.net.Uri
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kt.recycleapp.model.DatabaseReadModel

class DataUploadViewModel : ViewModel() {
    val db = DatabaseReadModel.instance

    var productList = ArrayList<String>()
    val listLoadFinish = MutableLiveData<String>()
    val uploadFinish = MutableLiveData<String>()

    var itemList = ObservableArrayList<Int>()

    var barcodes = ArrayList<String>()
    var names = ArrayList<String>()
    var kinds = ArrayList<String>()
    var subNames = ArrayList<String>()
    var infoText = ArrayList<String>()

    var photoUri: Uri? = null

    fun loadingList() {
        productList = db.getProductsList(listLoadFinish)
    }

    fun upload() {
        db.uploadData(barcodes[0], names, kinds, subNames, uploadFinish,photoUri, infoText)
    }
}