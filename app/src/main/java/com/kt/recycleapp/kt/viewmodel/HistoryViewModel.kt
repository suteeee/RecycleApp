package com.kt.recycleapp.kt.viewmodel

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.RequestBuilder
import com.kt.recycleapp.kt.activity.MainActivity
import com.kt.recycleapp.kt.etc.HistoryData
import com.kt.recycleapp.manager.MyPreferenceManager
import com.kt.recycleapp.model.DatabaseReadModel
import com.kt.recycleapp.model.MyRoomDatabase
import com.kt.recycleapp.model.RoomHelper

class HistoryViewModel : ViewModel() {
    companion object{
        var selected = MutableLiveData<Int>()
        var image : RequestBuilder<Drawable>? = null
    }

    var getProductName = MutableLiveData<String>()
    var decodeState = MutableLiveData<String>()

    var itemList = ObservableArrayList<HistoryData>()
    var tempList = ObservableArrayList<HistoryData>()
    var myRoomDbList :List<MyRoomDatabase>? = null
    var name:Map<String,String> = HashMap()
    val model = DatabaseReadModel()

    fun getFireData(){
        model.getProduct(getProductName)
    }

    fun getData(helper: RoomHelper?, prefs: MyPreferenceManager, activity: MainActivity) {
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


                //Log.d("것",DatabaseReadModel.name[it.barcode]!!)



            if(DatabaseReadModel.name[it.barcode] == null){
                barcode = "바코드 값 : ${it.barcode}"
            }
            else{
                barcode = "제품명 : ${DatabaseReadModel.name[it.barcode]}"
            }
            itemList.add(HistoryData(date,image,barcode))
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