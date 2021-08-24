package com.kt.recycleapp.kotlin.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kt.recycleapp.kotlin.activity.MainActivity
import com.kt.recycleapp.kotlin.etc.FavoriteData
import com.kt.recycleapp.model.DatabaseReadModel
import com.kt.recycleapp.model.RoomHelper

class FavoriteViewModel: ViewModel() {
    companion object{
        var selected = MutableLiveData<Int>()
    }
    val itemList = ObservableArrayList<FavoriteData>()
    var tempList = ObservableArrayList<FavoriteData>()
    val model = DatabaseReadModel()
    var getProductName = MutableLiveData<String>()

    fun setData(helper: RoomHelper) {
      helper.databaseDao().getFavoriteAll().forEach {
          val image = it.image
          val date = it.dateTime
          var barcode = it.barcode

          var date1 = ""
          var date2 = ""
          var date3 = ""


          if (date != null) {
              date1 = date.substring(0..3)
              date2 = date.substring(4..5)
              date3 = date.substring(6..7)
          }
          var newDate = "${date1}년 ${date2}월 ${date3}일"

          DatabaseReadModel.name.forEach {
              Log.d(it.key,it.value)
          }

          if(barcode == null){
              barcode = "바코드 값 : ${barcode}"
          }
          else{
              barcode = "제품명 : ${DatabaseReadModel.name[barcode].toString()}"
          }


          itemList.add(FavoriteData(image,barcode,newDate))
      }
        MainActivity.favoriteItemForSearch = itemList
    }

    fun getFireData(){
        model.getProduct(getProductName)
    }

    fun filterList(historyItemsForSearch: ArrayList<FavoriteData>) {
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
        MainActivity.favoriteItemForSearch = itemList
    }
}