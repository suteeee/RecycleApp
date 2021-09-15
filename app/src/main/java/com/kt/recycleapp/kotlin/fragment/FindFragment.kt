package com.kt.recycleapp.kotlin.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.kt.recycleapp.kotlin.activity.MainActivity
import com.kt.recycleapp.kotlin.adapter.FindBigAdapter
import com.kt.recycleapp.kotlin.viewmodel.FindViewModel
import java.recycleapp.R
import java.recycleapp.databinding.FragmentFindBinding

class FindFragment : Fragment(){
    companion object{ var click = MutableLiveData<String>()}
    lateinit var binding:FragmentFindBinding
    lateinit var mAdapter : FindBigAdapter
    lateinit var viewModel:FindViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_find, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val act = activity as MainActivity
        //act.viewModel.toolbarText.value = "찾아보기"

        viewModel = ViewModelProvider(this).get(FindViewModel::class.java)
        binding.bigItem =viewModel
        viewModel.itemList.clear()
        viewModel.smallItemList.clear()

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

        click.observe(viewLifecycleOwner,{
            if(it == "start"){
                viewClick()
                click.value = "stop"
            }
        })

        (activity as MainActivity).viewModel.searchFlag.observe(viewLifecycleOwner,{
            if(it == "finish"){
                viewModel.bigFilterList(MainActivity.findBigForSearch)
            }
            if(it == "reset"){
                viewModel.bigResetList()
            }
        })


        return binding.root
    }


    fun viewClick() {
        (activity as MainActivity).viewModel.selectedFragment.value = "findsmall"
        activity?.supportFragmentManager?.beginTransaction()?.add(R.id.small_layout1,FindSmallFragment())?.addToBackStack(null)?.commit()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val act = activity as MainActivity
        act.viewModel.selectedFragment.value = "find"
        val v = act.viewModel
        v.fragmentStack.push("find")
    }

    override fun onDetach() {
        super.onDetach()
        val act = activity as MainActivity
        val v = act.viewModel
        v.fragmentStack.pop()
        v.selectedFragment.value = v.fragmentStack.peek()
        if(v.fragmentStack.peek() == "main") act.supportFragmentManager.beginTransaction().replace(R.id.small_layout1,MainFragment()).commit()
    }
}