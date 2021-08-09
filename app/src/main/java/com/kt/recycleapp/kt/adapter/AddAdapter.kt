package com.kt.recycleapp.kt.adapter

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
import com.google.firebase.firestore.FirebaseFirestore
import com.kt.recycleapp.kt.viewmodel.AddViewModel
import java.recycleapp.databinding.AddpageItemUnitBinding
import java.util.HashMap

class AddAdapter : RecyclerView.Adapter<AddAdapter.AddViewHoler>() {
    var items = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddViewHoler {
        val binding = AddpageItemUnitBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val holder = AddViewHoler(parent.context,binding)
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
            val adt = ArrayAdapter(context, R.layout.simple_spinner_dropdown_item,AddViewModel.productList);
            binding.productsSp2.adapter = adt
            adt.notifyDataSetChanged()
            binding.productsSp2.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    AddViewModel.products[pos] = binding.productsSp2.getItemAtPosition(position).toString()
                    Log.d("것것", AddViewModel.products[pos]);
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            })

            binding.summitBtn.setOnClickListener {
                if(pos == 0){
                    if(binding.nameEt.text.isNotBlank()){
                        tmpProduct.put(AddViewModel.barcode,binding.nameEt.text.toString())
                        AddViewModel.addItems[pos] = tmpProduct
                        Toast.makeText(context,"저장 완료!.",Toast.LENGTH_SHORT).show()
                        AddViewModel.addItems.forEach {
                            Log.d("것것",it.toString())
                        }
                    }
                    else{ Toast.makeText(context,"빈칸으로 저장할 수 없습니다.",Toast.LENGTH_SHORT).show() }
                }

                else{
                    if(binding.nameEt.text.isNotBlank()){
                        tmpProduct.put(AddViewModel.addItems[0][AddViewModel.barcode].toString() + "_${pos}",binding.nameEt.text.toString())
                        AddViewModel.addItems[pos] = tmpProduct
                        Toast.makeText(context,"저장 완료!.",Toast.LENGTH_SHORT).show()
                        AddViewModel.addItems.forEach {
                            Log.d("것것",it.toString())
                        }
                    }
                    else{ Toast.makeText(context,"빈칸으로 저장할 수 없습니다.",Toast.LENGTH_SHORT).show() }
                }

            }
        }
    }
}