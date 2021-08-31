package com.kt.recycleapp.kotlin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.kt.recycleapp.kotlin.viewmodel.AlertViewModel
import java.recycleapp.R
import java.recycleapp.databinding.FragmentAlertBinding

class AlertFragment : DialogFragment() {
    lateinit var binding :FragmentAlertBinding
    lateinit var viewModel:AlertViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_alert, container, false)
        viewModel = ViewModelProvider(this).get(AlertViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val bundle = arguments
        val type = bundle?.getString("AlertType")
        var text = ""
        when(type){
            "AddSuccess" -> {
                text = "상품 등록이 완료되었습니다!"
            }
        }
        viewModel.str.value = text

        binding.AlertBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}