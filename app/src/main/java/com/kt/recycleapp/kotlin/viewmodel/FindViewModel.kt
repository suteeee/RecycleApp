package com.kt.recycleapp.kotlin.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kt.recycleapp.kotlin.activity.MainActivity
import com.kt.recycleapp.kotlin.etc.FindBigData
import com.kt.recycleapp.kotlin.etc.FindSmallData
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
    var itemData = ArrayList<String>()
    var itemDataSmall =  ArrayList<HashMap<String,String>>()
    var findBigProgress = MutableLiveData<String>()
    var findSmallProgress = MutableLiveData<String>()
    var bigTempList = ObservableArrayList<FindBigData>()
    var smallTempList = ObservableArrayList<FindSmallData>()
    var bigidx = 0
    var smallidx = 0

    val imgArr = arrayOf(R.drawable.ic_baterry_default, R.drawable.ic_iron_default, R.drawable.ic_mix_default, R.drawable.ic_vinyl_default,
        R.drawable.ic_glass_default, R.drawable.ic_trash_default, R.drawable.ic_paper_default, R.drawable.ic_can_default, R.drawable.ic_pet_default,R.drawable.ic_plastic_default)

    fun addItem(index:Int){
        itemList.add(FindBigData(imgArr[index],itemData[index],bigidx++))
    }

    fun addSmallItem(){
        var idx = 0
        when(selectDoc){
            "건전지"-> idx = 0
            "고철" -> idx = 1
            "복합물품"-> idx = 2
            "비닐" -> idx = 3
            "유리"-> idx = 4
            "일반쓰레기" -> idx = 5
            "종이"-> idx = 6
            "캔" -> idx = 7
            "페트병"-> idx = 8
            "플라스틱" -> idx = 9
        }

        val temp =  imgArr[idx]
        var cnt = 0

        itemDataSmall.forEach {
            smallItemList.add(FindSmallData(temp,itemDataSmall[cnt].values.elementAt(0),smallidx++))
            cnt++
        }
    }

    fun findBig() :ArrayList<String>{
        itemData = model.findBig(findBigProgress)
        return itemData
    }

    fun findSmall(): ArrayList<HashMap<String,String>>{
        itemDataSmall = model.findSmall(findSmallProgress)

        return itemDataSmall
    }

    fun bigFilterList(findBigForSearch: ArrayList<FindBigData>) {
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