package com.kt.recycleapp.kt.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.camera.core.Camera
import androidx.camera.core.CameraInfo
import androidx.camera.view.CameraController
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kt.recycleapp.kt.viewmodel.CameraSettingFragmenViewModel
import kotlinx.android.synthetic.main.fragment_camera_setting.view.*
import java.recycleapp.R
import java.recycleapp.databinding.FragmentCameraSettingBinding

class CameraSettingFragment : Fragment() {
    val ZOOM = 0
    lateinit var binding: FragmentCameraSettingBinding
    lateinit var cameraSettingViewModel:CameraSettingFragmenViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_camera_setting, container, false)
        cameraSettingViewModel = ViewModelProvider(this).get(CameraSettingFragmenViewModel::class.java)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = cameraSettingViewModel
        }
        val rootView = binding.root



        val seekBar = rootView.camera_seekv
        rootView.zoom_btn.setOnClickListener {
           seekBar.visibility = View.VISIBLE
            seekSet(seekBar,5,ZOOM)
        }


        return rootView
    }

    fun seekSet(seekBar:SeekBar, m:Int, flag:Int) {
        seekBar.max = m
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when(flag){
                    ZOOM->{
                        cameraSettingViewModel.zoomCnt.value = progress.toFloat()
                        binding.invalidateAll()
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

    }

}