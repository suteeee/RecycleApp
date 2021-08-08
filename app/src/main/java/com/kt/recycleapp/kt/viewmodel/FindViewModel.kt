package com.kt.recycleapp.kt.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kt.recycleapp.kt.activity.MainActivity
import com.kt.recycleapp.kt.etc.FavoriteData
import com.kt.recycleapp.kt.etc.FindBigData
import com.kt.recycleapp.kt.etc.FindSmallData
import com.kt.recycleapp.model.DatabaseReadModel
import java.recycleapp.R

class FindViewModel: ViewModel() {
    companion object{
        var selectDoc = ""
        var selectItem = ""
    }
    val model = DatabaseReadModel()
    val itemList = ObservableArrayList<FindBigData>()
    val smallItemList = ObservableArrayList<FindSmallData>()
    var itemData = ArrayList<HashMap<String,String>>()
    var itemDataSmall =  ArrayList<HashMap<String,String>>()
    var findBigProgress = MutableLiveData<String>()
    var findSmallProgress = MutableLiveData<String>()
    var bigTempList = ObservableArrayList<FindBigData>()
    var smallTempList = ObservableArrayList<FindSmallData>()

    val imgArr = arrayOf(R.drawable.paper, R.drawable.plastic, R.drawable.vinyl, R.drawable.iron, R.drawable.delivery, R.drawable.constore, R.drawable.baterry, R.drawable.trash, R.drawable.pet)
    val paperPng = arrayOf(R.drawable.paperwrap,R.drawable.paperbox,R.drawable.papebag)
    val platicPng = arrayOf(R.drawable.plasticlego,R.drawable.plasticpettop,R.drawable.strrow,R.drawable.plasticpringlestop)

    fun addItem(index:Int){
        itemList.add(FindBigData(imgArr[index],itemData[index][index.toString()]!!))
    }

    fun addSmallItem(idx: Int){
        var temp = paperPng
        when(idx){
            0->temp = paperPng
            1->temp = platicPng
        }

        var cnt = 0

        itemDataSmall.forEach {
            smallItemList.add(FindSmallData(temp[idx],itemDataSmall[cnt].values.elementAt(0)))
            cnt++
        }
    }

    fun findBig() :ArrayList<HashMap<String,String>>{
        itemData = model.findBig(findBigProgress)
        return itemData
    }

    fun findSmall(): ArrayList<HashMap<String,String>>{
        itemDataSmall = model.findSmall(findSmallProgress)

        return itemDataSmall
    }

    fun bigFilterList(findBigForSearch: ArrayList<FindBigData>) {
        Log.d("search","filter")
        itemList.forEach { bigTempList.add(it) }
        itemList.clear()
        findBigForSearch.forEach {
            itemList.add(it)
        }
    }

    fun bigResetList() {
        itemList.clear()
        bigTempList.forEach { itemList.add(it) }
        bigTempList.clear()
        MainActivity.findBigForSearch = itemList
    }

    fun smallFilterList(findSmallForSearch: ArrayList<FindSmallData>) {
        Log.d("search","filter")
        smallItemList.forEach { smallTempList.add(it) }
        smallItemList.clear()
        findSmallForSearch.forEach {
            smallItemList.add(it)
        }
    }

    fun smallResetList() {
        smallItemList.clear()
        smallTempList.forEach { smallItemList.add(it) }
        smallTempList.clear()
        MainActivity.findSmallForSearch = smallItemList
    }
}