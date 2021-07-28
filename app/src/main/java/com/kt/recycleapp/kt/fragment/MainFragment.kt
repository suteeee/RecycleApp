package com.kt.recycleapp.kt.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.kt.recycleapp.kt.camera.MyImageAnalyzer
import com.kt.recycleapp.kt.viewmodel.CameraSettingFragmenViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.io.File
import java.recycleapp.R
import java.recycleapp.databinding.FragmentMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

typealias BarcodeListener = (barcode: String) -> Unit

class MainFragment : Fragment() {
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    private val REQUEST_CODE_PERMISSIONS = 10
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var detector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    var processingBarcode = AtomicBoolean(false)
    lateinit var binding: FragmentMainBinding
    lateinit var viewModel:CameraSettingFragmenViewModel

    private var camera: Camera? = null
    private var cameraController: CameraControl? = null
    private var cameraInfo :CameraInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main, container, false)
        viewModel = ViewModelProvider(this).get(CameraSettingFragmenViewModel::class.java)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }

        val rootView = binding.root

        binding.captureBtn.setOnClickListener {
            when(cameraInfo?.torchState?.value){
                TorchState.ON->{
                    cameraController?.enableTorch(false)
                    Log.d("torch","on")
                }
                TorchState.OFF->{
                    cameraController?.enableTorch(true)
                    Log.d("torch","off")
                }
            }
        }

        return rootView
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(
                    previewView.surfaceProvider
                )
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, MyImageAnalyzer { barcode ->
                        if (processingBarcode.compareAndSet(false, true)) {
                            Toast.makeText(activity?.baseContext,barcode,Toast.LENGTH_SHORT).show()
                        }
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
                cameraController = camera!!.cameraControl
                cameraInfo = camera!!.cameraInfo

                cameraInfo!!.zoomState.observe(viewLifecycleOwner, Observer {
                    viewModel.zoomState.value = it.zoomRatio
                })


                viewModel.zoomCnt.observe(viewLifecycleOwner,{
                    Log.d("see",it.toString())
                    cameraController?.setZoomRatio(it)
                    binding.invalidateAll()
                })

                binding.HistoryBtn.setOnClickListener {
                    Log.d("d","asdgasdg")
                    //cameraController.setLinearZoom()

                }


            } catch (e: Exception) {
                Log.e("PreviewUseCase", "Binding failed! :(", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        cameraExecutor.shutdown()
        super.onDestroy()
    }

}