package com.kt.recycleapp.kotlin.alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.kt.recycleapp.kotlin.main.MainActivity
import java.recycleapp.R
import java.recycleapp.databinding.FragmentAlertBinding

class AlertFragment : DialogFragment() {
    companion object {
        val instance = AlertFragment()
        val bundle = Bundle()
        fun showAlert(activity: MainActivity, value:String, cancel:Boolean) {
            bundle.putString("AlertType", value)
            instance.arguments = bundle
            instance.isCancelable = cancel
            instance.show(activity.frgMng.beginTransaction(),null)
        }

    }

    lateinit var binding :FragmentAlertBinding
    lateinit var viewModel: AlertViewModel

    override fun onResume() {
        super.onResume()
        val params :ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = MainActivity.size.x
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

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
            "SaveSuccess" -> {
                text = "입력사항을 저장했습니다. 업로드버튼을 꼭 눌러주세요!"
            }
            "NotEmptyEditText" -> {
                text = "빈칸으로 등록할 수 없습니다."
            }
            "UnderFlow" -> {
                text = "더이상 삭제할 수 없습니다!"
            }
            "ImgLoading" -> {
                binding.alertPb.visibility = View.VISIBLE
                binding.AlertBtn.visibility = View.INVISIBLE

                AlertViewModel.imageLoadFinish.observe(viewLifecycleOwner, {
                    if(it) {
                        text =
                            if(AlertViewModel.imageUploadResult.value == true) "이미지 업로드가 완료되었습니다!"
                            else "이미지 업로드에 실패하였습니다.."
                        viewModel.str.value = text
                        binding.AlertBtn.visibility = View.VISIBLE
                        binding.alertPb.visibility = View.INVISIBLE
                        AlertViewModel.imageLoadFinish.value = false
                    }
                })
            }
        }
        viewModel.str.value = text

        binding.AlertBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}