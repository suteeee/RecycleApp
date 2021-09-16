package com.kt.recycleapp.kotlin.viewmodel

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.RequestBuilder
import com.kt.recycleapp.kotlin.activity.MainActivity
import com.kt.recycleapp.kotlin.etc.HistoryData
import com.kt.recycleapp.manager.MyPreferenceManager
import com.kt.recycleapp.model.DatabaseReadModel
import com.kt.recycleapp.model.MyRoomDatabase
import com.kt.recycleapp.model.RoomHelper

class HistoryViewModel : ViewModel() {
    companion object{
        var selected = MutableLiveData<Int>(-1)
    }

    var getProductName = MutableLiveData<String>()
    var decodeState = MutableLiveData<String>()
    var cnt = 0
    var itemList = ObservableArrayList<HistoryData>()
    var tempList = ObservableArrayList<HistoryData>()
    var myRoomDbList :List<MyRoomDatabase>? = null
    var name:Map<String,String> = HashMap()
    val model = DatabaseReadModel.instance

    fun getFireData(){
        model.getProduct(getProductName)
    }

    fun getData(helper: RoomHelper?, prefs: MyPreferenceManager, activity: MainActivity) {

        myRoomDbList = helper?.databaseDao()?.getAll()
        myRoomDbList?.forEach {
            var date1:String? = ""
            var date2 :String?= ""
            var date3 :String?= ""
            val image = it.image
            var barcode = ""

            var arr :ArrayList<String> = ArrayList()
            if(prefs.favoriteList != null)
                arr = prefs.favoriteList
            arr.add(image!!)
            prefs.favoriteList = arr

            date1 = it.dateTime?.substring(0..3)
            date2 = it.dateTime?.substring(4..5)
            date3 = it.dateTime?.substring(6..7)

            val date = "${date1}년 ${date2}월 ${date3}일"

            if(DatabaseReadModel.name[it.barcode] == null){
                barcode = "바코드 값 : ${it.barcode}"
            }
            else{
                barcode = "제품명 : ${DatabaseReadModel.name[it.barcode]}"
            }
            itemList.add(HistoryData(date,image,barcode,cnt++))
        }

        MainActivity.historyItemsForSearch = itemList
    }

    fun filterList(historyItemsForSearch: ArrayList<HistoryData>) {
        Log.d("search","filter")
        itemList.forEach { tempList.add(it) }
        itemList.clear()
        historyItemsForSearch.forEach {
            itemList.add(it)
        }
    }

    fun resetList() {
        itemList.clear()
        tempList.forEach { itemList.add(it) }
        tempList.clear()
        MainActivity.historyItemsForSearch = itemList
    }
}