package com.kt.recycleapp.kotlin.find

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
import com.kt.recycleapp.kotlin.Internet
import com.kt.recycleapp.kotlin.alert.AlertFragment
import com.kt.recycleapp.kotlin.main.MainActivity
import com.kt.recycleapp.kotlin.main.MainFragment
import com.kt.recycleapp.kotlin.main.MainViewModel
import com.kt.recycleapp.manager.MyPreferenceManager
import java.recycleapp.R
import java.recycleapp.databinding.FragmentFindBinding

class FindFragment : Fragment(){
    companion object{ var click = MutableLiveData<String>()}
    lateinit var binding:FragmentFindBinding
    lateinit var mAdapter : FindBigAdapter
    lateinit var viewModel: FindViewModel
    lateinit var act: MainActivity
    lateinit var actViewModel: MainViewModel
    lateinit var prefs :MyPreferenceManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_find, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        act = activity as MainActivity
        prefs = MyPreferenceManager(requireContext())

        viewModel = ViewModelProvider(this).get(FindViewModel::class.java)
        binding.bigItem =viewModel
        viewModel.itemList.clear()
        viewModel.smallItemList.clear()

        mAdapter = FindBigAdapter()
        binding.findBigRv.adapter = mAdapter

        val iStatus = Internet.getStatus(requireContext())

        when(iStatus) {
            Internet.MOBILE_DATA -> {
                Log.d("find","${prefs.mobileInternetShow}")
                if(!(prefs.mobileInternetShow)) {
                    act.showToast(resources.getString(R.string.mobileData))
                    prefs.mobileInternetShow = true
                }

            }
            Internet.NOT_CONNECT -> {
                act.showToast(resources.getString(R.string.internetNotConnected))
            }
        }

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

        act.viewModel.searchFlag.observe(viewLifecycleOwner,{
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
        act.viewModel.selectedFragment.value = "findsmall"
        act.replaceFragmentWithBackStack(FindSmallFragment(),null)
        //act.supportFragmentManager.beginTransaction().add(R.id.small_layout1,FindSmallFragment()).addToBackStack(null).commit()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val act = activity as MainActivity
        actViewModel = act.viewModel
        actViewModel.selectedFragment.value = "find"
        actViewModel.fragmentStack.push("find")
    }

    override fun onDetach() {
        super.onDetach()
        actViewModel.fragmentStack.pop()
        actViewModel.selectedFragment.value = actViewModel.fragmentStack.peek()
        if(actViewModel.fragmentStack.peek() == "main")
            act.replaceFragment(MainFragment())
           //act.supportFragmentManager.beginTransaction().replace(R.id.small_layout1,MainFragment()).commit()
    }
}