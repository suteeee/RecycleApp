package com.kt.recycleapp.kotlin.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kt.recycleapp.kotlin.activity.MainActivity
import com.kt.recycleapp.kotlin.viewmodel.ImageUploadViewModel
import com.kt.recycleapp.model.DatabaseReadModel
import java.recycleapp.R
import java.recycleapp.databinding.FragmentImageUploadBinding

class ImageUploadFragment : DialogFragment() {
    lateinit var binding:FragmentImageUploadBinding
    lateinit var viewModel: ImageUploadViewModel
    lateinit var db :DatabaseReadModel

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

        binding.imageUploadFrgOk.setOnClickListener {
            if(binding.imageUploadFrgName.text.isEmpty()) {
                Toast.makeText(context,"이름을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            else {
                viewModel.uploadImage(binding.imageUploadFrgName.text.toString(),db)
                AlertFragment.showAlert(requireActivity() as MainActivity,"ImgLoading",false)
                dismiss()
            }

        }

        //갤러리에서 이미지 가지고 오기
        binding.imageUploadFrgBtn.setOnClickListener { imageChoose() }

        //창 닫기
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