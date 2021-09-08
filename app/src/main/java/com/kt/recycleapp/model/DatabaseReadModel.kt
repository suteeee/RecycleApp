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
import java.recycleapp.R

class DatabaseReadModel {
    private val STORAGE_URL = "gs://recycleapp-e6ed9.appspot.com"

    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance(STORAGE_URL).reference
    var kind :String = ""

    companion object {
        var name = HashMap<String, String>()
        var decodeImageList = ArrayList<Bitmap>()
        var nameForSetImage = ""
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

    fun findProductKind(finding: MutableLiveData<String>, product: String, kind: MutableLiveData<String>, imgCode: MutableLiveData<Int>) {
        finding.value = "start"
        var res = ""
        db.collection("products").get().addOnCompleteListener {
            (it.result.documents).forEach { doc ->
                // 물품 명으로 탐색
                if (doc.data?.values?.contains(product) == true) {
                    res = doc.id
                    return@forEach
                }
                kind.value = res
                this.kind = res
            }
            when(res) {
                "건전지" -> R.drawable.ic_baterry_default
                "고철" -> R.drawable.ic_iron_default
                "비닐" -> R.drawable.ic_vinyl_default
                "유리" -> R.drawable.ic_glass_default
                "일반쓰레기" -> R.drawable.ic_trash_default
                "종이" -> R.drawable.ic_paper_default
                "캔" -> R.drawable.ic_can_default
                "페트병" -> R.drawable.ic_paper_default
                "플라스틱" -> R.drawable.ic_plastic_default
            }
            finding.value = "finish"
        }
    }

    fun settingResult(setting:MutableLiveData<String>,kind: String,product: ObservableArrayList<AnnounceData>,barcode:String){
        setting.value = "start"
        val infoMap = HashMap<String,String>()
        var name = barcode
        var info = ""
        var pKind = kind
        db.collection("products").get().addOnCompleteListener {
            (it.result.documents).forEach{doc ->
                if (doc.data?.keys?.contains(barcode) == true) { //db에 존재할때
                    name = doc.data?.get(barcode).toString()
                    pKind = doc.id
                    return@forEach
                }
            }
        }
        db.collection("resultInfo").get().addOnCompleteListener {
            (it.result.documents).forEach { doc ->
                doc.data?.forEach {
                    infoMap[it.key] = it.value.toString()
                }
            }
            info = infoMap.get(pKind).toString()
            product.add(AnnounceData(name, info ,pKind))//첫번째 페이지(주 물품)

            db.collection("products").document("복합물품").collection("sublist").get().addOnCompleteListener {
                (it.result.documents).forEach { doc->
                    val d = doc.data
                    d?.forEach { map->
                        if(map.key.contains(name)){
                            product.add(AnnounceData(map.value.toString(),infoMap[doc.id], doc.id))
                        }
                    }

                }
                setting.value = "finish"
            }

        }
    }

    fun setImage(context: Context, imageView: ImageView, progressBar: ProgressBar, itemName: String) {
       // CoroutineScope(Dispatchers.IO).launch {
            var k = ""
            storage.child("products_image/IMAGE_${itemName.replace(" ", "")}.png")
                .downloadUrl.addOnSuccessListener {
                    Glide.with(context).load(it).override(500).into(imageView)
                    progressBar.visibility = View.INVISIBLE
                }
                .addOnFailureListener {
                    var id = 0
                    Log.d("kind", itemName)
                    db.collection("products").get().addOnCompleteListener {
                        (it.result.documents).forEach { doc ->
                            // 물품 명으로 탐색
                            if (doc.data?.values?.contains(itemName) == true) {
                                k = doc.id
                                return@forEach
                            }
                        }
                        db.collection("products").document("복합물품").collection("sublist").get()
                            .addOnCompleteListener {
                                (it.result.documents).forEach { doc ->
                                    // 물품 명으로 탐색
                                    if (doc.data?.values?.contains(itemName) == true) {
                                        Log.d("kind", "find")
                                        k = doc.id
                                        return@forEach
                                    }
                                }

                                when (k) {
                                    "건전지" -> id = R.drawable.ic_baterry_default
                                    "고철" -> id = R.drawable.ic_iron_default
                                    "비닐" -> id = R.drawable.ic_vinyl_default
                                    "유리" -> id = R.drawable.ic_glass_default
                                    "일반쓰레기" -> id = R.drawable.ic_trash_default
                                    "종이" -> id = R.drawable.ic_paper_default
                                    "캔" -> id = R.drawable.ic_can_default
                                    "페트병" -> id = R.drawable.ic_pet_default
                                    "플라스틱" -> id = R.drawable.ic_plastic_default
                                }
                                Log.d("kind", "$id $k")

                                Glide.with(context).load(id).override(500).into(imageView)
                                progressBar.visibility = View.INVISIBLE
                            }
                    }
                }
       // }
    }

    fun uploadAll(photoUri: Uri?) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = AddViewModel.addItems
            try {
                for (i in 0 until list.size) {
                    if (i == 0) {
                        db.collection("products").document(AddViewModel.products[i])
                            .update(list[i])
                    } else {
                        db.collection("products").document("복합물품").collection("sublist").document(
                            AddViewModel.products[i]
                        )
                            .update(list[i])
                    }
                }
            }catch (e:Exception){

            }

            if(photoUri != null) {
                val fileName = "IMAGE_${list[0][AddViewModel.barcode]}"
                val imgRef = storage.child("products_image/$fileName.png")
                imgRef.putFile(photoUri)
            }

            AddViewModel.addItems.clear()
        }
    }

    fun uploadData(barcode: String, names: ArrayList<String>, kinds: ArrayList<String>, subnames: ArrayList<String>, uploadFinish: MutableLiveData<String>,photoUri: Uri?) {

        CoroutineScope(Dispatchers.IO).launch{
            uploadFinish.postValue("start")
            val col = db.collection("products")
            val sub = col.document("복합물품").collection("sublist")

            col.document(kinds[0]).update(barcode, names[0])


            for(i in 1 until names.size){
                sub.document(kinds[i]).update(names[i], subnames[i])
            }

            if(photoUri != null) {
                val fileName = "IMAGE_${names[0]}"
                val imgRef = storage.child("products_image/$fileName.png")
                imgRef.putFile(photoUri)
            }

            uploadFinish.postValue("finish")
        }
   }

}