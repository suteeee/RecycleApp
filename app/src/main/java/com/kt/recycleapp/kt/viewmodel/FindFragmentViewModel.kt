package com.kt.recycleapp.kt.viewmodel

import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kt.recycleapp.kt.etc.FindBigData
import com.kt.recycleapp.kt.fragment.FindSmallFragment
import com.kt.recycleapp.model.DatabaseReadModel
import java.recycleapp.R

class FindFragmentViewModel: ViewModel() {
    val model = DatabaseReadModel()
    val itemList = ObservableArrayList<FindBigData>()
    var itemData = ArrayList<HashMap<String,String>>()
    var itemDataSmall = ArrayList<HashMap<String,String>>()
    var findBigProgress = MutableLiveData<String>()
    var findSmallProgress = MutableLiveData<String>()

    val imgArr = arrayOf(R.drawable.paper, R.drawable.plastic, R.drawable.vinyl, R.drawable.iron, R.drawable.delivery, R.drawable.constore, R.drawable.baterry, R.drawable.trash, R.drawable.pet)

    fun addItem(index:Int){
        itemList.add(FindBigData(imgArr[index],itemData[index][index.toString()]!!))
    }

    fun findBig() :ArrayList<HashMap<String,String>>{
        itemData = model.findBig(findBigProgress)
        Log.d(findBigProgress.toString(),"doc")

        return itemData
    }

    fun findSmall():ArrayList<HashMap<String,String>>{
        itemDataSmall = model.findSmall(findSmallProgress)

        return itemDataSmall
    }
}