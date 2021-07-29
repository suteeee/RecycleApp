package com.kt.recycleapp.kt.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kt.recycleapp.kt.etc.FindBigData
import com.kt.recycleapp.kt.activity.MainActivity
import com.kt.recycleapp.kt.activity.OnBackPressListener
import com.kt.recycleapp.kt.adapter.FindBigAdapter
import com.kt.recycleapp.kt.viewmodel.FindFragmentViewModel
import java.recycleapp.R
import java.recycleapp.databinding.FragmentFindBinding

class FindFragment : Fragment(),OnBackPressListener{
    lateinit var binding:FragmentFindBinding
    lateinit var mAdapter : FindBigAdapter
    lateinit var viewModel:FindFragmentViewModel
    val imgArr = arrayOf(R.drawable.paper, R.drawable.plastic, R.drawable.vinyl, R.drawable.iron, R.drawable.delivery, R.drawable.constore, R.drawable.baterry, R.drawable.trash, R.drawable.pet)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_find, container, false)
        viewModel = ViewModelProvider(this).get(FindFragmentViewModel::class.java)
        viewModel.findBig()
        mAdapter = FindBigAdapter()
        binding.findBigRv.layoutManager = LinearLayoutManager(binding.root.context)
        binding.bigItem =viewModel

        viewModel.findBigProgress.observe(viewLifecycleOwner,{
            if(it == "finish"){
                for(i in 0 until viewModel.itemData.size){
                    viewModel.addItem(i)
                }

            }
        })
        binding.findBigRv.adapter = mAdapter


        return binding.root

    }

    override fun onBack() {
        val act = activity as MainActivity
        act.setOnBackPressListener(null)
        act.supportFragmentManager.beginTransaction().replace(R.id.small_layout1,MainFragment()).commit()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val act = activity as MainActivity
        act.setOnBackPressListener(this)
    }

   /* fun addItem(id:Int, text:String) {
        val data = FindBigData(id, text)
        viewModel.itemList.add(data)
    }*/
}