package com.kt.recycleapp.kt.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.kt.recycleapp.kt.viewmodel.HistoryViewModel
import java.recycleapp.R
import java.recycleapp.databinding.HistoryFragmentBinding

class HistoryFragment : Fragment() {

    lateinit var binding:HistoryFragmentBinding
    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.history_fragment, container, false)

        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        return binding.root
    }


}