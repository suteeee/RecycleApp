package com.kt.recycleapp.kt.viewmodel

import android.graphics.BitmapFactory
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.kt.recycleapp.kt.etc.FavoriteData
import com.kt.recycleapp.kt.etc.HistoryData
import com.kt.recycleapp.model.DatabaseReadModel
import com.kt.recycleapp.model.RoomHelper

class FavoriteItemFragmentViewModel: ViewModel() {
    val itemList = ObservableArrayList<FavoriteData>()

    fun setData(activity: FragmentActivity?, helper: RoomHelper) {
      helper.databaseDao().getAll().forEach {
          if(it.favorite == "true"){
              val image = it.image

            val decodeThread = DecodeThread(activity,image,it.dateTime,it.barcode)
              decodeThread.start()
          }
      }

    }


    inner class DecodeThread(val activity: FragmentActivity?, val image: String?, val date: String?, var barcode: String?) :Thread() {
        override fun run() {
            val path ="${activity?.externalMediaDirs?.get(0)}/수거했어 오늘도!/${image}"
            Log.d(path,"것")
            val bm = BitmapFactory.decodeFile(path)

            var date1 = ""
            var date2 = ""
            var date3 = ""

            if(date != "Recycle") {

                if (date != null) {
                    date1 = date.substring(0..3)
                    date2 = date.substring(4..5)
                    date3 = date.substring(6..7)
                }

            }
            var date = "${date1}년 ${date2}월 ${date3}일"
            if(date == "Recycle"){
                date = "2021년 07월 31일"
            }

            if(DatabaseReadModel.name[barcode] == null){
                barcode = "바코드 값 : ${barcode}"
            }
            else{
                barcode = "제품명 : ${DatabaseReadModel.name[barcode]}"
            }

            itemList.add(FavoriteData(bm,barcode,date))
        }
    }
}