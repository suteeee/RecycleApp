package com.kt.recycleapp.kotlin.find

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.recycleapp.databinding.FindBigLayoutUnitBinding

class FindBigAdapter : RecyclerView.Adapter<FindBigAdapter.MyViewHolder>() {
    var items = ArrayList<FindBigData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FindBigLayoutUnitBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val holder = MyViewHolder(binding,parent.context)
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MyViewHolder(
        private val binding: FindBigLayoutUnitBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: FindBigData){
            binding.big = data
            binding.findBigTv.setOnClickListener {
                FindFragment.click.value = "start"
                FindViewModel.selectDoc = binding.findBigTv.text.toString()
            }
        }
    }
}