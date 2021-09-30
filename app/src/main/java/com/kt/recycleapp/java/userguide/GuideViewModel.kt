package com.kt.recycleapp.java.userguide

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.kt.recycleapp.java.userguide.fragments.*

class GuideViewModel:ViewModel() {

    val frgList = java.util.ArrayList<Fragment>()

    fun initList() {
        frgList.add(UserGuideFragment())
        frgList.add(UserGuideFragment2())
        frgList.add(UserGuideFragment3())
        frgList.add(UserGuideFragment4())
        frgList.add(UserGuideFragment5())
        frgList.add(UserGuideFragment6())
        frgList.add(UserGuideFragment7())
        frgList.add(UserGuideFragment8())
        frgList.add(UserGuideFragment9())
    }


}