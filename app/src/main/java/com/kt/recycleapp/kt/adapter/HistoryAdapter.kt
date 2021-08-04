package com.kt.recycleapp.kt.adapter

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.kt.recycleapp.kt.etc.HistoryData
import com.kt.recycleapp.kt.viewmodel.HistoryViewModel
import com.kt.recycleapp.manager.MyPreferenceManager
import com.kt.recycleapp.model.MyRoomDatabase
import com.kt.recycleapp.model.RoomHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.recycleapp.databinding.HistoryLayoutUnitBinding

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.MyHolder>() {
    var items = ArrayList<HistoryData>()
    lateinit var prefs :MyPreferenceManager
    var helper:RoomHelper? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = HistoryLayoutUnitBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        prefs = MyPreferenceManager(parent.context)
        helper = Room.databaseBuilder(parent.context,RoomHelper::class.java,"Database").build()
        val holder = MyHolder(binding)
        return holder
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MyHolder(private val binding: HistoryLayoutUnitBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: HistoryData, position: Int) {
            binding.history = data
            binding.historyUnitLayout.setOnClickListener{
                Log.d(position.toString(),"것")
                HistoryViewModel.selected.value = position
            }

            binding.historyBtn.setOnClickListener {
                (it as ImageView).setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.SRC_ATOP)
                GlobalScope.launch {
                    helper?.databaseDao()?.updateFavorite(position+1)
                }



            }
        }
    }
}