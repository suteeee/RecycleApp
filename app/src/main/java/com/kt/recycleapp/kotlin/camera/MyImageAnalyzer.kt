package com.kt.recycleapp.kotlin.camera

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.kt.recycleapp.kotlin.fragment.BarcodeListener

class MyImageAnalyzer(private val barcodeListener : BarcodeListener): ImageAnalysis.Analyzer {
    val scanner = BarcodeScanning.getClient()

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze( imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if(mediaImage != null){

            val image = InputImage.fromMediaImage(mediaImage,imageProxy.imageInfo.rotationDegrees)

            scanner.process(image).addOnSuccessListener { barcodes->
                for(barcode in barcodes){
                    barcodeListener(barcode.rawValue ?:"")
                }
            }
                .addOnFailureListener {  }
                .addOnCompleteListener { imageProxy.close() }
        }


    }

}