package com.kt.recycleapp.kt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kt.recycleapp.kt.etc.FindBigData
import com.kt.recycleapp.kt.viewmodel.FindFragmentViewModel
import kotlinx.android.synthetic.main.recycler_layout_unit.view.*
import java.recycleapp.R
import java.recycleapp.databinding.FindBigLayoutUnitBinding
import java.recycleapp.databinding.RecyclerLayoutUnitBinding


class FindBigAdapter: RecyclerView.Adapter<FindBigAdapter.MyViewHolder>() {
    var items = ArrayList<FindBigData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FindBigLayoutUnitBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val holder = MyViewHolder(binding)
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class MyViewHolder(private val binding:FindBigLayoutUnitBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data:FindBigData){
            binding.big = data
        }
    }
}