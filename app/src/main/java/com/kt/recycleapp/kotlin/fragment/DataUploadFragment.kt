package com.kt.recycleapp.kotlin.fragment

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.kt.recycleapp.kotlin.adapter.UploadAdapter
import java.recycleapp.R
import java.recycleapp.databinding.DataUploadFragmentBinding

class DataUploadFragment : Fragment() {
    private lateinit var viewModel: DataUploadViewModel
    lateinit var binding: DataUploadFragmentBinding
    var cnt = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.data_upload_fragment, container, false)
        viewModel = ViewModelProvider(this).get(DataUploadViewModel::class.java)

        binding.apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        val adt = UploadAdapter(viewModel)

        binding.uploadRv.adapter = adt
        Glide.with(requireContext()).load(R.drawable.default_nothing).override(300).into(binding.imagePreviewIv)

        viewModel.loadingList()
        viewModel.listLoadFinish.observe(viewLifecycleOwner,{
            if(it == "finish"){
                binding.uploadPb.visibility = View.INVISIBLE
                addNewProduct()
                binding.addProductBtn.setOnClickListener { addNewProduct() }

                binding.dataUploadBtn.setOnClickListener {
                    binding.uploadPb.visibility = View.VISIBLE
                    Log.d(viewModel.barcodes.toString(),"upBarcode")
                    Log.d(viewModel.names.toString(),"upName")
                    Log.d(viewModel.kinds.toString(),"upKine")
                    Log.d(viewModel.subNames.toString(),"upSub")
                    viewModel.upload()

                }
                viewModel.listLoadFinish.value = "done"
            }
        })

        viewModel.uploadFinish.observe(viewLifecycleOwner,{
            if(it == "finish"){
                viewModel.uploadFinish.value = "done"
                viewModel.photoUri = null
                binding.uploadPb.visibility = View.INVISIBLE
                Toast.makeText(context,"데이터 업로드 완료",Toast.LENGTH_SHORT).show()
            }
        })

        binding.uploadImageBtn.setOnClickListener {
            val uri: Uri = Uri.parse("${MainFragment.outputDirectory}")
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.setDataAndType(uri, "image/*")
            startActivityForResult(photoPickerIntent,0)
        }

        return binding.root
    }

    fun addNewProduct(){
        viewModel.itemList.add(cnt++)
        viewModel.barcodes.add("")
        viewModel.names.add("")
        viewModel.kinds.add("")
        viewModel.subNames.add("")
        viewModel.infoText.add("")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.photoUri = data?.data
        Glide.with(requireContext()).load( viewModel.photoUri).override(300).into(binding.imagePreviewIv)
    }


}