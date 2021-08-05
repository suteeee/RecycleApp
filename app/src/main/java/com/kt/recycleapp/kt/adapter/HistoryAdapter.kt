package com.kt.recycleapp.kt.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.kt.recycleapp.kt.etc.HistoryData
import com.kt.recycleapp.kt.viewmodel.HistoryViewModel
import com.kt.recycleapp.manager.MyPreferenceManager
import com.kt.recycleapp.model.MyRoomDatabase
import com.kt.recycleapp.model.RoomHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.recycleapp.databinding.HistoryLayoutUnitBinding
import java.util.logging.Handler

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.MyHolder>() {
    var items = ArrayList<HistoryData>()
    lateinit var prefs :MyPreferenceManager
    var helper:RoomHelper? = null
    var list:List<MyRoomDatabase>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = HistoryLayoutUnitBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        prefs = MyPreferenceManager(parent.context)
        helper = Room.databaseBuilder(parent.context,RoomHelper::class.java,"Database").allowMainThreadQueries().build()

        list = helper?.databaseDao()?.getAll()

        val holder = MyHolder(binding,parent.context)
        return holder
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MyHolder(private val binding: HistoryLayoutUnitBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: HistoryData, position: Int) {
            GlobalScope.launch {

                binding.history = data
                val path ="${context.externalMediaDirs?.get(0)}/수거했어 오늘도!/${data.bm}"

                val handler = android.os.Handler(Looper.getMainLooper())
                handler.postDelayed({
                    Glide.with(context).load(path).into(binding.favoriteIv)
                    binding.historyBtn.setOnClickListener {
                        var colorString = "#000000"
                        var state = "false"
                        if(list?.get(position)?.favorite == "false"){
                            colorString = "#ff0000"
                            state = "true"
                        }
                        (it as ImageView).setColorFilter(Color.parseColor(colorString), PorterDuff.Mode.SRC_ATOP)
                        helper?.databaseDao()?.updateFavorite(position+1,state)
                    }
                },0)

                if(list?.get(position)?.favorite == "true"){
                    binding.historyBtn.setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.SRC_ATOP)
                }

                binding.historyUnitLayout.setOnClickListener{
                    HistoryViewModel.selected.value = position
                }
            }
        }
    }
}