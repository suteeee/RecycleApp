package com.kt.recycleapp.kotlin.favorite

import android.view.View
import android.widget.TextView
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kt.recycleapp.kotlin.main.MainActivity
import com.kt.recycleapp.model.DatabaseReadModel
import com.kt.recycleapp.model.RoomHelper

class FavoriteViewModel: ViewModel() {
    companion object{
        var selected = MutableLiveData<Int>()
    }
    val itemList = ObservableArrayList<FavoriteData>()
    var tempList = ObservableArrayList<FavoriteData>()
    val model = DatabaseReadModel.instance
    var getProductName = MutableLiveData<String>()
    var getSQLiteFDataFinish = MutableLiveData<Boolean>()
    var idx = 0

    fun getSQLiteFavoriteData(helper: RoomHelper) {
        model.getSQLitefavoriteData(helper,getSQLiteFDataFinish)
    }

    fun setData(helper: RoomHelper, favoriteNoItemTv: TextView) {
      model.myRoomFDBList?.forEach {
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

          if(model.productName[barcode].toString() != "null") {
              barcode = "제품명 : ${model.productName[barcode]}"
          }else {
              barcode = "바코드 값 : ${barcode}"
          }

          itemList.add(FavoriteData(image,barcode,newDate,idx++))
      }
        if(itemList.isEmpty()) {
            favoriteNoItemTv.visibility = View.VISIBLE
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