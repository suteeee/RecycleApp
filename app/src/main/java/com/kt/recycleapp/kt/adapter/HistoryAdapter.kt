package com.kt.recycleapp.kt.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.recycleapp.databinding.FindSmallLayoutUnitBinding
import java.recycleapp.databinding.HistoryLayoutUnitBinding

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.MyHolder>() {


    inner class MyHolder(private val binding: HistoryLayoutUnitBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}