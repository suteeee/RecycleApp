package com.kt.recycleapp.kotlin.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.kt.recycleapp.java.announce.AnnounceRecyclerFragment
import com.kt.recycleapp.kotlin.analyzer.MyImageAnalyzer
import com.kt.recycleapp.kotlin.history.HistoryFragment
import com.kt.recycleapp.manager.MyPreferenceManager
import com.kt.recycleapp.model.MyRoomDatabase
import com.kt.recycleapp.model.RoomHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.navi_header.*
import kotlinx.android.synthetic.main.navi_header.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.recycleapp.R
import java.recycleapp.databinding.FragmentMainBinding
import java.text.SimpleDateFormat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.sqrt

typealias BarcodeListener = (barcode: String) -> Unit

class MainFragment : Fragment() {
    companion object{
        lateinit var outputDirectory:File
    }

    private lateinit var cameraExecutor: ExecutorService
    var processingBarcode = AtomicBoolean(false)
    lateinit var binding: FragmentMainBinding
    lateinit var viewModel: CameraViewModel

    private var camera: Camera? = null
    private var cameraController: CameraControl? = null
    private var cameraInfo :CameraInfo? = null

    private var imageCapture :ImageCapture?= null
    var helper :RoomHelper? = null
    lateinit var prefs :MyPreferenceManager

    var captureIsFinish = MutableLiveData<String>()

    var dist = 0.0f
    var zoomCnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = MyPreferenceManager(requireContext())
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        helper = RoomHelper.getInstance(requireContext())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main, container, false)
        viewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            binding.viewmodel = viewModel
            binding.invalidateAll()
        }

        val rootView = binding.root

        binding.captureBtn.setOnClickListener {
            when(cameraInfo?.torchState?.value){
                TorchState.ON->{ cameraController?.enableTorch(false) }
                TorchState.OFF->{ cameraController?.enableTorch(true) }
            }
        }

        binding.zoomRefreshIv.setOnClickListener {
            viewModel.zoomCnt.value = "1.0x"
        }

        binding.HistoryBtn.setOnClickListener {
            fragmentChange(HistoryFragment())
        }


        viewModel.zoomCnt.observe(viewLifecycleOwner,{
            binding.zoonShowTv.visibility = View.VISIBLE
            val f = it.substring(0..2).toFloat()
            cameraController?.setZoomRatio(f)
            binding.invalidateAll()
            var handler = Handler(Looper.getMainLooper())
            handler.postDelayed({ binding.zoonShowTv.visibility = View.INVISIBLE },2000)
        })

        if(prefs.cameraPermission == "GRANTED"){
            initCamera()
        }else{
            Toast.makeText(requireContext(),"카메라 기능을 이용하실수 없습니다. 권한을 허용해주세요.",Toast.LENGTH_SHORT).show()
        }

        return rootView
    }

    fun writeDB(barcode: String, fineName: String) {
        val date = fineName.split("_")[1]
        var index = prefs.sqLiteIndex
        prefs.sqLiteIndex = index+1
        val data = MyRoomDatabase(barcode,date,fineName,"false",index)
        helper?.databaseDao()?.insert(data)
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

    fun takePhoto() :String{
        val imageCapture = imageCapture?:return ""
        val name = newPngFileName()
        val photoFile = File(outputDirectory,name)
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOption,ContextCompat.getMainExecutor(requireContext()),object:ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                captureIsFinish.value="no"
                val path ="$outputDirectory/$name"
                val bm = BitmapFactory.decodeFile(path)
                context?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(
                    outputDirectory
                )))
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

    @SuppressLint("ClickableViewAccessibility")
    private fun initCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()

            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, MyImageAnalyzer { barcode ->
                        if (processingBarcode.compareAndSet(false, true)) {
                            val fineName = if(prefs.storagePermission == "GRANTED") {
                                takePhoto()
                            }
                            else {"null"}

                            if(prefs.storagePermission == "DENIED") captureIsFinish.value = "yes"

                            captureIsFinish.observe(viewLifecycleOwner,{fin->
                                if(fin=="yes"){
                                    Log.d("prefs",prefs.storagePermission)
                                    if(prefs.storagePermission == "GRANTED") writeDB(barcode,fineName)
                                    captureIsFinish.value="no"
                                    val bundle = Bundle()
                                    bundle.putString("barcode",barcode)
                                    bundle.putBoolean("capture",true)

                                    val frg = AnnounceRecyclerFragment()
                                    frg.arguments = bundle

                                    fragmentChange(frg)
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
                var multiTouch = false
                binding.previewView.setOnTouchListener { v, event ->
                    val iv = binding.focustabIv
                    when(event.action and MotionEvent.ACTION_MASK){

                        MotionEvent.ACTION_MOVE ->{
                            if(event.pointerCount == 2) {
                                if (zoomCnt >= 1){
                                    zoomCnt = 0
                                    multiTouch = true
                                val max = 3.0f
                                val min = 1.0f
                                val newDist = getFingerSpacing(event)
                                val curZoom = viewModel.zoomCnt.value?.substring(0..2)?.toFloat()

                                if (dist < newDist && curZoom != null) {
                                    if (curZoom < max) {
                                        viewModel.zoomCnt.value = String.format("%.1f",curZoom + 0.1f) + "x"
                                    }
                                    else{
                                        viewModel.zoomCnt.value = "3.0x"
                                    }
                                } else if (dist > newDist && curZoom != null) {
                                    if (curZoom > min) {
                                        viewModel.zoomCnt.value = String.format("%.1f",curZoom - 0.1f) + "x"
                                    }
                                    else{
                                        viewModel.zoomCnt.value = "1.0x"
                                    }
                                }
                                dist = newDist
                                iv.visibility = View.INVISIBLE
                            }
                                else zoomCnt++
                            }
                            return@setOnTouchListener false
                        }
                        MotionEvent.ACTION_DOWN -> {
                            GlobalScope.launch {
                                delay(100)
                                if(!multiTouch){
                                    v.performClick()
                                    val handler = Handler(Looper.getMainLooper())
                                    handler.postDelayed({
                                        iv.x = event.x - iv.width / 2
                                        iv.y = event.y - iv.height * 2
                                        iv.visibility = View.VISIBLE
                                    }, 0)
                                }
                            }
                            return@setOnTouchListener true
                        }
                        MotionEvent.ACTION_UP -> {
                            if (!multiTouch) {
                                Log.d("touch2",multiTouch.toString())
                                val factory = binding.previewView.meteringPointFactory
                                val point = factory.createPoint(event.x, event.y)
                                val action = FocusMeteringAction.Builder(point).build()
                                GlobalScope.launch {
                                    delay(100)

                                    cameraController?.startFocusAndMetering(action)
                                    v.performClick()

                                    val handler = Handler(Looper.getMainLooper())
                                    handler.postDelayed({
                                        iv.visibility = View.INVISIBLE
                                    }, 2000)
                                }
                            }
                            Log.d("touch3",multiTouch.toString())
                            multiTouch = false

                            return@setOnTouchListener true

                        }
                        else -> return@setOnTouchListener false
                    }
                }


            } catch (e: Exception) { }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    fun getFingerSpacing(event :MotionEvent):Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun getOutputDirectory():File{
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it,resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if(mediaDir != null && mediaDir.exists()) mediaDir
        else requireActivity().filesDir
    }

    fun fragmentChange(frg:Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.small_layout1,frg)?.commit()
    }


    override fun onDestroy() {
        cameraExecutor.shutdown()
        super.onDestroy()
    }
}