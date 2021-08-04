package com.kt.recycleapp.kt.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kt.recycleapp.kt.etc.HistoryData
import com.kt.recycleapp.manager.MyPreferenceManager
import com.kt.recycleapp.model.DatabaseReadModel
import com.kt.recycleapp.model.MyRoomDatabase
import com.kt.recycleapp.model.RoomHelper

class HistoryViewModel : ViewModel() {
    companion object{
        var selected = MutableLiveData<Int>()
    }

    var getProductName = MutableLiveData<String>()
    var decodeState = MutableLiveData<String>()

    val itemList = ObservableArrayList<HistoryData>()
    var myRoomDbList :List<MyRoomDatabase>? = null
    var name:Map<String,String> = HashMap()
    val model = DatabaseReadModel()
    var bm :Bitmap? = null

    fun getFireData(){
        model.getProduct(getProductName)
    }

    fun getData(helper: RoomHelper?, activity: FragmentActivity?, prefs: MyPreferenceManager) {
        myRoomDbList = helper?.databaseDao()?.getAll()
        myRoomDbList?.forEach {
            var date1:String? = ""
            var date2 :String?= ""
            var date3 :String?= ""
            var image = it.image
            var barcode = ""

            var arr :ArrayList<String> = ArrayList()
            if(prefs.favoriteList != null)
                arr = prefs.favoriteList
            arr.add(image!!)
            prefs.favoriteList = arr

            Log.d(prefs.favoriteList.toString(),"거거")

            if(it.dateTime != "Recycle") {

                date1 = it.dateTime?.substring(0..3)
                date2 = it.dateTime?.substring(4..5)
                date3 = it.dateTime?.substring(6..7)
            }
            var date = "${date1}년 ${date2}월 ${date3}일"
            if(it.dateTime == "Recycle"){
                date = "2021년 07월 31일"
            }

            if(DatabaseReadModel.name[it.barcode] == null){
                barcode = "바코드 값 : ${it.barcode}"
            }
            else{
                barcode = "제품명 : ${DatabaseReadModel.name[it.barcode]}"
            }

            val decodeThread = DecodeThread(activity,it.image,date,barcode)
            decodeThread.start()

        }
    }

    inner class DecodeThread(val activity: FragmentActivity?, val image: String?, val date: String, val barcode: String) :Thread() {
        override fun run() {
            val path ="${activity?.externalMediaDirs?.get(0)}/수거했어 오늘도!/${image}"
            Log.d(path,"junnn")
            bm = BitmapFactory.decodeFile(path)
            itemList.add(HistoryData(date,bm,barcode))
        }
    }
}