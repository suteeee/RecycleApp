package com.kt.recycleapp.kotlin.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.kt.recycleapp.java.announce.AnnounceRecyclerFragment
import com.kt.recycleapp.kotlin.activity.MainActivity
import com.kt.recycleapp.kotlin.camera.MyImageAnalyzer
import com.kt.recycleapp.kotlin.viewmodel.CameraSettingViewModel
import com.kt.recycleapp.manager.MyPreferenceManager
import com.kt.recycleapp.model.DatabaseReadModel
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

    //private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
    //private val REQUEST_CODE_PERMISSIONS = 10
    private lateinit var cameraExecutor: ExecutorService
    var processingBarcode = AtomicBoolean(false)
    lateinit var binding: FragmentMainBinding
    lateinit var viewModel:CameraSettingViewModel

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
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main, container, false)
        viewModel = ViewModelProvider(this).get(CameraSettingViewModel::class.java)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            binding.viewmodel = viewModel
            binding.invalidateAll()
        }
        (activity as MainActivity).viewModel.toolbarText.value = "수거했어 오늘도!"

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

        binding.zoomRefreshIv.setOnClickListener {
            viewModel.zoomCnt.value = "1.0x"
        }

        binding.HistoryBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.small_layout1,HistoryFragment())?.commit()
        }


        viewModel.zoomCnt.observe(viewLifecycleOwner,{
            binding.zoonShowTv.visibility = View.VISIBLE
            val f = it.substring(0..2).toFloat()
            cameraController?.setZoomRatio(f)
            binding.invalidateAll()
            var handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                binding.zoonShowTv.visibility = View.INVISIBLE
            },1000)
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
        val data = MyRoomDatabase(barcode,date,fineName,"false")
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*if (allPermissionsGranted()) {
            try {
                initCamera()
            }
            catch (e:Exception){}
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }*/

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
                val path ="$outputDirectory/$name"
                val bm = BitmapFactory.decodeFile(path)
                DatabaseReadModel.decodeImageList.add(bm)
                context?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(outputDirectory)))
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

                            captureIsFinish.observe(viewLifecycleOwner,{fin->
                                if(fin=="yes"){
                                    writeDB(barcode,fineName)
                                    captureIsFinish.value="no"
                                    val bundle = Bundle()
                                    bundle.putString("barcode",barcode)

                                    val frg = AnnounceRecyclerFragment()
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


            } catch (e: Exception) {
                Log.e("PreviewUseCase", "Binding failed! :(", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    fun getFingerSpacing(event :MotionEvent):Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }

   /* private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }*/

    /*override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                try {
                    initCamera()
                }
                catch (e:Exception){}
            } else {
                requestPermissions(permissions, requestCode)
                Toast.makeText(requireContext(), "카메라 기능을 사용하실 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }*/

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

    override fun onAttach(context: Context) {
        Log.d("search","create!!")
        super.onAttach(context)
    }
}