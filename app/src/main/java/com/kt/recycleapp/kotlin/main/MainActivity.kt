package com.kt.recycleapp.kotlin.main

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kt.recycleapp.java.fragment.*
import com.kt.recycleapp.java.userguide.UserGuideMainFragment
import com.kt.recycleapp.kotlin.listener.OnBackPressListener
import com.kt.recycleapp.kotlin.favorite.FavoriteData
import com.kt.recycleapp.kotlin.find.FindBigData
import com.kt.recycleapp.kotlin.find.FindSmallData
import com.kt.recycleapp.kotlin.favorite.FavoriteItemFragment
import com.kt.recycleapp.kotlin.find.FindFragment
import com.kt.recycleapp.kotlin.history.HistoryData
import com.kt.recycleapp.kotlin.history.HistoryFragment
import com.kt.recycleapp.kotlin.upload.DataUploadFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navi_header.view.*
import java.recycleapp.R
import java.recycleapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    var mBackPressListener : OnBackPressListener? = null
    var pressedTime = 0L
    lateinit var viewModel : MainViewModel
    lateinit var binding:ActivityMainBinding
    lateinit var frgMng :FragmentManager
    lateinit var searchView: SearchView
    lateinit var homeBtn: ImageView

    var tempText = ""

    companion object{
        var historyItemsForSearch = ArrayList<HistoryData>()
        var favoriteItemForSearch = ArrayList<FavoriteData>()
        var findBigForSearch = ArrayList<FindBigData>()
        var findSmallForSearch = ArrayList<FindSmallData>()

        lateinit var size:Point
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var title = "???????????? ?????????!"
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        size = Point()
        display.getSize(size)

        searchView = binding.toolbarSv
        homeBtn = binding.homeBtn2

        viewModel.selectedFragment.value = "main"

        viewModel.fragmentStack.push("main")
        frgMng = supportFragmentManager

        val intent = intent.extras
        if (intent?.get("darkModeRefresh") != null) {
            frgMng.beginTransaction().replace(R.id.small_layout1, AppSettingFragment()).addToBackStack(null).commit()
        }
        else
            frgMng.beginTransaction().replace(R.id.small_layout1, MainFragment()).commit()

        MainViewModel.isPopupClose.observe(this,{
            if(it == "close"){
                mBackPressListener = null
                MainViewModel.isPopupClose.value = "open"
                goToMainFragment("back")
            }

        })

        searchView.setOnSearchClickListener {
            binding.homeBtn2.visibility = View.INVISIBLE
            tempText = viewModel.toolbarText.value!!
            viewModel.toolbarText.value = ""
        }
        searchView.setOnCloseListener(SearchView.OnCloseListener {
            binding.homeBtn2.visibility = View.VISIBLE
            if (binding.toolbarSv.query.isNotEmpty() || viewModel.searchFlag.value == "finish") {
                viewModel.toolbarText.value = tempText
                viewModel.searchFlag.value = "reset"
            }
            return@OnCloseListener false
        })
        viewModel.selectedFragment.observe(this, {


            if (it != "history" && it != "favorite" && it != "find" && it != "findsmall") {
                searchView.visibility = View.INVISIBLE
            } else {
                searchView.visibility = View.VISIBLE
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.setQuery("", false)
                    viewModel.searchFlag.value = "selected"
                    if (it == "history") {
                        val temp = ArrayList<HistoryData>()
                        historyItemsForSearch.forEach { res ->
                            if (res.barcode!!.lowercase().contains(query!!.lowercase())) {
                                temp.add(res)
                            }
                        }
                        historyItemsForSearch = temp
                    } else if (it == "favorite") {
                        val temp = ArrayList<FavoriteData>()
                        favoriteItemForSearch.forEach { res ->
                            if (res.name!!.lowercase().contains(query!!.lowercase())) {
                                temp.add(res)
                            }
                        }
                        favoriteItemForSearch = temp
                    } else if (it == "find") {
                        val temp = ArrayList<FindBigData>()
                        findBigForSearch.forEach { res ->
                            if (res.str.lowercase().contains(query!!.lowercase())) {
                                temp.add(res)
                            }
                        }
                        findBigForSearch = temp
                    } else if (it == "findsmall") {
                        val temp = ArrayList<FindSmallData>()
                        findSmallForSearch.forEach { res ->
                            if (res.str.lowercase().contains(query!!.lowercase())) {
                                temp.add(res)
                            }
                        }
                        findSmallForSearch = temp
                    }

                    viewModel.searchFlag.value = "finish"

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

            when(it) {
                "main" -> {title = "???????????? ?????????!"}
                "find" -> {title = "????????????"}
                "adv" -> {title = "??????????????????"}
                "favorite" -> {title = "????????????"}
                "history" -> {title = "????????????"}
                "recycle" -> {title = "???????????? ?????????"}
                "tip" -> {title = "????????? ???"}
                "setting" -> {title = "????????????"}
                "userGuide" -> {title = "?????? ?????????"}
                "dataUpload" -> {title = "????????? ?????????"}
            }
            viewModel.toolbarText.value = title

        })

        homeBtn.setOnClickListener {
            goToMainFragment("home")
        }

        //????????? ??????
        val popup = PopupFragmentStartpage.getInstance()
        popup.show(frgMng, PopupFragmentStartpage.TAG_EVENT_DIALOG)

        //??? ????????? noactionbar ??? ???????????? ????????? ????????????????????? actionbar??? ????????? ?????? toolbar??? ???????????? ??????
        setSupportActionBar(mainToolBar_tb1)
        val actionBar = supportActionBar

        //????????? ????????????
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        //?????? ??????????????????
        actionBar?.setDisplayShowTitleEnabled(false)


        //?????? ???????????? ?????????
        naviSet()

        val header = navi_nv.getHeaderView(0)
        Glide.with(applicationContext).load(R.drawable.header_image).override(400).into(header.header_Iv)

    }

    fun naviSet() {

        //?????? ?????? ?????????
        navi_nv.setNavigationItemSelectedListener { menuItem ->
            var fragment : Fragment? = null
            var selected = ""
            menuItem.isChecked = true
            drawer_layout.closeDrawers()
            viewModel.isDrawerOpen.value = false

            //????????? ????????? id
            val id = menuItem.itemId

            //????????? ?????? ????????????
            var layout = R.id.small_layout1

            //???????????? ?????? ??????
            if (id == R.id.find) {
                fragment = FindFragment()
            }
            else if (id == R.id.advancedSearch) {
                selected = "adv"
                fragment = AdvancedSearchFragment()
            }
            else if (id == R.id.favoriteItem) {
                selected = "favorite"
                fragment = FavoriteItemFragment()
            }
            else if (id == R.id.history) {
                selected = "history"
                fragment = HistoryFragment()
            }
            else if (id == R.id.recycleDayInfo) {
                selected = "recycle"
                fragment = RecycleDayInfoFragment()
            }
            else if (id == R.id.dailyTip) {
                selected = "tip"
                fragment = DailyTipFragment()
            }
            else if (id == R.id.AppSetting) {
                selected = "setting"
                fragment = AppSettingFragment()
            }
            else if (id == R.id.userGuide) {
                selected = "userGuide"
                fragment =
                    UserGuideMainFragment()
            }
            else if (id == R.id.dataUpload) {
                selected = "dataUpload"
                fragment = DataUploadFragment()
            }
            viewModel.selectedFragment.value = selected

            if (fragment != null) {
                //??????????????? ????????????(??????????????? ??????)
                 if(viewModel.selectedFragment.value == "main"){
                     frgMng.beginTransaction().replace(layout,fragment).addToBackStack(null).commit()
                 }else{
                     frgMng.beginTransaction().add(layout,fragment).addToBackStack(null).commit()
                 }
            }

            true
        }
    }


    //????????? ?????? ?????? ?????????
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            //??? ??????(???????????? ????????????)
            android.R.id.home->{
                //?????? ??????
                viewModel.isDrawerOpen.value = true
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun replaceFragment(frg:Fragment) {
        frgMng.beginTransaction().replace(R.id.small_layout1,frg).commit()
    }
    fun replaceFragmentWithBackStack(frg:Fragment,name:String?) {
        frgMng.beginTransaction().replace(R.id.small_layout1,frg).addToBackStack(name).commit()
    }
    fun replaceFragmentWithCommitAllowingStateLoss(frg:Fragment) {
        frgMng.beginTransaction().replace(R.id.small_layout1,frg).commitAllowingStateLoss()
    }


    fun setOnBackPressListener(listener: OnBackPressListener?){
            mBackPressListener = listener
    }

    fun goToMainFragment(flag:String) {
        viewModel.selectedFragment.value = "main"
        while (frgMng.backStackEntryCount != 0){
            frgMng.popBackStackImmediate()
        }
        viewModel.fragmentStack.clear()
        viewModel.fragmentStack.add("main")

        when(flag){
            "back" -> frgMng.beginTransaction().replace(R.id.small_layout1, MainFragment()).commit()
            "home" -> frgMng.beginTransaction().add(R.id.small_layout1, MainFragment()).commit()
        }

    }

    fun showToast(str:String) {
        Toast.makeText(applicationContext,str,Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if(!binding.toolbarSv.isIconified){
            viewModel.toolbarText.value = tempText

            if(searchView.query.isNotEmpty() || viewModel.searchFlag.value == "finish"){
                viewModel.searchFlag.value = "reset"
            }
            searchView.isIconified = true
        }
        else if(viewModel.isDrawerOpen.value == true){
            Log.d("MainBack","1")
            drawer_layout.closeDrawer(GravityCompat.START)
            viewModel.isDrawerOpen.value = false
        }
        else if(viewModel.selectedFragment.value != "main"){
            Log.d("MainBack","2")
            viewModel.isPopup.value = true
            mBackPressListener = null
            super.onBackPressed()
        }
        else if(mBackPressListener != null){
            Log.d("MainBack","3 ${mBackPressListener.toString()}")
            viewModel.isPopup.value = true
            mBackPressListener!!.onBack()
        }
        else{
            Log.d("MainBack","4")
            if (pressedTime == 0L) {
                Toast.makeText(this, "?????? ??? ????????? ???????????????.", Toast.LENGTH_SHORT).show()
                pressedTime = System.currentTimeMillis()
            } else {
                val second = (System.currentTimeMillis() - pressedTime).toInt()
                if (second > 2000) {
                    Toast.makeText(this, "?????? ??? ????????? ???????????????.", Toast.LENGTH_SHORT).show()
                    pressedTime = 0
                } else {
                    super.onBackPressed()
                    finish()
                }
            }
        }
    }
}