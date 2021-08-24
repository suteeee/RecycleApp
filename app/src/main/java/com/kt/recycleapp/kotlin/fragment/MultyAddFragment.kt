package com.kt.recycleapp.kotlin.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.kt.recycleapp.kotlin.activity.MainActivity
import com.kt.recycleapp.kotlin.activity.OnBackPressListener
import com.kt.recycleapp.kotlin.adapter.AddAdapter
import com.kt.recycleapp.kotlin.viewmodel.AddViewModel
import com.kt.recycleapp.model.DatabaseReadModel
import java.recycleapp.R
import java.recycleapp.databinding.FragmentMultyAddBinding

class MultyAddFragment : Fragment(), OnBackPressListener {
    lateinit var binding:FragmentMultyAddBinding
    lateinit var viewModel:AddViewModel
    lateinit var mAdapter : AddAdapter
    var ld = MutableLiveData<String>()
    var data = DatabaseReadModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
 
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_multy_add, container, false)
        val arr = data.getProductsList(ld)
        viewModel = ViewModelProvider(this).get(AddViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        mAdapter = AddAdapter()

        ld.observe(viewLifecycleOwner, {
            if(it.equals("finish")){
                binding.multyPb.visibility = View.INVISIBLE
                var cnt = 0
                binding.multyRv.adapter = mAdapter
                binding.itemaddBtn.setOnClickListener {
                    viewModel.itemList.add(cnt++)
                    AddViewModel.products.add("")
                    AddViewModel.addItems.add(HashMap())
                }

                AddViewModel.productList = arr
            }
        })

        AddViewModel.summit.observe(viewLifecycleOwner,{
            if(it == "click"){
               binding.multyPb.visibility = View.VISIBLE
            }
            else{
                binding.multyPb.visibility = View.INVISIBLE
            }
        })

        binding.addAllBtn.setOnClickListener {
            binding.multyPb.visibility = View.VISIBLE
            val db = FirebaseFirestore.getInstance()
            val list = AddViewModel.addItems
                for(i in 0 until list.size){
                    if(i == 0){
                        db.collection("products").document(AddViewModel.products[i])
                            .update(list[i])
                    }
                    else{
                        db.collection("products").document("복합물품").collection("sublist").document(AddViewModel.products[i])
                            .update(list[i])
                    }
                }
                binding.multyPb.visibility = View.INVISIBLE
                Toast.makeText(context,"상품 등록이 완료되었습니다.",Toast.LENGTH_SHORT).show()
            AddViewModel.addItems.clear()
        }
        return binding.root
    }

    override fun onBack() {
        val act = activity as MainActivity?
        act!!.setOnBackPressListener(null)
        act.supportFragmentManager.beginTransaction().replace(R.id.small_layout1, MainFragment()).commit()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as MainActivity).setOnBackPressListener(this)
    }
}