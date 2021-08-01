package com.kt.recycleapp.kt.adapter

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kt.recycleapp.kt.etc.FindBigData
import com.kt.recycleapp.kt.etc.FindSmallData
import com.kt.recycleapp.kt.etc.HistoryData

object BindingAdapters {
    @BindingAdapter("bind:item")
    @JvmStatic
    fun bindItem(recyclerView: RecyclerView, item:ObservableArrayList<FindBigData>){
        val adt = FindBigAdapter()
        val lm = LinearLayoutManager(recyclerView.context)

        recyclerView.layoutManager = lm
        recyclerView.adapter = adt
        (recyclerView.adapter as FindBigAdapter).items = item
        recyclerView.adapter?.notifyDataSetChanged()
    }

    @BindingAdapter("imgRes")
    @JvmStatic
    fun imgLoad(img:ImageView, id:Int) {
        img.setImageResource(id)
    }

    @BindingAdapter("bind:imgBitmap")
    @JvmStatic
    fun imgSet(img: ImageView,bm: Bitmap){
        img.setImageBitmap(bm)
    }

    @BindingAdapter("bind:smallItem")
    @JvmStatic
    fun bindSmallItem(recyclerView: RecyclerView, item:ObservableArrayList<FindSmallData>){
        val adt = FindSmallAdapter()
        val lm = LinearLayoutManager(recyclerView.context)

        recyclerView.layoutManager = lm
        recyclerView.adapter = adt
        (recyclerView.adapter as FindSmallAdapter).items = item
        recyclerView.adapter?.notifyDataSetChanged()
    }

    @BindingAdapter("bind:historyItem")
    @JvmStatic
    fun historyItem(recyclerView: RecyclerView, item:ObservableArrayList<HistoryData>) {
        val adt = HistoryAdapter()
        val lm = LinearLayoutManager(recyclerView.context)

        recyclerView.layoutManager = lm
        recyclerView.adapter = adt
        (recyclerView.adapter as HistoryAdapter).items = item
        recyclerView.adapter?.notifyDataSetChanged()
    }



}