package com.kt.recycleapp.kt.viewmodel

import android.graphics.BitmapFactory
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kt.recycleapp.kt.etc.FavoriteData
import com.kt.recycleapp.model.DatabaseReadModel
import com.kt.recycleapp.model.RoomHelper

class FavoriteViewModel: ViewModel() {
    companion object{
        var selected = MutableLiveData<Int>()
    }
    val itemList = ObservableArrayList<FavoriteData>()

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


          if(DatabaseReadModel.name[barcode] == null){
              barcode = "바코드 값 : ${barcode}"
          }
          else{
              barcode = "제품명 : ${DatabaseReadModel.name[barcode]}"
          }

          itemList.add(FavoriteData(image,barcode,newDate))
      }
    }
}