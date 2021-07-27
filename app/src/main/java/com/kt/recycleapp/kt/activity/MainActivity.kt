package com.kt.recycleapp.kt.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.kt.recycleapp.java.fragment.AdvancedSearchFragment
import com.kt.recycleapp.java.fragment.AppSettingFragment
import com.kt.recycleapp.java.fragment.DailyTipFragment
import com.kt.recycleapp.java.fragment.HistoryFragment
import com.kt.recycleapp.kt.fragment.CameraSettingFragment
import com.kt.recycleapp.kt.fragment.FavoriteItemFragment
import com.kt.recycleapp.kt.fragment.FindFragment
import com.kt.recycleapp.kt.fragment.RecycleDayInfoFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.lang.Exception
import java.recycleapp.R
import java.recycleapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private var preview: Preview? = null
    private var imageCapture:ImageCapture? = null
    private lateinit var outputDirectory:File
    private lateinit var cameraExecutor:ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        cameraSet()

        binding.captureBtn.setOnClickListener { takePhoto() }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()

        setSupportActionBar(mainToolBar_tb1)
        val actionBar =supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        actionBar?.setDisplayShowTitleEnabled(false)

        navi_nv.setNavigationItemSelectedListener { menuItem ->
            var fragment : Fragment? = null


            menuItem.isChecked = true
            drawer_layout.closeDrawers()
            val id = menuItem.itemId
            val title = menuItem.title.toString()
            if (id == R.id.find) {
                Toast.makeText(this, "$title: find", Toast.LENGTH_SHORT).show()
                fragment = FindFragment()
            } else if (id == R.id.advancedSearch) {
                Toast.makeText(this, "$title: adv", Toast.LENGTH_SHORT).show()
                fragment = AdvancedSearchFragment()
            } else if (id == R.id.cameraSetting) {
                Toast.makeText(this, "$title: cs", Toast.LENGTH_SHORT).show()
                fragment = CameraSettingFragment()
            }
            else if (id == R.id.favoriteItem) {
                Toast.makeText(this, "$title: cs", Toast.LENGTH_SHORT).show()
                fragment = FavoriteItemFragment()
            }
            else if (id == R.id.history) {
                Toast.makeText(this, "$title: cs", Toast.LENGTH_SHORT).show()
                fragment = HistoryFragment()
            }
            else if (id == R.id.recycleDayInfo) {
                Toast.makeText(this, "$title: cs", Toast.LENGTH_SHORT).show()
                fragment = RecycleDayInfoFragment()
            }
            else if (id == R.id.dailyTip) {
                Toast.makeText(this, "$title: cs", Toast.LENGTH_SHORT).show()
                fragment = DailyTipFragment()
            }
            else if (id == R.id.AppSetting) {
                Toast.makeText(this, "$title: cs", Toast.LENGTH_SHORT).show()
                fragment = AppSettingFragment()
            }

            if (fragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.container_layout1,fragment).commit()
            }
            true
        }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    private fun cameraSet() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider:ProcessCameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try{
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            }catch (e:Exception){
                Log.d("cameraX","Binding failed")
            }

        },ContextCompat.getMainExecutor(this))


    }

    private fun takePhoto() {
        val imageCapture = imageCapture?:return
        val photoFile = File(outputDirectory,newJpgFileName())
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOption,ContextCompat.getMainExecutor(this),object:ImageCapture.OnImageSavedCallback{
            override fun onError(exception: ImageCaptureException) {
                Log.d("cameraX","captureFail")
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val saveUri = Uri.fromFile(photoFile)
                val msg = "Success:$saveUri"
                Toast.makeText(baseContext,msg,Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun newJpgFileName():String{
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = "Recycle_${sdf.format(System.currentTimeMillis())}"
        return "${filename}.jpg"
    }

    private fun getOutputDirectory():File{
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it,resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if(mediaDir != null && mediaDir.exists()) mediaDir
        else filesDir
    }

    fun barcodeOption() {
        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(
                FirebaseVisionBarcode.FORMAT_EAN_8,
                FirebaseVisionBarcode.FORMAT_EAN_13)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}