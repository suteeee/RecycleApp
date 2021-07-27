package com.kt.recycleapp.kt.camera

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata

private class MyImageAnalyzer: ImageAnalysis.Analyzer {
    private fun degreesToFirebaseRotation(degree:Int):Int = when(degree){
        0 -> FirebaseVisionImageMetadata.ROTATION_0
        90 -> FirebaseVisionImageMetadata.ROTATION_90
        180 -> FirebaseVisionImageMetadata.ROTATION_180
        270 -> FirebaseVisionImageMetadata.ROTATION_270
        else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
    }

    override fun analyze(image: ImageProxy) {

    }


}