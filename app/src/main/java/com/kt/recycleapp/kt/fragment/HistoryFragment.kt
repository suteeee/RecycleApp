package com.kt.recycleapp.kt.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.kt.recycleapp.java.fragment.AnnounceRecyclePageFragment
import com.kt.recycleapp.kt.activity.MainActivity
import com.kt.recycleapp.kt.activity.OnBackPressListener
import com.kt.recycleapp.kt.adapter.HistoryAdapter
import com.kt.recycleapp.kt.viewmodel.HistoryViewModel
import com.kt.recycleapp.manager.MyPreferenceManager
import com.kt.recycleapp.model.DatabaseReadModel
import com.kt.recycleapp.model.RoomHelper
import java.recycleapp.R
import java.recycleapp.databinding.HistoryFragmentBinding

class HistoryFragment : Fragment(){

    lateinit var binding:HistoryFragmentBinding
    lateinit var mAdapter:HistoryAdapter
    private lateinit var viewModel: HistoryViewModel
    var helper:RoomHelper? = null
    var model = DatabaseReadModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.history_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val act = activity as MainActivity
        act.viewModel.toolbarText.value = "히스토리"

        //rootView는 액티비티를 나타냄 (container는 우리끼리 mainactivity레이아웃을 의미하는 것으로 약속)
        //아래 코드는 액티비티 자체를 가져오는 것이다
        Log.d("historyFragment", (activity as MainActivity?)!!.viewModel.selectedFragment.value!!)

        var prefs = MyPreferenceManager(requireContext())

        helper = Room.databaseBuilder(requireContext(), RoomHelper::class.java,"Database").allowMainThreadQueries().build()

        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        binding.model = viewModel
        viewModel.getFireData()

        viewModel.getProductName.observe(viewLifecycleOwner,{
            if(it == "finish"){
                binding.historyPb.visibility = View.INVISIBLE
                viewModel.getData(helper, prefs, activity as MainActivity)
            }
        })
        HistoryViewModel.selected.observe(viewLifecycleOwner,{
            if(it > -1){
                val list = helper?.databaseDao()?.getAll()
                val barcodes = ArrayList<String>()
                list?.forEach {res -> barcodes.add(res.barcode!!) }

                val frg = AnnounceRecyclePageFragment()
                val bundle = Bundle()
                val temp = DatabaseReadModel.name[barcodes[it]]

                bundle.putString("barcode", temp ?: barcodes[it])

                frg.arguments = bundle
                HistoryViewModel.selected.value = -1
                activity?.supportFragmentManager?.beginTransaction()?.add(R.id.small_layout1,frg)?.addToBackStack(null)?.commit()

            }
        })


        mAdapter = HistoryAdapter()
        binding.historyRv.adapter = mAdapter

        (activity as MainActivity).viewModel.searchFlag.observe(viewLifecycleOwner,{
            if(it == "finish"){
                viewModel.filterList(MainActivity.historyItemsForSearch)
            }
            if(it == "reset"){
                viewModel.resetList()
            }
        })

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val act = activity as MainActivity
        act.viewModel.selectedFragment.value = "history"
        val v = act.viewModel
        v.fragmentStack.push("history")
    }

    override fun onDetach() {
        super.onDetach()
        val act = activity as MainActivity
        val v = act.viewModel
        v.fragmentStack.pop()
        v.selectedFragment.value = v.fragmentStack.peek()
    }
}