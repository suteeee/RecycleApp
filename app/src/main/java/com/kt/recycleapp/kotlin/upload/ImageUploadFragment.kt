package com.kt.recycleapp.kotlin.upload

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kt.recycleapp.kotlin.Internet
import com.kt.recycleapp.kotlin.main.MainActivity
import com.kt.recycleapp.kotlin.alert.AlertFragment
import com.kt.recycleapp.kotlin.main.MainFragment
import com.kt.recycleapp.manager.MyPreferenceManager
import com.kt.recycleapp.model.DatabaseReadModel
import java.recycleapp.R
import java.recycleapp.databinding.FragmentImageUploadBinding

class ImageUploadFragment : DialogFragment() {
    lateinit var binding:FragmentImageUploadBinding
    lateinit var viewModel: ImageUploadViewModel
    lateinit var db :DatabaseReadModel
    lateinit var prefs:MyPreferenceManager
    lateinit var act:MainActivity

    override fun onResume() {
        super.onResume()
        val params :ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = MainActivity.size.x
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_image_upload, container, false)
        viewModel = ViewModelProvider(this).get(ImageUploadViewModel::class.java)
        db = DatabaseReadModel.instance
        Glide.with(requireContext()).load(R.drawable.default_nothing).override(300).into(binding.imageUploadIv)
        prefs = MyPreferenceManager(requireContext())
        act = requireActivity() as MainActivity

        val iStatus = Internet.getStatus(requireContext())

        when(iStatus) {
            Internet.MOBILE_DATA -> {
                if(!prefs.mobileInternetShow) {
                    act.showToast(resources.getString(R.string.mobileData))
                    prefs.mobileInternetShow = true
                }
            }
            Internet.NOT_CONNECT -> {
                AlertFragment.showAlert((requireActivity() as MainActivity), "InternetNotConnectedToUpload", true)
                dismiss()
            }
        }

        binding.imageUploadFrgOk.setOnClickListener {
            if(binding.imageUploadFrgName.text.isEmpty()) {
                Toast.makeText(context,"????????? ??????????????????",Toast.LENGTH_SHORT).show()
            }
            else {
                viewModel.uploadImage(binding.imageUploadFrgName.text.toString(),db)
                AlertFragment.showAlert(requireActivity() as MainActivity, "ImgLoading", false)
                dismiss()
            }

        }

        //??????????????? ????????? ????????? ??????
        binding.imageUploadFrgBtn.setOnClickListener { imageChoose() }

        //??? ??????
        binding.imageUploadFrgCancel.setOnClickListener { dismiss() }

        return binding.root
    }

    fun imageChoose() {
        val uri: Uri = Uri.parse("${MainFragment.outputDirectory}")
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.setDataAndType(uri, "image/*")
        startActivityForResult(photoPickerIntent,0)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.photoUri = data?.data
        Glide.with(requireContext()).load( viewModel.photoUri).override(300).into(binding.imageUploadIv)
    }


}