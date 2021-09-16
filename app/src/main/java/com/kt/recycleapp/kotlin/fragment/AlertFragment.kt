package com.kt.recycleapp.kotlin.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.kt.recycleapp.kotlin.activity.MainActivity
import com.kt.recycleapp.kotlin.viewmodel.AlertViewModel
import java.recycleapp.R
import java.recycleapp.databinding.FragmentAlertBinding

class AlertFragment : DialogFragment() {
    companion object {
        val instance = AlertFragment()
        val bundle = Bundle()
        fun showAlert(activity: MainActivity,value:String,cancel:Boolean) {
            bundle.putString("AlertType", value)
            instance.arguments = bundle
            instance.isCancelable = cancel
            instance.show(activity.frgMng.beginTransaction(),null)
        }

    }

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
            "HistoryDeleteSuccess" -> {
                text = "히스토리 삭제가 완료되었습니다!"
            }
        }
        viewModel.str.value = text

        binding.AlertBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}