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
    private val MULTIPLE = "multiple"
    private val HAVE_MULTIPLE_CHECK = "haveMultipleProduct"

    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance(STORAGE_URL).reference
    var product = db.collection("product")
    var resultInfo = db.collection("resultInfo")
    var kind: String = ""
    var myRoomDbList :List<MyRoomDatabase>? = null
    var myRoomDbListR :List<MyRoomDatabase>? = null
    var myRoomFDBList :List<MyRoomDatabase>? = null
    var productName = HashMap<String, String>()
    var nameForSetImage = ""

    var productsKindList = ArrayList<String>()

    companion object {
        val instance: DatabaseReadModel = DatabaseReadModel()
    }

    fun findBig(findBigProgress: MutableLiveData<String>): ArrayList<String> {
        findBigProgress.value = "start"
        if(productsKindList.isEmpty()) {
            resultInfo.get().addOnCompleteListener {
                it.result.documents.forEach { doc->
                    doc.data?.keys?.forEach { key ->
                        productsKindList.add(key)
                    }
                }
                findBigProgress.value = "finish"
            }
        }else {
            findBigProgress.value = "finish"
        }

        return productsKindList
    }

    fun findSmall(findSmallProgress: MutableLiveData<String>): ArrayList<HashMap<String, String>> {
        findSmallProgress.value = "start"
        var selected = FindViewModel.selectDoc
        val arr = ArrayList<HashMap<String, String>>()

        product.whereEqualTo("kind",selected).get().addOnCompleteListener {
            it.result.documents.forEach { doc->
                val temp = HashMap<String, String>()
                temp[doc.data?.get("barcode").toString()] = doc.data?.get("name").toString()
                arr.add(temp)
            }
            findSmallProgress.value = "finish"
        }
        return arr
    }


    fun getSQLiteData(helper: RoomHelper?, getSQLDataFinish: MutableLiveData<Boolean>):List<MyRoomDatabase> {
        getSQLDataFinish.value = false
        if(myRoomDbList == null) { myRoomDbList = helper?.databaseDao()?.getAllDesc() }
        getSQLDataFinish.value = true
        return myRoomDbList!!
    }

    fun getSQLiteDataR(helper: RoomHelper?, getSQLDataFinish: MutableLiveData<Boolean>){
        getSQLDataFinish.value = false
        if(myRoomDbListR == null) { myRoomDbListR = helper?.databaseDao()?.getAll() }
        getSQLDataFinish.value = true

    }

    fun getSQLitefavoriteData(helper: RoomHelper?, getSQLiteFDataFinish: MutableLiveData<Boolean>) {
        getSQLiteFDataFinish.value = false
        if(myRoomFDBList == null) { myRoomFDBList = helper?.databaseDao()?.getFavoriteAll() }
        getSQLiteFDataFinish.value = true
    }

    /*
    *History, Favorite에서 사용됨
    *물품 바코드와 이름을 로딩
    * */

    fun getProduct(getProductName: MutableLiveData<String>) {
        getProductName.value = "start"
        if(productName.isEmpty()) {
            product.get().addOnCompleteListener {
                it.result.documents.forEach { doc ->
                    val data = doc.data
                    productName.put(data?.get("barcode").toString(), data?.get("name").toString())
                }
                getProductName.value = "finish"
            }
        }
        else {
            getProductName.value = "finish"
        }
    }

    /*
    * AddPopupPage, AddViewModel, DataUploadViewModel에서 사용됨
    * 물품 분류 리스트를 로드하는 함수
    * */
    fun getProductsList(arr: MutableLiveData<String>): ArrayList<String> {
        arr.value = "start"

        if(productName.isEmpty()) {
            resultInfo.get().addOnCompleteListener {
                it.result.documents.forEach { doc->
                    doc.data?.keys?.forEach { key->
                        productsKindList.add(key)
                    }
                }
                arr.value = "finish"
            }
        }else {
            arr.value = "finish"
        }

        return productsKindList
    }

    /*
    * AnnounceRecycler에서 사용
    * 특정 물품의 분류를 조회하는 함수
    * */
    fun findProductKind(finding: MutableLiveData<String>, product: String, kind: MutableLiveData<String>) {
        finding.value = "start"
        var res = ""

        //물품 명으로 조회
        this.product.whereEqualTo("name",product).get()
            .addOnCompleteListener {
            if(!it.result.isEmpty) {
                it.result.documents.forEach { doc->
                    res = doc.data?.get("kind").toString()
                    kind.value = res
                    this.kind = res
                }
            }
            else {
                kind.value = res
                this.kind = res
            }

            finding.value = "finish"
        }
    }

    fun settingResult(setting: MutableLiveData<String>, kind: String, product: ObservableArrayList<AnnounceData>, barcode: String) {
        setting.value = "start"
        /*
        * barcode 값은 캡쳐해서 들어올 경우 880065465 등, 탐색해서 들어올 경우 물품 명. -> 히스토리 통해 없는걸로 들어오면 숫자.
        * */

        var name = barcode
        var info = ""
        var pKind = kind
        var document: Map<String, Any>? = null

        var productName = this.product.whereEqualTo("name", barcode)
        var productBarcode = this.product.whereEqualTo("barcode", barcode)

        var productData: Map<String, Any>? = HashMap()

        fun set() {
            name = productData?.get("name").toString()
            pKind = productData?.get("kind").toString()
            val pInfo = productData?.get("info").toString()
            info =
                if (pInfo.isNotEmpty()) {
                    pInfo
                } else {
                    document?.get(pKind).toString()
                }
        }

        fun multiAdd(name:String) {
            val multiPrd = this.product.document(name).collection(MULTIPLE).get()
            multiPrd.addOnCompleteListener {
                it.result.documents.forEach { doc ->
                    val tData = doc.data
                    val tName = tData?.get("name").toString()
                    val tKind = tData?.get("kind").toString()
                    val tInfo =
                        if(tData?.get("info").toString().isNotEmpty()) {tData?.get("info").toString()}
                        else {document?.get(tKind).toString()}

                    product.add(AnnounceData(tName, tInfo, tKind))
                }
                setting.value = "finish"
            }
        }

        resultInfo.get().addOnCompleteListener {
            (it.result.documents).forEach { doc ->
                document = doc.data
            }

            productBarcode.get().addOnCompleteListener { bit ->
                val barcodeRes = bit.result.documents
                if(barcodeRes.size != 0) { //바코드 번호로 조회 완료
                    barcodeRes.forEach { barcodeDoc ->
                        productData = barcodeDoc.data
                        set()
                    }
                    product.add(AnnounceData(name, info, pKind))
                    if(productData?.get(HAVE_MULTIPLE_CHECK) as Boolean) {
                        multiAdd(name)
                    }
                    else {
                        setting.value = "finish"
                    }

                }else { //바코드 번호로 조회 실패
                    productName.get().addOnCompleteListener { nit ->
                        val nameRes = nit.result.documents
                        if(nameRes.size != 0) { //물품 명으로 조회 완료
                            nameRes.forEach { nameDoc ->
                                productData = nameDoc.data
                                set()
                                product.add(AnnounceData(name, info, pKind))

                                if(productData?.get(HAVE_MULTIPLE_CHECK) as Boolean) {
                                    multiAdd(name)
                                }else {
                                    setting.value = "finish"
                                }
                            }
                        }
                        else { //물품 없음
                            info = "데이터를 등록해주세요!"
                            pKind = "등록되지 않은 물품입니다."
                            product.add(AnnounceData(name, info, pKind))
                            setting.value = "finish"
                        }
                    }
                }
            }
        }
    }

    fun setImage(context: Context, imageView: ImageView, progressBar: ProgressBar, itemName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            fun initImg() :Int{
                var id = R.drawable.default_nothing
                when (kind) {
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
                return id
            }

            storage.child("products_image/IMAGE_${itemName.replace(" ", "")}.png")
                .downloadUrl.addOnSuccessListener {
                    Glide.with(context).load(it).override(500).into(imageView)
                    progressBar.visibility = View.INVISIBLE
                }
                .addOnFailureListener {
                    var id = R.drawable.default_nothing
                    Log.d("kindname", itemName)

                    //해당 물품명으로 조회
                    product.whereEqualTo("name",itemName).get().addOnCompleteListener {
                        //일반 물품중에서 존재할때
                        Log.d(it.result.isEmpty.toString(),"kindname")
                        if(!it.result.isEmpty) {
                            it.result.forEach { res ->
                                kind = res.data["kind"].toString()
                                id = initImg()
                                Glide.with(context).load(id).override(500).into(imageView)
                                progressBar.visibility = View.INVISIBLE
                            }
                        }else { //없을때
                            product.document(nameForSetImage).collection(MULTIPLE).get().addOnCompleteListener { mit ->
                                if(!mit.result.isEmpty) { //복합물품 안에는 존재할때
                                    mit.result.forEach { mRes ->
                                        kind = mRes.data["kind"].toString()
                                        id = initImg()
                                        Glide.with(context).load(id).override(500).into(imageView)
                                        progressBar.visibility = View.INVISIBLE
                                    }
                                }
                                else { //다 없으면
                                    Glide.with(context).load(id).override(500).into(imageView)
                                    progressBar.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }
                }
        }
    }

    fun makeData(idx:Int, kinds:ArrayList<String>, names: ArrayList<String>, barcode: String, exList: ArrayList<String>,check:Boolean) :HashMap<String,Any>{
        val item:HashMap<String,Any>?
        item = hashMapOf(
            "name" to names[idx],
            "barcode" to barcode,
            "kind" to kinds[idx],
             HAVE_MULTIPLE_CHECK to check,
            "info" to exList[idx]
        )
        return item
    }

    fun makeMultiData(idx:Int,kinds:ArrayList<String>,names: ArrayList<String>, exList: ArrayList<String>) :HashMap<String,Any>{
        val item:HashMap<String,Any>?
        item = hashMapOf(
            "name" to names[idx],
            "kind" to kinds[idx],
            "info" to exList[idx]
        )
        return item
    }

    fun uploadAll(photoUri: Uri?, list: ArrayList<HashMap<String, Any>>,exList : ArrayList<String>) {
        var check = false
        val names =ArrayList<String>()
        val barcode = list[0].keys.elementAt(0)

        fun uploadImg(name:String,photoUri: Uri) {
            CoroutineScope(Dispatchers.IO).launch{
                val fileName = "IMAGE_$name"
                val imgRef = storage.child("products_image/$fileName.png")
                imgRef.putFile(photoUri)
            }
        }

        list.forEach { it.values.forEach { name -> names.add(name.toString()) }}
        Log.d(list.toString(),"plz")
        if(names.size != 1) { check = true }

        try {
            for (i in 0 until list.size) {
                if (i == 0) { product.document(names[i]).set(makeData(i,AddViewModel.kinds,names,barcode,exList,check)) }
                else { product.document(names[0]).collection(MULTIPLE).document(names[i]).set(makeMultiData(i,AddViewModel.kinds,names,exList)) }
            }
        }catch (e:Exception) {}

        if(photoUri != null) {
            uploadImg(names[0],photoUri)
        }



        AddViewModel.addItems.clear()
    }

    fun uploadData
                (barcode: String, names: ArrayList<String>, kinds: ArrayList<String>, subnames: ArrayList<String>,
                 uploadFinish: MutableLiveData<String>, photoUri: Uri?, infoText: ArrayList<String>) {

        CoroutineScope(Dispatchers.IO).launch{
            uploadFinish.postValue("start")
            var check = false

            if(kinds.size != 1) check = true

            for(i in 0 until kinds.size) {
                if(i == 0) {
                    product.document(names[i])
                        .set(makeData(i,kinds,names,barcode,infoText,check))
                }
                else {
                    product.document(names[0]).collection(MULTIPLE).document(names[i])
                        .set(makeMultiData(i,kinds,names,infoText))
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

        product.whereEqualTo("barcode",barcode).get().addOnCompleteListener {
            var check = false
            val size = it.result.documents.size
            if(size == 0) {
                product.whereEqualTo("name",barcode).get().addOnCompleteListener { name ->
                    if(name.result.documents.size != 0){
                       check = true
                    }
                    isHaveBarcode.value = check
                    checkBarcodeFinish.value = true
                }
            }else {
                check = true
                isHaveBarcode.value = check
                checkBarcodeFinish.value = true
            }


        }

    }



}