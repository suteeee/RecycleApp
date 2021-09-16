package com.kt.recycleapp.kotlin.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.kt.recycleapp.java.announce.AnnounceRecyclerFragment
import com.kt.recycleapp.kotlin.activity.MainActivity
import com.kt.recycleapp.kotlin.adapter.FavoriteAdapter
import com.kt.recycleapp.kotlin.viewmodel.FavoriteViewModel
import com.kt.recycleapp.kotlin.viewmodel.MainViewModel
import com.kt.recycleapp.manager.MyPreferenceManager
import com.kt.recycleapp.model.DatabaseReadModel
import com.kt.recycleapp.model.RoomHelper
import java.recycleapp.R
import java.recycleapp.databinding.FragmentFavoriteItemBinding

class FavoriteItemFragment : Fragment() {

    lateinit var binding:FragmentFavoriteItemBinding
    lateinit var viewModel : FavoriteViewModel
    lateinit var mAdapter : FavoriteAdapter
    lateinit var prefs:MyPreferenceManager
    lateinit var progressBar: ProgressBar
    lateinit var act:MainActivity
    lateinit var actViewModel:MainViewModel
    var helper:RoomHelper? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("fff","ffff")
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favorite_item, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
       // act = activity as MainActivity

        helper = RoomHelper.getInstance(requireContext())

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        binding.favorite = viewModel
        prefs = MyPreferenceManager(requireContext())
        viewModel.itemList.clear()
        viewModel.getFireData()
        progressBar = binding.favoritePb

        viewModel.getProductName.observe(viewLifecycleOwner,{
            if(it == "finish"){
                progressBar.visibility = View.INVISIBLE
                viewModel.setData(helper!!)
            }
        })

        mAdapter = FavoriteAdapter()
        binding.favoriteRv.adapter = mAdapter

        FavoriteViewModel.selected.observe(viewLifecycleOwner,{
            if(it > -1){
                val list = helper?.databaseDao()?.getFavoriteAll()
                val barcodes = ArrayList<String>()

                list?.forEach {res-> barcodes.add(res.barcode!!) }
                val frg = AnnounceRecyclerFragment()
                val bundle = Bundle()
                val temp = DatabaseReadModel.name[barcodes[it]]
                bundle.putString("barcode", temp ?: barcodes[it])
                frg.arguments = bundle
                FavoriteViewModel.selected.value = -1

                act.replaceFragmentWithBackStack(frg,null)
                //activity?.supportFragmentManager?.beginTransaction()?.add(R.id.small_layout1,frg)?.addToBackStack(null)?.commit()
            }

        })

        (activity as MainActivity).viewModel.searchFlag.observe(viewLifecycleOwner,{
            if(it == "finish"){
                viewModel.filterList(MainActivity.favoriteItemForSearch)
            }
            if(it == "reset"){
                viewModel.resetList()
            }
        })

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        act = activity as MainActivity
        actViewModel = act.viewModel
        actViewModel.selectedFragment.value = "favorite"
        actViewModel.fragmentStack.push("favorite")
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