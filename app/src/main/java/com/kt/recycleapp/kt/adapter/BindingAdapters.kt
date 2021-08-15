package com.kt.recycleapp.kt.adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import com.kt.recycleapp.java.announce.AnnounceAdapter
import com.kt.recycleapp.java.announce.AnnounceData
import com.kt.recycleapp.kt.etc.FavoriteData
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

    @BindingAdapter("imgBitmap")
    @JvmStatic
    fun imgSet(img: ImageView,bm: RequestBuilder<Drawable>){
        bm.into(img)
    }

    @BindingAdapter("smallItem")
    @JvmStatic
    fun bindSmallItem(recyclerView: RecyclerView, item:ObservableArrayList<FindSmallData>){
        val adt = FindSmallAdapter()
        val lm = LinearLayoutManager(recyclerView.context)

        recyclerView.layoutManager = lm
        recyclerView.adapter = adt
        (recyclerView.adapter as FindSmallAdapter).items = item
        recyclerView.adapter?.notifyDataSetChanged()
    }

    @BindingAdapter("historyItem")
    @JvmStatic
    fun historyItem(recyclerView: RecyclerView, item:ObservableArrayList<HistoryData>) {
        val adt = HistoryAdapter()
        val lm = LinearLayoutManager(recyclerView.context)

        recyclerView.layoutManager = lm
        recyclerView.adapter = adt
        (recyclerView.adapter as HistoryAdapter).items = item
        recyclerView.adapter?.notifyDataSetChanged()
    }

    @BindingAdapter("favoriteItem")
    @JvmStatic
    fun favoriteItem(recyclerView: RecyclerView, item:ObservableArrayList<FavoriteData>) {
        val adt = FavoriteAdapter()
        val lm = LinearLayoutManager(recyclerView.context)

        recyclerView.layoutManager = lm
        recyclerView.adapter = adt
        (recyclerView.adapter as FavoriteAdapter).items = item
        recyclerView.adapter?.notifyDataSetChanged()
    }

    @BindingAdapter("add")
    @JvmStatic
    fun addItem(recyclerView: RecyclerView,item:ObservableArrayList<Int>) {
        val adt = AddAdapter()
        val lm = LinearLayoutManager(recyclerView.context)

        recyclerView.layoutManager = lm
        recyclerView.adapter = adt
        (recyclerView.adapter as AddAdapter).items = item
        recyclerView.adapter?.notifyDataSetChanged()
    }

    /*@BindingAdapter("announce")
    @JvmStatic
    fun set(recyclerView: RecyclerView, item: ObservableArrayList<AnnounceData>) {
        val adt = AnnounceAdapter()
        val lm = LinearLayoutManager(recyclerView.context)
        lm.orientation = RecyclerView.HORIZONTAL

        recyclerView.layoutManager = lm
        recyclerView.adapter = adt
        (recyclerView.adapter as AnnounceAdapter).setItems(item)
        recyclerView.adapter?.notifyDataSetChanged()
    }*/

}