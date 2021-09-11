package com.kt.recycleapp.kotlin.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.kt.recycleapp.java.announce.AnnounceRecyclerFragment
import com.kt.recycleapp.kotlin.activity.MainActivity
import com.kt.recycleapp.kotlin.adapter.FavoriteAdapter
import com.kt.recycleapp.kotlin.viewmodel.FavoriteViewModel
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
    var helper:RoomHelper? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("fff","ffff")
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favorite_item, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val act = activity as MainActivity
        act.viewModel.toolbarText.value = "즐겨찾기"


        helper = Room.databaseBuilder(requireContext(), RoomHelper::class.java,"Database").allowMainThreadQueries().build()

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        binding.favorite = viewModel
        prefs = MyPreferenceManager(requireContext())
        viewModel.itemList.clear()
        viewModel.getFireData()

        viewModel.getProductName.observe(viewLifecycleOwner,{
            if(it == "finish"){
                binding.favoritePb.visibility = View.INVISIBLE
                viewModel.setData(helper!!)
            }
        })

        mAdapter = FavoriteAdapter()
        binding.favoriteRv.adapter = mAdapter

        FavoriteViewModel.selected.observe(viewLifecycleOwner,{
            if(it > -1){
                val list = helper?.databaseDao()?.getFavoriteAll()
                val barcodes = ArrayList<String>()

                list?.forEach {res->
                    barcodes.add(res.barcode!!)
                    Log.d("favoriteItem ${res.no} $it",res.barcode!!)
                }
                val frg = AnnounceRecyclerFragment()
                val bundle = Bundle()
                val temp = DatabaseReadModel.name[barcodes[it]]
                bundle.putString("barcode", temp ?: barcodes[it])
                frg.arguments = bundle
                FavoriteViewModel.selected.value = -1
                activity?.supportFragmentManager?.beginTransaction()?.add(R.id.small_layout1,frg)?.addToBackStack(null)?.commit()
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
        val act = activity as MainActivity
        act.viewModel.selectedFragment.value = "favorite"
        val v = act.viewModel
        v.fragmentStack.push("favorite")
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