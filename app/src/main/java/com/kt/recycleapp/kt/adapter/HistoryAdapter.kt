package com.kt.recycleapp.kt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kt.recycleapp.kt.etc.FindSmallData
import com.kt.recycleapp.kt.etc.HistoryData
import com.kt.recycleapp.kt.fragment.FindSmallFragment
import com.kt.recycleapp.kt.viewmodel.FindFragmentViewModel
import java.recycleapp.databinding.FindSmallLayoutUnitBinding
import java.recycleapp.databinding.HistoryLayoutUnitBinding

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.MyHolder>() {
    var items = ArrayList<HistoryData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = HistoryLayoutUnitBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val holder = MyHolder(binding)
        return holder
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MyHolder(private val binding: HistoryLayoutUnitBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data:HistoryData) {
            binding.history = data
            binding.historyUnitLayout.setOnClickListener{

            }
        }
    }
}