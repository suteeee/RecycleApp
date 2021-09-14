package com.kt.recycleapp.kotlin.adapter

import android.R
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kt.recycleapp.kotlin.viewmodel.AddViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.recycleapp.databinding.AddpageItemUnitBinding
import java.util.HashMap

class AddAdapter(val viewModel: AddViewModel) : RecyclerView.Adapter<AddAdapter.AddViewHoler>() {
    var items = ArrayList<Int>()
    lateinit var holder :AddViewHoler

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddViewHoler {
        val binding = AddpageItemUnitBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        holder = AddViewHoler(parent.context,binding)
        return holder
    }

    override fun onBindViewHolder(holder: AddViewHoler, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class AddViewHoler (var context: Context, val binding: AddpageItemUnitBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(pos: Int) {
            val tmpProduct= HashMap<String,Any>()
            val adt = ArrayAdapter(context, R.layout.simple_spinner_dropdown_item,viewModel.productList);
            binding.productsSp2.adapter = adt
            adt.notifyDataSetChanged()
            binding.productsSp2.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    AddViewModel.products[pos] = binding.productsSp2.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            })

            binding.summitBtn.setOnClickListener {
                if(pos == 0){
                    if(binding.nameEt.text.isNotBlank()){
                        tmpProduct.put(AddViewModel.barcode,binding.nameEt.text.toString())
                        AddViewModel.addItems[pos] = tmpProduct
                        Toast.makeText(context,"저장 완료!",Toast.LENGTH_SHORT).show()
                    }
                    else{ Toast.makeText(context,"빈칸으로 저장할 수 없습니다.",Toast.LENGTH_SHORT).show() }
                }

                else{
                    if(binding.nameEt.text.isNotBlank()){
                        tmpProduct.put(AddViewModel.addItems[0][AddViewModel.barcode].toString() + "_${pos}",binding.nameEt.text.toString())
                        AddViewModel.addItems[pos] = tmpProduct
                        Toast.makeText(context,"저장 완료!",Toast.LENGTH_SHORT).show()
                    }
                    else{ Toast.makeText(context,"빈칸으로 저장할 수 없습니다.",Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }
}