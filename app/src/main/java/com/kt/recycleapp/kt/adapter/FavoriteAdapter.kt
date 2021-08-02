package com.kt.recycleapp.kt.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kt.recycleapp.kt.etc.FavoriteData
import java.recycleapp.databinding.FavoriteLayoutUnitBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHoler>() {
    var items = ArrayList<FavoriteData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHoler {
        var binding = FavoriteLayoutUnitBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val holder = FavoriteViewHoler(binding)
        return holder
    }

    override fun onBindViewHolder(holder: FavoriteViewHoler, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
    inner class FavoriteViewHoler(private val binding: FavoriteLayoutUnitBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FavoriteData) {
            binding.data = data
            binding.favoriteBtn.setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.SRC_ATOP)
        }
    }
}