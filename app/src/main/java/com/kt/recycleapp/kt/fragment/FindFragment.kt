package com.kt.recycleapp.kt.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.kt.recycleapp.kt.activity.MainActivity
import com.kt.recycleapp.kt.activity.OnBackPressListener
import com.kt.recycleapp.kt.adapter.FindBigAdapter
import com.kt.recycleapp.kt.viewmodel.FindViewModel
import java.recycleapp.R
import java.recycleapp.databinding.FragmentFindBinding

class FindFragment : Fragment(),OnBackPressListener{
    companion object{ var click = MutableLiveData<String>()}
    lateinit var binding:FragmentFindBinding
    lateinit var mAdapter : FindBigAdapter
    lateinit var viewModel:FindViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_find, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        (activity as MainActivity).viewModel.toolbarText.value = "찾아보기"

        viewModel = ViewModelProvider(this).get(FindViewModel::class.java)
        binding.bigItem =viewModel

        mAdapter = FindBigAdapter()
        binding.findBigRv.adapter = mAdapter

        viewModel.findBig()
        viewModel.findBigProgress.observe(viewLifecycleOwner,{
            if(it == "finish"){
                binding.findBigPb.visibility = View.INVISIBLE
                for(i in 0 until viewModel.itemData.size){
                    viewModel.addItem(i)
                }
                MainActivity.findBigForSearch = viewModel.itemList
            }
        })

        Log.d((binding.findBigRv.adapter as FindBigAdapter).toString(),mAdapter.toString())


        click.observe(viewLifecycleOwner,{
            if(it == "start"){
                viewClick()
                click.value = "stop"
            }
            Log.d("c3",it.toString())
        })

        (activity as MainActivity).viewModel.searchFlag.observe(viewLifecycleOwner,{
            Log.d("search",(activity as MainActivity).viewModel.searchFlag.value.toString())
            if(it == "finish"){
                Log.d("search","do")
                viewModel.bigFilterList(MainActivity.findBigForSearch)
            }
            if(it == "reset"){
                viewModel.bigResetList()
            }
        })


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.findBigRv.adapter?.notifyDataSetChanged()
    }

    fun viewClick() {
        Log.d("click","click")
        (activity as MainActivity).viewModel.selectedFragment.value = "findsmall"
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.small_layout1,FindSmallFragment())?.commit()
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
}