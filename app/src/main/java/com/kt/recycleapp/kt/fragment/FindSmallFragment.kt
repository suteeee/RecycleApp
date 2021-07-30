package com.kt.recycleapp.kt.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kt.recycleapp.kt.activity.MainActivity
import com.kt.recycleapp.kt.activity.OnBackPressListener
import com.kt.recycleapp.kt.adapter.FindSmallAdapter
import com.kt.recycleapp.kt.viewmodel.FindFragmentViewModel
import java.recycleapp.R
import java.recycleapp.databinding.FragmentFindSmallBinding

class FindSmallFragment : Fragment(), OnBackPressListener {
    lateinit var binding:FragmentFindSmallBinding
    lateinit var viewModel: FindFragmentViewModel
    lateinit var mAdapter: FindSmallAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_find_small, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel = ViewModelProvider(this).get(FindFragmentViewModel::class.java)
        viewModel.findSmall()
        binding.smallItem = viewModel

        mAdapter = FindSmallAdapter()
        binding.findSmallRv.adapter = mAdapter


        viewModel.findSmallProgress.observe(viewLifecycleOwner,{
            if(it == "finish"){
                binding.findSmallPb.visibility = View.INVISIBLE
                var idx = 0
                when(FindFragmentViewModel.selectDoc){
                    "종이"->idx = 0
                    "플라스틱"->idx = 1
                }
                viewModel.itemDataSmall[idx].forEach {
                    Log.d("check",it.toString())
                }
                for(i in 0 until viewModel.itemDataSmall[idx].size){
                    viewModel.addSmallItem(i,idx)
                }
            }
        })

        return binding.root
    }

    override fun onBack() {
        val act = activity as MainActivity
        act.setOnBackPressListener(null)
        act.supportFragmentManager.beginTransaction().replace(R.id.small_layout1,FindFragment()).commit()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val act = activity as MainActivity
        act.setOnBackPressListener(this)
    }
}