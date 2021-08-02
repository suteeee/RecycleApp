package com.kt.recycleapp.kt.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.lifecycle.ViewModelProvider
import com.kt.recycleapp.java.fragment.AnnounceRecyclePageFragment
import com.kt.recycleapp.kt.camera.MyImageAnalyzer
import com.kt.recycleapp.kt.viewmodel.CameraSettingFragmenViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.navi_header.*
import kotlinx.android.synthetic.main.navi_header.view.*
import java.io.File
import java.recycleapp.databinding.FragmentMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.recycleapp.R
import java.text.SimpleDateFormat
import androidx.camera.core.CameraSelector
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.kt.recycleapp.model.MyRoomDatabase
import com.kt.recycleapp.model.RoomHelper

typealias BarcodeListener = (barcode: String) -> Unit

class MainFragment : Fragment() {
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val REQUEST_CODE_PERMISSIONS = 10
    private lateinit var cameraExecutor: ExecutorService
    var processingBarcode = AtomicBoolean(false)
    lateinit var binding: FragmentMainBinding
    lateinit var viewModel:CameraSettingFragmenViewModel

    private var camera: Camera? = null
    private var cameraController: CameraControl? = null
    private var cameraInfo :CameraInfo? = null

    private var imageCapture :ImageCapture?= null

    private lateinit var outputDirectory:File
    private lateinit var mScaleGestureDetector : ScaleGestureDetector
    private var mScaleFactor = 1.0f
    var helper :RoomHelper? = null

    var captureIsFinish = MutableLiveData<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main, container, false)
        viewModel = ViewModelProvider(this).get(CameraSettingFragmenViewModel::class.java)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            binding.viewmodel = viewModel
            binding.invalidateAll()
        }

        val rootView = binding.root

        helper = Room.databaseBuilder(requireContext(),RoomHelper::class.java,"Database").allowMainThreadQueries().build()


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

        mScaleGestureDetector = ScaleGestureDetector(rootView.context,
            com.kt.recycleapp.kt.etc.ScaleListener(viewModel, mScaleFactor)
        )

        binding.previewView.setOnTouchListener{view, motionEvent->
            mScaleGestureDetector.onTouchEvent(motionEvent)
            return@setOnTouchListener false
        }

        viewModel.zoomCnt.observe(viewLifecycleOwner,{
            cameraController!!.setZoomRatio(it)
            binding.invalidateAll()
        })

        return rootView
    }

    fun writeDB(barcode: String, fineName: String) {
        val date = fineName.split("_")[1]
        val data = MyRoomDatabase(barcode,date,fineName,"false")
        helper?.databaseDao()?.insert(data)
        Log.d("data","write")
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
            //binding.targetIv.invalidate()
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    fun takePhoto() :String{
        val imageCapture = imageCapture?:return ""
        val name = newPngFileName()
        val photoFile = File(outputDirectory,name)
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOption,ContextCompat.getMainExecutor(requireContext()),object:ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                captureIsFinish.value="no"
                val saveUri = Uri.fromFile(photoFile)
                Toast.makeText(context,"카메라 캡쳐 & 저장 $saveUri",Toast.LENGTH_SHORT).show()
                captureIsFinish.value="yes"
            }

            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
                Toast.makeText(context,"캡쳐 에러 발생",Toast.LENGTH_SHORT).show()
            }
        })

        return name
    }

    private fun newPngFileName():String{
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = "Recycle_${sdf.format(System.currentTimeMillis())}"
        return "${filename}.png"
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
            imageCapture = ImageCapture.Builder().build()

            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, MyImageAnalyzer { barcode ->
                        if (processingBarcode.compareAndSet(false, true)) {
                            val fineName = takePhoto()
                            Toast.makeText(activity?.baseContext,barcode,Toast.LENGTH_SHORT).show()

                            captureIsFinish.observe(viewLifecycleOwner,{
                                if(it=="yes"){
                                    writeDB(barcode,fineName)
                                    captureIsFinish.value="no"
                                    val frg = AnnounceRecyclePageFragment()
                                    val bundle = Bundle()
                                    bundle.putString("barcode", barcode)
                                    frg.arguments = bundle

                                    activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.small_layout1,frg)?.commit()
                                }
                            })
                        }
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis,imageCapture)
                cameraController = camera!!.cameraControl
                cameraInfo = camera!!.cameraInfo


            } catch (e: Exception) {
                Log.e("PreviewUseCase", "Binding failed! :(", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getOutputDirectory():File{
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it,resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if(mediaDir != null && mediaDir.exists()) mediaDir
        else requireActivity().filesDir
    }


    override fun onDestroy() {
        cameraExecutor.shutdown()
        super.onDestroy()
    }



}