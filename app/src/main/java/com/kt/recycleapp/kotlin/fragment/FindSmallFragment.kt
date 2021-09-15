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
import com.kt.recycleapp.java.announce.AnnounceRecyclerFragment
import com.kt.recycleapp.kotlin.activity.MainActivity
import com.kt.recycleapp.kotlin.adapter.FindSmallAdapter
import com.kt.recycleapp.kotlin.viewmodel.FindViewModel
import java.recycleapp.R
import java.recycleapp.databinding.FragmentFindSmallBinding

class FindSmallFragment : Fragment() {
    companion object{
        var smallClick = MutableLiveData<String>()

    }
    lateinit var binding:FragmentFindSmallBinding
    lateinit var viewModel: FindViewModel
    lateinit var mAdapter: FindSmallAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_find_small, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val act = activity as MainActivity
       // act.viewModel.toolbarText.value = "찾아보기"

        viewModel = ViewModelProvider(this).get(FindViewModel::class.java)
        viewModel.findSmall()
        binding.smallItem = viewModel

        mAdapter = FindSmallAdapter()
        binding.findSmallRv.adapter = mAdapter


        viewModel.findSmallProgress.observe(viewLifecycleOwner,{
            if(it == "finish"){
                binding.findSmallPb.visibility = View.INVISIBLE
                viewModel.addSmallItem()
                MainActivity.findSmallForSearch = viewModel.smallItemList
            }
        })

        smallClick.observe(viewLifecycleOwner,{
            if(it == "start"){
                viewClick()
            }
        })


        (activity as MainActivity).viewModel.searchFlag.observe(viewLifecycleOwner,{
            if(it == "finish"){
                viewModel.smallFilterList(MainActivity.findSmallForSearch)
            }
            if(it == "reset"){
                viewModel.smallResetList()
            }
        })

        return binding.root
    }

    fun viewClick() {
        val frg = AnnounceRecyclerFragment()
        val bundle = Bundle()
        bundle.putString("barcode",FindViewModel.selectItem)

        frg.arguments = bundle

        activity?.supportFragmentManager?.beginTransaction()?.add(R.id.small_layout1,frg)?.addToBackStack(null)?.commit()
        smallClick.value = "stop"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val act = activity as MainActivity
        act.viewModel.selectedFragment.value = "findsmall"
        val v = act.viewModel
        v.fragmentStack.push("findsmall")
    }

    override fun onDetach() {
        super.onDetach()
        val act = activity as MainActivity
        val v = act.viewModel
        v.fragmentStack.pop()
        v.selectedFragment.value = v.fragmentStack.peek()
    }
}