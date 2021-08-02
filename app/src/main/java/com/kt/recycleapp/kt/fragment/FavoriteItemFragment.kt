package com.kt.recycleapp.kt.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.kt.recycleapp.kt.activity.MainActivity
import com.kt.recycleapp.kt.activity.OnBackPressListener
import com.kt.recycleapp.kt.adapter.FavoriteAdapter
import com.kt.recycleapp.kt.viewmodel.FavoriteItemFragmentViewModel
import com.kt.recycleapp.manager.MyPreferenceManager
import com.kt.recycleapp.model.RoomHelper
import java.recycleapp.R
import java.recycleapp.databinding.FragmentFavoriteItemBinding

class FavoriteItemFragment : Fragment(),OnBackPressListener {

    lateinit var binding:FragmentFavoriteItemBinding
    lateinit var viewModel : FavoriteItemFragmentViewModel
    lateinit var mAdapter : FavoriteAdapter
    lateinit var prefs:MyPreferenceManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favorite_item, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        val helper = Room.databaseBuilder(requireContext(), RoomHelper::class.java,"Database").allowMainThreadQueries().build()

        viewModel = ViewModelProvider(this).get(FavoriteItemFragmentViewModel::class.java)
        binding.favorite = viewModel
        prefs = MyPreferenceManager(requireContext())
        viewModel.setData(activity,helper)

        mAdapter = FavoriteAdapter()
        binding.favoriteRv.adapter = mAdapter

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