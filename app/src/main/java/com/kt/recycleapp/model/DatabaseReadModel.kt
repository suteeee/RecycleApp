package com.kt.recycleapp.model

import android.content.Context
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
import com.kt.recycleapp.kotlin.upload.AddViewModel
import com.kt.recycleapp.kotlin.alert.AlertViewModel
import com.kt.recycleapp.kotlin.find.FindViewModel
import kotlinx.coroutines.*
import java.recycleapp.R

class DatabaseReadModel {
    private val STORAGE_URL = "gs://recycleapp-e6ed9.appspot.com"
    private val MIXED_PRODUCT = "복합물품"
    private val SUBLIST_PRODUCT = "sublist"
    private val SUBLIST_INFO = "subList"

    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance(STORAGE_URL).reference
    var products = db.collection("products")
    var detailInfo = db.collection("detailInfo")
    var mixedProducts = products.document(MIXED_PRODUCT).collection(SUBLIST_PRODUCT).get()
    var mixedInfo =  detailInfo.document(MIXED_PRODUCT).collection(SUBLIST_INFO)
    var kind :String = ""


    companion object {
        var name = HashMap<String, String>()
        var nameForSetImage = ""
        val instance:DatabaseReadModel = DatabaseReadModel()
    }

    fun findBig(findBigProgress: MutableLiveData<String>):ArrayList<String>{
        findBigProgress.value = "start"
        val collection = products
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
        val collection = products
        val multi = products.document(MIXED_PRODUCT).collection(SUBLIST_PRODUCT)
        val arr = ArrayList<HashMap<String,String>>()
        //컬렉션 Arr 안에 문서 arr 안에 값 hashmap 구조

        val c =
            if(selected == MIXED_PRODUCT) multi.get()
            else collection.get()

            c.addOnCompleteListener {
                val variable = it.result.documents
                variable.forEach { doc->
                    if(selected != MIXED_PRODUCT){
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

        var collection = products
        collection.get().addOnCompleteListener {
            for(doc in it.result.documents) {
                if(doc.id != MIXED_PRODUCT){
                    doc.data?.forEach { res-> name.put(res.key,res.value.toString()) }
                }
            }
            getProductName.value="finish"
        }
    }

    fun getProductsList(arr:MutableLiveData<String>) : ArrayList<String>{
        arr.value = "start"
        var collection = products
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
        products.get().addOnCompleteListener {
            (it.result.documents).forEach { doc ->
                // 물품 명으로 탐색
                if (doc.data?.values?.contains(product) == true) {
                    Log.d(doc.data?.toString(),"test")
                    res = doc.id
                    return@forEach
                }
                kind.value = res
                this.kind = res
            }
            finding.value = "finish"
        }
    }

    fun settingResult(setting:MutableLiveData<String>,kind: String,product: ObservableArrayList<AnnounceData>,barcode:String){
        setting.value = "start"
        var name = barcode
        var info = ""
        var pKind = kind
        var document: Map<String, Any>? =  null
        val resultInfo = db.collection("resultInfo").get()


        resultInfo.addOnCompleteListener {
            (it.result.documents).forEach { doc ->
                document = doc.data
            }
            Log.d("doc", info)
        }


        products.get().addOnCompleteListener {
            (it.result.documents).forEach{doc ->
                if(doc.data?.get(barcode) != null){
                    name = doc.data?.get(barcode).toString()
                    pKind = doc.id
                }
            }

            if(pKind.isEmpty()) {
                info = "데이터를 등록해주세요!"
                pKind = "등록되지 않은 물품입니다."
                product.add(AnnounceData(name, info ,pKind))
            }
            else {
                mixedInfo.document(pKind).get().addOnCompleteListener {
                    val res = (it.result.data)?.get(barcode)
                    if(res != null) {
                        //세부 설명이 있으면 info를 세부 설명으로
                        info = res.toString()
                    }else {
                        //없으면 기본 설명 탐색
                        info = document?.get(pKind).toString()

                    }
                    product.add(AnnounceData(name, info ,pKind))//첫번째 페이지(주 물품)

                    //두번째 물품부터
                   mixedProducts.addOnCompleteListener {
                        var cnt = 1
                        (it.result.documents).forEach { doc->
                            val d = doc.data
                            d?.forEach { map->
                                //물품 이름이 복합물품 문서 안에 있으면
                                if(map.key.contains(name)){
                                    var str = ""
                                    //상세설명 찾아보기
                                    mixedInfo.document(doc.id).get().addOnCompleteListener {
                                        str = it.result.data?.get("${name}_${cnt++}").toString()
                                        if(str.isEmpty() || str.isBlank() || str == "null") {
                                            str = document?.get(doc.id).toString()
                                        }
                                        product.add(AnnounceData(map.value.toString(),str, doc.id))
                                    }
                                }
                            }
                        }
                        setting.value = "finish"
                    }
                }
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
                    products.get().addOnCompleteListener {
                        (it.result.documents).forEach { doc ->
                            // 물품 명으로 탐색
                            if (doc.data?.values?.contains(itemName) == true) {
                                k = doc.id
                                return@forEach
                            }
                        }
                        mixedProducts.addOnCompleteListener {
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
            val exList = AddViewModel.infoText
            try {
                for (i in 0 until list.size) {
                    if (i == 0) {
                        products.document(AddViewModel.products[i]).update(list[i])
                        detailInfo.document(AddViewModel.products[i]).update(exList[i])
                    } else {
                        products.document(MIXED_PRODUCT).collection(SUBLIST_PRODUCT).document(AddViewModel.products[i]).update(list[i])
                        mixedInfo.document(AddViewModel.products[i]).update(exList[i])
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

    fun uploadData
                (barcode: String, names: ArrayList<String>, kinds: ArrayList<String>, subnames: ArrayList<String>, uploadFinish: MutableLiveData<String>, photoUri: Uri?, infoText: ArrayList<String>) {
        CoroutineScope(Dispatchers.IO).launch{
            uploadFinish.postValue("start")
            val col = products
            val sub = col.document(MIXED_PRODUCT).collection(SUBLIST_PRODUCT)

            col.document(kinds[0]).update(barcode, names[0])

            if(infoText[0].isNotBlank() && infoText[0].isNotEmpty()) {
                detailInfo.document(kinds[0]).update(names[0],infoText[0])
            }

            for(i in 1 until names.size){
                sub.document(kinds[i]).update(names[i], subnames[i])
                Log.d("Load11",infoText[i])
                if(infoText[i].isNotBlank() && infoText[i].isNotEmpty()) {
                    mixedInfo.document(kinds[i]).update(names[i],infoText[i])
                }
            }

            if(photoUri != null) {
                imageUpload(names[0],photoUri)
            }

            uploadFinish.postValue("finish")
        }
   }

    fun imageUpload(name:String, photoUri:Uri) {
        val fileName = "IMAGE_${name.replace(" ","")}"
        val imgRef = storage.child("products_image/$fileName.png")
        imgRef.putFile(photoUri).addOnCompleteListener {
            AlertViewModel.imageUploadResult.value = it.isSuccessful
            AlertViewModel.imageLoadFinish.value = true
        }
    }

    fun checkBarcode(barcode: String, isHaveBarcode: MutableLiveData<Boolean>, checkBarcodeFinish: MutableLiveData<Boolean>){
        checkBarcodeFinish.value = false
        isHaveBarcode.value = false
        products.get().addOnCompleteListener {
            var check = false
            (it.result.documents).forEach { doc->
                Log.d("Main1",doc.data?.get(barcode).toString())
                if(doc.data?.get(barcode) != null) {
                    check = true
                    return@forEach
                }
            }
            isHaveBarcode.value = check
            checkBarcodeFinish.value = true
        }
    }

}