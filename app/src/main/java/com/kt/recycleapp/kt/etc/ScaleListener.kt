package com.kt.recycleapp.kt.etc

import android.util.Log
import android.view.ScaleGestureDetector
import com.kt.recycleapp.kt.viewmodel.CameraSettingViewModel

class ScaleListener(v: CameraSettingViewModel, mScaleFactor: Float): ScaleGestureDetector.SimpleOnScaleGestureListener(){
    var f = mScaleFactor
    var zoomcnt = 0
    val viewModel = v
    override fun onScale(detector: ScaleGestureDetector?): Boolean {

        zoomcnt++
        if(zoomcnt >2){
            zoomcnt = 0
            f *= detector!!.scaleFactor
            f = Math.max(1f, Math.min(f, 10.0f))
            viewModel.zoomCnt.value = f
            Log.d(viewModel.zoomCnt.value.toString(), "um")
        }

        return super.onScale(detector)
    }
}