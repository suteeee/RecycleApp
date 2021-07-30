package com.kt.recycleapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class DatabaseReadModel {
    val db = FirebaseFirestore.getInstance()


    fun findBig(findBigProgress: MutableLiveData<String>):ArrayList<HashMap<String,String>>{
        findBigProgress.value = "start"
        val collection = db.collection("find-big")
        val arr = ArrayList<HashMap<String,String>>()
        collection.get().addOnCompleteListener {
            for(document in it.result){
                for(i in 0 until document.data.keys.size){
                    val temp = HashMap<String,String>()
                    temp[document.data.keys.elementAt(i).toString()] =
                        document.data.values.elementAt(i).toString()
                    arr.add(temp)
                }
            }
            findBigProgress.value = "finish"
        }
        return arr
    }

    fun findSmall(findSmallProgress: MutableLiveData<String>): ArrayList<HashMap<String, String>> {
        findSmallProgress.value = "start"
        val collection = db.collection("find-small").document("종이")
        val arr = ArrayList<HashMap<String,String>>()
        collection.get().addOnCompleteListener {
            val variable = it.result.data
            for(i in 0 until variable!!.size){
                val temp = HashMap<String,String>()
                temp.put(i.toString(),variable[i.toString()].toString())
                arr.add(temp)
            }
            findSmallProgress.value = "finish"
        }
        return arr
    }
}