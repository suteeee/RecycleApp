package com.kt.recycleapp.kotlin.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.kt.recycleapp.kotlin.activity.MainActivity
import com.kt.recycleapp.kotlin.activity.OnBackPressListener
import com.kt.recycleapp.kotlin.adapter.AddAdapter
import com.kt.recycleapp.kotlin.viewmodel.AddViewModel
import com.kt.recycleapp.kotlin.viewmodel.MainViewModel
import com.kt.recycleapp.model.DatabaseReadModel
import java.recycleapp.R
import java.recycleapp.databinding.FragmentMultyAddBinding

class MultyAddFragment : Fragment(), OnBackPressListener {
    lateinit var binding:FragmentMultyAddBinding
    lateinit var viewModel:AddViewModel
    var ld = MutableLiveData<String>()
    var data = DatabaseReadModel.instance
    var cnt = 0

    lateinit var act:MainActivity
    lateinit var actViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_multy_add, container, false)

        viewModel = ViewModelProvider(this).get(AddViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        Glide.with(requireContext()).load(R.drawable.default_nothing).override(300).into(binding.productImageIv)
        val adapter = AddAdapter(viewModel)

        binding.multyRv.adapter = adapter

        viewModel.loadingList()
        viewModel.listLoadFinish.observe(viewLifecycleOwner, {
            if(it == "finish") {
                viewModel.itemList.add(cnt++)
                AddViewModel.products.add("")
                AddViewModel.infoText.add(HashMap())
                AddViewModel.addItems.add(HashMap())

                ld.observe(viewLifecycleOwner, {
                    if (it.equals("finish")) {
                        binding.multyPb.visibility = View.INVISIBLE
                        binding.itemaddBtn.setOnClickListener {
                            viewModel.itemList.add(cnt++)
                            AddViewModel.products.add("")
                            AddViewModel.infoText.add(HashMap())
                            AddViewModel.addItems.add(HashMap())
                        }
                    }
                })
            }
        })



        AddViewModel.summit.observe(viewLifecycleOwner,{
            if(it == "click"){
               binding.multyPb.visibility = View.VISIBLE
            }
            else{
                binding.multyPb.visibility = View.INVISIBLE
            }
        })

        binding.addAllBtn.setOnClickListener {
            viewModel.uploadAll()
            AlertFragment.showAlert(act,"AddSuccess",true)
            viewModel.photoUri = null
            productClear()
        }

        binding.imageUploadBtn.setOnClickListener {
            val uri: Uri = Uri.parse("${MainFragment.outputDirectory}")
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.setDataAndType(uri, "image/*")
            startActivityForResult(photoPickerIntent,0)
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.photoUri = data?.data
        Glide.with(requireContext()).load( viewModel.photoUri).override(300).into(binding.productImageIv)
    }

    fun addNewProduct(){
        viewModel.itemList.add(cnt++)
        AddViewModel.products.add("")
        AddViewModel.infoText.add(HashMap())
        AddViewModel.addItems.add(HashMap())
    }

    fun productClear() {
        viewModel.itemList.clear()
        AddViewModel.products.clear()
        AddViewModel.infoText.clear()
        AddViewModel.addItems.clear()
        addNewProduct()
    }

    override fun onBack() {
        act.setOnBackPressListener(null)
        act.replaceFragment(MainFragment())
        //act.supportFragmentManager.beginTransaction().replace(R.id.small_layout1, MainFragment()).commit()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as MainActivity).setOnBackPressListener(this)
        act = activity as MainActivity
    }
}