package com.kt.recycleapp.kotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kt.recycleapp.kotlin.etc.FindSmallData
import com.kt.recycleapp.kotlin.fragment.FindSmallFragment
import com.kt.recycleapp.kotlin.viewmodel.FindViewModel
import java.recycleapp.databinding.FindSmallLayoutUnitBinding

class FindSmallAdapter : RecyclerView.Adapter<FindSmallAdapter.SmallAdapterHolder>() {
    var items = ArrayList<FindSmallData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallAdapterHolder {
        val binding = FindSmallLayoutUnitBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val holder = SmallAdapterHolder(binding)
        return holder
    }

    override fun onBindViewHolder(holder: SmallAdapterHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class SmallAdapterHolder(private val binding: FindSmallLayoutUnitBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data:FindSmallData) {
            binding.small = data
            binding.findSmallTv.setOnClickListener {
                FindViewModel.selectItem = binding.findSmallTv.text.toString()
                FindSmallFragment.smallClick.value = "start"
            }
        }
    }
}