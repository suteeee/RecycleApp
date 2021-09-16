package com.kt.recycleapp.kotlin.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kt.recycleapp.kotlin.etc.FavoriteData
import com.kt.recycleapp.kotlin.viewmodel.FavoriteViewModel
import com.kt.recycleapp.model.MyRoomDatabase
import com.kt.recycleapp.model.RoomHelper
import com.kt.recycleapp.model.RoomHelper.Companion.getInstance
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.recycleapp.databinding.FavoriteLayoutUnitBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHoler>() {
    var items = ArrayList<FavoriteData>()
    var helper: RoomHelper? = null
    var list:List<MyRoomDatabase>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHoler {
        var binding = FavoriteLayoutUnitBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val holder = FavoriteViewHoler(binding,parent.context)
        helper = getInstance(parent.context)

        list = helper?.databaseDao()?.getAll()

        return holder
    }

    override fun onBindViewHolder(holder: FavoriteViewHoler, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    inner class FavoriteViewHoler(private val binding: FavoriteLayoutUnitBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FavoriteData, position: Int) {
            binding.data = data

            val path ="${context.externalMediaDirs?.get(0)}/수거했어 오늘도!/${data.image}"
            val handler = android.os.Handler(Looper.getMainLooper())

            handler.postDelayed({
                Glide.with(context).load(path).into(binding.historyIv)
                binding.favoriteBtn.setOnClickListener {
                    GlobalScope.launch {
                        helper?.databaseDao()?.updateFavorite(position+1,"false")
                        items.removeAt(position)
                    }
                }
            },0)

            binding.favoriteBtn.setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.SRC_ATOP)

            binding.favoriteunitLayout.setOnClickListener {
                FavoriteViewModel.selected.value = data.pos
            }

        }
    }
}