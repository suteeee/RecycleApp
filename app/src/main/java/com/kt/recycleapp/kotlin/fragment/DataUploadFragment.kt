package com.kt.recycleapp.kotlin.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kt.recycleapp.kotlin.activity.MainActivity
import com.kt.recycleapp.kotlin.adapter.UploadAdapter
import com.kt.recycleapp.kotlin.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.recycleapp.R
import java.recycleapp.databinding.DataUploadFragmentBinding

class DataUploadFragment : Fragment() {
    private lateinit var viewModel: DataUploadViewModel
    lateinit var binding: DataUploadFragmentBinding
    lateinit var progressBar: ProgressBar
    lateinit var act: MainActivity
    lateinit var actViewModel:MainViewModel
    var cnt = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.data_upload_fragment, container, false)
        viewModel = ViewModelProvider(this).get(DataUploadViewModel::class.java)
        binding.apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        progressBar = binding.uploadPb

        val adt = UploadAdapter(viewModel,act)

        binding.uploadRv.adapter = adt
        Glide.with(requireContext()).load(R.drawable.default_nothing).override(300).into(binding.imagePreviewIv)

        viewModel.loadingList()
        viewModel.listLoadFinish.observe(viewLifecycleOwner,{
            if(it == "finish"){
                progressBar.visibility = View.INVISIBLE
                addNewProduct()
                binding.addProductBtn.setOnClickListener { addNewProduct() }

                binding.dataUploadBtn.setOnClickListener {
                    progressBar.visibility = View.VISIBLE
                    viewModel.upload()

                }
                viewModel.listLoadFinish.value = "done"
            }
        })

        viewModel.uploadFinish.observe(viewLifecycleOwner,{
            if(it == "finish"){
                viewModel.uploadFinish.value = "done"
                viewModel.photoUri = null
                progressBar.visibility = View.INVISIBLE
                AlertFragment.showAlert(act,"AddSuccess",true)
                productClear()
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

    fun productClear() {
        viewModel.itemList.clear()
        viewModel.barcodes.clear()
        viewModel.names.clear()
        viewModel.kinds.clear()
        viewModel.subNames.clear()
        viewModel.infoText.clear()
        addNewProduct()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.photoUri = data?.data
        Glide.with(requireContext()).load( viewModel.photoUri).override(300).into(binding.imagePreviewIv)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        act = activity as MainActivity
        actViewModel = act.viewModel
        act.viewModel.selectedFragment.value = "dataUpload"
        act.viewModel.fragmentStack.push("dataUpload")
    }

    override fun onDetach() {
        super.onDetach()

        actViewModel.fragmentStack.pop()
        actViewModel.selectedFragment.value = actViewModel.fragmentStack.peek()
        if(actViewModel.fragmentStack.peek() == "main")
            act.replaceFragment(MainFragment())
    }
}