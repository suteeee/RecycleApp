package com.kt.recycleapp.kotlin.upload

import android.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kt.recycleapp.kotlin.main.MainActivity
import com.kt.recycleapp.kotlin.alert.AlertFragment
import java.recycleapp.databinding.AddpageItemUnitBinding
import java.util.HashMap

class AddAdapter(val viewModel: AddViewModel, val act: MainActivity) : RecyclerView.Adapter<AddAdapter.AddViewHoler>() {
    var items = ArrayList<Int>()
    lateinit var holder : AddViewHoler

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
            init()
            val tmpProduct= HashMap<String,Any>()
            val tmpInfoText = ArrayList<String>()
            val adt = ArrayAdapter(context, R.layout.simple_spinner_dropdown_item,viewModel.productList)
            binding.productsSp2.adapter = adt
            adt.notifyDataSetChanged()
            binding.productsSp2.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    AddViewModel.kinds[pos] = binding.productsSp2.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            })

            binding.summitBtn.setOnClickListener {
                if(pos == 0){
                    if(binding.nameEt.text.isNotBlank()){
                        tmpProduct.put(AddViewModel.barcode,binding.nameEt.text.toString())
                        AddViewModel.addItems[pos] = tmpProduct

                        if(binding.productExplainEt.text.toString().isNotBlank()){
                            tmpInfoText.add(binding.productExplainEt.text.toString())
                            AddViewModel.infoText[pos] = binding.productExplainEt.text.toString()
                        }
                        else {
                            //tmpInfoText.put(binding.nameEt.text.toString(),binding.productExplainEt.text.toString())
                            AddViewModel.infoText[pos] = ""
                        }
                        AlertFragment.showAlert(act,"SaveSuccess",true)
                    }
                    else{ AlertFragment.showAlert(act,"NotEmptyEditText",true) }
                }

                else{
                    if(binding.nameEt.text.isNotBlank()){
                        val name = AddViewModel.addItems[0][AddViewModel.barcode].toString() + "_${pos}"
                        tmpProduct.put(name, binding.nameEt.text.toString())
                        AddViewModel.addItems[pos] = tmpProduct
                        if(binding.productExplainEt.text.toString().isNotBlank()){
                            //tmpInfoText.put(name,binding.productExplainEt.text.toString())
                            AddViewModel.infoText[pos] = binding.productExplainEt.text.toString()
                        }
                        AlertFragment.showAlert(act,"SaveSuccess",true)
                        //Toast.makeText(context,"저장 완료!",Toast.LENGTH_SHORT).show()
                    }
                    else{ AlertFragment.showAlert(act,"NotEmptyEditText",true) }
                }
            }
        }

        fun init() {
            binding.productExplainEt.setText("")
            binding.nameEt.setText("")
        }
    }
}