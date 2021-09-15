package com.kt.recycleapp.model

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kt.recycleapp.java.announce.AnnounceData
import com.kt.recycleapp.kotlin.activity.MainActivity
import com.kt.recycleapp.kotlin.viewmodel.AddViewModel
import com.kt.recycleapp.kotlin.viewmodel.FindViewModel
import kotlinx.coroutines.*
import java.recycleapp.R

class DatabaseReadModel() {
    var context:Context? = null
    constructor(context: Context) : this() {
        this.context = context
    }

    private val STORAGE_URL = "gs://recycleapp-e6ed9.appspot.com"

    val db = FirebaseFirestore.getInstance()
    val storage2 = FirebaseStorage.getInstance(STORAGE_URL).reference
    val storage = FirebaseStorage.getInstance(STORAGE_URL).reference
    var products = db.collection("products")
    var detailInfo = db.collection("detailInfo")
    var kind :String = ""

    companion object {
        var name = HashMap<String, String>()
        var decodeImageList = ArrayList<Bitmap>()
        var nameForSetImage = ""
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
        val multi = products.document("복합물품").collection("sublist")
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

        var collection = products
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
        var document: Map<String, Any>? = null
        products.get().addOnCompleteListener {
            (it.result.documents).forEach{doc ->
                if(doc.data?.get(barcode) != null){
                    name = doc.data?.get(barcode).toString()
                    pKind = doc.id
                }
            }
            if(pKind == null) {
                detailInfo.document(pKind).get().addOnCompleteListener {
                    val res = (it.result.data)?.get(barcode)
                    if(res != null) {
                        info = res.toString()
                    }else {
                        db.collection("resultInfo").get().addOnCompleteListener {
                            (it.result.documents).forEach { doc ->
                                document = doc.data
                                info = document?.get(pKind).toString()
                            }
                            Log.d("doc", info)
                        }
                    }
                    product.add(AnnounceData(name, info ,pKind))//첫번째 페이지(주 물품)

                    products.document("복합물품").collection("sublist").get().addOnCompleteListener {
                        (it.result.documents).forEach { doc->
                            val d = doc.data
                            d?.forEach { map->
                                if(map.key.contains(name)){
                                    /*detailInfo.document("복합물품").collection("subList").get().addOnCompleteListener {
                                        (it.result.documents).forEach{ doc2->
                                            //doc2.data?.get()
                                    }*/

                                    var str = document?.get(doc.id).toString()
                                    product.add(AnnounceData(map.value.toString(),str, doc.id))
                                }
                            }
                        }
                        setting.value = "finish"
                    }
                }
            }
            else {

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
                        products.document("복합물품").collection("sublist").get()
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
                        products.document(AddViewModel.products[i])
                            .update(list[i])
                    } else {
                        products.document("복합물품").collection("sublist").document(
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
            val col = products
            val sub = col.document("복합물품").collection("sublist")

            col.document(kinds[0]).update(barcode, names[0])


            for(i in 1 until names.size){
                sub.document(kinds[i]).update(names[i], subnames[i])
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
            if(it.isSuccessful){
                Toast.makeText(context,"이미지 업로드 완료",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"이미지 업로드 실패",Toast.LENGTH_SHORT).show()
            }

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