package com.kt.recycleapp.model

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kt.recycleapp.java.announce.AnnounceData
import com.kt.recycleapp.kotlin.viewmodel.AddViewModel
import com.kt.recycleapp.kotlin.viewmodel.FindViewModel
import kotlinx.coroutines.*
import java.io.File

class DatabaseReadModel {
    val STORAGE_URL = "gs://recycleapp-e6ed9.appspot.com"

    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance(STORAGE_URL).reference
    companion object {
        var name = HashMap<String, String>()
        var decodeImageList = ArrayList<Bitmap>()
    }

    fun findBig(findBigProgress: MutableLiveData<String>):ArrayList<String>{
        findBigProgress.value = "start"
        val collection = db.collection("products")
        val arr = ArrayList<String>()
        collection.get().addOnCompleteListener {
            for(document in it.result){
                arr.add(document.id)
            }
            findBigProgress.value = "finish"
        }
        return arr
    }

    fun findSmall(findSmallProgress: MutableLiveData<String>):  ArrayList<HashMap<String,String>>{
        findSmallProgress.value = "start"
        var selected = FindViewModel.selectDoc
        val collection = db.collection("products")
        val multi = db.collection("products").document("복합물품").collection("sublist")
        val arr = ArrayList<HashMap<String,String>>()
        //컬렉션 Arr 안에 문서 arr 안에 값 hashmap 구조

        val c =
            if(selected == "복합물품") multi.get()
            else collection.get()

            c.addOnCompleteListener {
                val variable = it.result.documents
                variable.forEach { doc->
                    if(selected != "복합물품"){
                        if(doc.id == selected){
                            for(i in 0 until doc.data?.keys?.size!!){
                                val temp = HashMap<String,String>()
                                temp[doc.data!!.keys.elementAt(i)] = doc.data!!.values.elementAt(i).toString()
                                arr.add(temp)
                            }
                        }
                    }else{
                        for(i in 0 until doc.data?.keys?.size!!){
                            val temp = HashMap<String,String>()
                            temp[doc.data!!.keys.elementAt(i)] = doc.data!!.values.elementAt(i).toString()
                            arr.add(temp)
                        }
                    }
                }
                findSmallProgress.value = "finish"
            }
        return arr
    }

    fun getProduct(getProductName: MutableLiveData<String>){
        getProductName.value = "start"

        var collection = db.collection("products")
        collection.get().addOnCompleteListener {
            for(doc in it.result.documents) {
                if(doc.id != "복합물품"){
                    doc.data?.forEach { res-> name.put(res.key,res.value.toString()) }
                }
            }
            getProductName.value="finish"
        }
    }

    fun getProductsList(arr:MutableLiveData<String>) : ArrayList<String>{
        arr.value = "start"
        var collection = db.collection("products")
        var list = ArrayList<String>()
        collection.get().addOnCompleteListener {
            (it.result.documents).forEach {
                list.add(it.id)
            }
            arr.value = "finish"
        }
        return list
    }

    fun findProductKind(finding:MutableLiveData<String>, product : String, kind: MutableLiveData<String>) {
        finding.value = "start"
        var res = ""
        db.collection("products").get().addOnCompleteListener {
                (it.result.documents).forEach { doc->
                    if(doc.data?.values?.contains(product) == true){
                        res = doc.id
                        return@forEach
                    }
                }
            Log.d("fff",res.toString())
            kind.value = res
            finding.value = "finish"
            }
    }

    fun settingResult(setting:MutableLiveData<String>,kind: String,product: ObservableArrayList<AnnounceData>,name:String){
        setting.value = "start"
        val infoMap = HashMap<String,String>()
        var info = ""
        db.collection("resultInfo").get().addOnCompleteListener {
            (it.result.documents).forEach { doc ->
                doc.data?.forEach {
                    infoMap[it.key] = it.value.toString()
                }
            }
            info = infoMap.get(kind).toString()
            product.add(AnnounceData(name, info))//첫번째 페이지(주 물품)

            db.collection("products").document("복합물품").collection("sublist").get().addOnCompleteListener {
                (it.result.documents).forEach { doc->
                    val d = doc.data
                    d?.forEach { map->
                        if(map.key.contains(name)){
                            product.add(AnnounceData(map.value.toString(),infoMap[doc.id]))
                        }
                    }

                }
                setting.value = "finish"
            }

        }
    }

    fun setImage(
        context: Context,
        imageView: ImageView,
        progressBar: ProgressBar,
        itemName: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(itemName,"sub")
            storage.child("products_image/IMAGE_${itemName.replace(" ", "")}.png")
                .downloadUrl.addOnSuccessListener {
                    Glide.with(context).load(it).override(500).into(imageView)
                    progressBar.visibility = View.INVISIBLE
                }
                .addOnFailureListener {
                    storage.child("default_images/default_nothing.png")
                        .downloadUrl.addOnSuccessListener {uri->
                            Glide.with(context).load(uri).override(500).into(imageView)
                            progressBar.visibility = View.INVISIBLE
                    }
                }

        }
    }

    fun uploadAll(photoUri: Uri?) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = AddViewModel.addItems
            for(i in 0 until list.size){
                if(i == 0){
                    db.collection("products").document(AddViewModel.products[i])
                        .update(list[i])
                }
                else{
                    db.collection("products").document("복합물품").collection("sublist").document(
                        AddViewModel.products[i])
                        .update(list[i])
                }
            }

            if(photoUri != null) {
                val fileName = "IMAGE_${list[0][AddViewModel.barcode]}"
                val imgRef = storage.child("products_image/$fileName.png")
                imgRef.putFile(photoUri)
            }

            AddViewModel.addItems.clear()
        }
    }

}