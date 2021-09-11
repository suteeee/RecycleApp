package com.kt.recycleapp.kotlin.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.kt.recycleapp.model.DatabaseReadModel

class ImageUploadViewModel:ViewModel() {
    var photoUri: Uri? = null

    fun uploadImage(name:String,db:DatabaseReadModel) {
        db.imageUpload(name,photoUri!!)
    }
}