package com.kt.recycleapp.kotlin.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.kt.recycleapp.kotlin.etc.HistoryData
import com.kt.recycleapp.kotlin.viewmodel.HistoryViewModel
import com.kt.recycleapp.manager.MyPreferenceManager
import com.kt.recycleapp.model.MyRoomDatabase
import com.kt.recycleapp.model.RoomHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.recycleapp.databinding.HistoryLayoutUnitBinding

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.MyHolder>() {
    var items = ArrayList<HistoryData>()
    lateinit var prefs :MyPreferenceManager
    var helper:RoomHelper? = null
    var list:List<MyRoomDatabase>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = HistoryLayoutUnitBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        prefs = MyPreferenceManager(parent.context)
        helper = RoomHelper.getInstance(parent.context)

        CoroutineScope(Dispatchers.IO).launch {
            list = helper?.databaseDao()?.getAll()
        }

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
            CoroutineScope(Dispatchers.Main).launch {

                binding.history = data
                val path ="${context.externalMediaDirs?.get(0)}/수거했어 오늘도!/${data.bm}"

                val handler = android.os.Handler(Looper.getMainLooper())
                handler.postDelayed({
                    Glide.with(context).load(path).into(binding.historyIv)
                    binding.historyBtn.setOnClickListener {
                        var colorString = "#000000"
                        var state = "false"
                        if(list?.get(position)?.favorite == "false"){
                            colorString = "#ff0000"
                            state = "true"
                        }
                        var check = 0
                        list?.forEach { if(list?.get(position)?.favorite == "true"){ check += 1 } }

                        Log.d("check",check.toString())
                        if(check < 10){
                            (it as ImageView).setColorFilter(Color.parseColor(colorString), PorterDuff.Mode.SRC_ATOP)

                            CoroutineScope(Dispatchers.IO).launch {
                                Log.d("${position} ${state}","test")
                                helper?.databaseDao()?.updateFavorite(position,state)
                            }

                        }
                        else{
                            Toast.makeText(context,"즐겨찾기는 최대 10개까지 등록 가능합니다.",Toast.LENGTH_SHORT).show()
                        }

                    }
                },0)

                try {
                    if (list?.get(position)?.favorite == "true") {
                        binding.historyBtn.setColorFilter(
                            Color.parseColor("#ff0000"),
                            PorterDuff.Mode.SRC_ATOP
                        )
                    }
                }catch (e:Exception){}

                binding.historyUnitLayout.setOnClickListener{
                    HistoryViewModel.selected.value = data.pos
                }
            }
        }
    }
}