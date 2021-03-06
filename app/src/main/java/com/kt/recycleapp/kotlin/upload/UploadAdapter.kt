package com.kt.recycleapp.kotlin.upload

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kt.recycleapp.kotlin.main.MainActivity
import com.kt.recycleapp.kotlin.alert.AlertFragment
import java.recycleapp.databinding.DataUploadUnitBinding

class UploadAdapter(val viewmodel: DataUploadViewModel, val act: MainActivity) :RecyclerView.Adapter<UploadAdapter.UploadViewHoler>(){
    lateinit var binding: DataUploadUnitBinding
    var items = ArrayList<Int>()
   // var viewmodel :DataUploadViewModel? = null
    lateinit var holder : UploadViewHoler
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadViewHoler {
        binding = DataUploadUnitBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        holder = UploadViewHoler(binding,parent.context)
        return holder
    }

    override fun onBindViewHolder(holder: UploadViewHoler, position: Int) {
       holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class UploadViewHoler(val binding:DataUploadUnitBinding, val context: Context):RecyclerView.ViewHolder(binding.root){
        var selected = ""
        fun onBind(pos: Int) {
            init()
            if(pos != 0){
                binding.uploadBarcodeEt.isEnabled = false
                binding.uploadBarcodeEt.isClickable = false
            }

            val adt = ArrayAdapter(context, R.layout.simple_spinner_dropdown_item, viewmodel.productList)
            binding.uploadKindSp.adapter = adt
            adt.notifyDataSetChanged()

            binding.uploadKindSp.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selected = parent?.getItemAtPosition(position).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })

            binding.uploadSaveBtn.setOnClickListener {
                save(pos)
                AlertFragment.showAlert(act,"SaveSuccess",true)
            }

        }

        fun init() {
            binding.uploadBarcodeEt.setText("")
            binding.uploadInfoEt.setText("")
            binding.uploadNameEt.setText("")
        }

        fun save(i: Int) {
            var name = ""
            var info = binding.uploadInfoEt.text.toString()
            viewmodel.kinds[i] = selected
                name = binding.uploadNameEt.text.toString()
                viewmodel.barcodes[i] = binding.uploadBarcodeEt
                    .text.toString()
                viewmodel.names[i] = name

            if(info.isNotEmpty() && info.isNotBlank()) {
                viewmodel.infoText[i] = info
            }
        }

    }
}