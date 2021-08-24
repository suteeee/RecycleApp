package com.kt.recycleapp.model

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.kt.recycleapp.java.announce.AnnounceData
import com.kt.recycleapp.kotlin.viewmodel.FindViewModel
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
}