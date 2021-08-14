package com.kt.recycleapp.model

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kt.recycleapp.kt.viewmodel.FindViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DatabaseReadModel {
    val db = FirebaseFirestore.getInstance()
    companion object {
        var name = HashMap<String, String>()
        var decodeImageList = ArrayList<Bitmap>()
    }

    fun decode(activity: Activity?,image : String) {
        GlobalScope.launch {
            val path ="${activity?.externalMediaDirs?.get(0)}/수거했어 오늘도!/${image}"
            val bm = BitmapFactory.decodeFile(path)
            decodeImageList.add(bm)
         }
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
            Log.d("start","junnn")
            for(doc in it.result.documents) {
               doc.data?.forEach { res->
                   name.put(res.key,res.value.toString())
               }
            }
            Log.d("junnnn1",name.toString())
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
            Log.d("것것1",list.toString())
            arr.value = "finish"
        }
        return list
    }
}