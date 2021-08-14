package com.kt.recycleapp.kt.fragment

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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

class HistoryFragment : Fragment(),OnBackPressListener {

    lateinit var binding:HistoryFragmentBinding
    lateinit var mAdapter:HistoryAdapter
    private lateinit var viewModel: HistoryViewModel
    var helper:RoomHelper? = null
    var model = DatabaseReadModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.history_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        (activity as MainActivity).viewModel.toolbarText.value = "히스토리"

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

            val list = helper?.databaseDao()?.getAll()
            val barcodes = ArrayList<String>()
            list?.forEach {res -> barcodes.add(res.barcode!!) }

            val frg = AnnounceRecyclePageFragment()
            val bundle = Bundle()
            val temp = DatabaseReadModel.name[barcodes[it]]

            bundle.putString("barcode", temp ?: barcodes[it])

            frg.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.small_layout1,frg)?.commit()

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