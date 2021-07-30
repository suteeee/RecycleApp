package com.kt.recycleapp.kt.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kt.recycleapp.kt.etc.FindBigData
import com.kt.recycleapp.kt.etc.FindSmallData
import com.kt.recycleapp.model.DatabaseReadModel
import java.recycleapp.R

class FindFragmentViewModel: ViewModel() {
    companion object{
        var selectDoc = ""
    }
    val model = DatabaseReadModel()
    val itemList = ObservableArrayList<FindBigData>()
    val smallItemList = ObservableArrayList<FindSmallData>()
    var itemData = ArrayList<HashMap<String,String>>()
    var itemDataSmall =  ArrayList<ArrayList<HashMap<String,String>>>()
    var findBigProgress = MutableLiveData<String>()
    var findSmallProgress = MutableLiveData<String>()

    val imgArr = arrayOf(R.drawable.paper, R.drawable.plastic, R.drawable.vinyl, R.drawable.iron, R.drawable.delivery, R.drawable.constore, R.drawable.baterry, R.drawable.trash, R.drawable.pet)
    val paperPng = arrayOf(R.drawable.paperwrap,R.drawable.paperbox,R.drawable.papebag)
    val platicPng = arrayOf(R.drawable.plasticlego,R.drawable.plasticpettop,R.drawable.strrow,R.drawable.plasticpringlestop)

    fun addItem(index:Int){
        itemList.add(FindBigData(imgArr[index],itemData[index][index.toString()]!!))
    }

    fun addSmallItem(index: Int, idx: Int){
        var temp = paperPng
        when(idx){
            0->temp = paperPng
            1->temp = platicPng
        }
        smallItemList.add(FindSmallData(temp[index],itemDataSmall[idx][index][index.toString()]!!))
    }

    fun findBig() :ArrayList<HashMap<String,String>>{
        itemData = model.findBig(findBigProgress)
        Log.d(findBigProgress.toString(),"doc")

        return itemData
    }

    fun findSmall(): ArrayList<ArrayList<HashMap<String,String>>>{
        itemDataSmall = model.findSmall(findSmallProgress)

        return itemDataSmall
    }
}