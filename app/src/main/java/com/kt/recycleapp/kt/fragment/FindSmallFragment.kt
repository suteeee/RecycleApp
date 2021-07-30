package com.kt.recycleapp.kt.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kt.recycleapp.kt.activity.MainActivity
import com.kt.recycleapp.kt.activity.OnBackPressListener
import com.kt.recycleapp.kt.viewmodel.FindFragmentViewModel
import java.recycleapp.R

class FindSmallFragment : Fragment(), OnBackPressListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val v = FindFragmentViewModel()
        val arr = v.findSmall()

        v.findSmallProgress.observe(viewLifecycleOwner,{
            if(it == "finish"){
                arr.forEach { res -> Log.d(res.toString(),"123") }
            }
        })

        return inflater.inflate(R.layout.fragment_find_small, container, false)
    }

    override fun onBack() {
        val act = activity as MainActivity
        act.setOnBackPressListener(null)
        act.supportFragmentManager.beginTransaction().replace(R.id.small_layout1,MainFragment()).commit()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val act = activity as MainActivity
        act.setOnBackPressListener(this)
    }
}