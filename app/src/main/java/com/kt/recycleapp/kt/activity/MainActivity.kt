package com.kt.recycleapp.kt.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kt.recycleapp.java.fragment.*
import com.kt.recycleapp.kt.etc.FavoriteData
import com.kt.recycleapp.kt.etc.FindBigData
import com.kt.recycleapp.kt.etc.FindSmallData
import com.kt.recycleapp.kt.etc.HistoryData
import com.kt.recycleapp.kt.fragment.*
import com.kt.recycleapp.kt.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navi_header.view.*
import java.recycleapp.R
import java.recycleapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    var mBackPressListener : OnBackPressListener? = null
    var pressedTime = 0L
    lateinit var viewModel :MainViewModel
    lateinit var binding:ActivityMainBinding
    var tempText = ""

    companion object{
        var historyItemsForSearch = ArrayList<HistoryData>()
        var favoriteItemForSearch = ArrayList<FavoriteData>()
        var findBigForSearch = ArrayList<FindBigData>()
        var findSmallForSearch = ArrayList<FindSmallData>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.toolbarSv.setOnSearchClickListener {
            tempText = viewModel.toolbarText.value!!
            viewModel.toolbarText.value=""
        }
        binding.toolbarSv.setOnCloseListener (SearchView.OnCloseListener {
            if(binding.toolbarSv.query.isNotEmpty() || viewModel.searchFlag.value == "finish") {
                viewModel.toolbarText.value = tempText
                viewModel.searchFlag.value = "reset"
            }
            return@OnCloseListener false
        })
        viewModel.selectedFragment.observe(this,{
            val sv = binding.toolbarSv

            if(it != "history" && it != "favorite" && it != "find" && it != "findsmall"){
                sv.visibility = View.INVISIBLE
            }
            else{
                sv.visibility = View.VISIBLE
            }

            sv.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d("query","summit")
                    sv.setQuery("",false)
                    viewModel.searchFlag.value = "selected"
                    if(it =="history"){
                        val temp = ArrayList<HistoryData>()
                        historyItemsForSearch.forEach {res->
                            if(res.barcode!!.lowercase().contains(query!!.lowercase())){
                                temp.add(res)
                            }
                        }
                        historyItemsForSearch = temp
                    }
                    else  if(it =="favorite"){
                        val temp = ArrayList<FavoriteData>()
                        favoriteItemForSearch.forEach {res->
                            if(res.name!!.lowercase().contains(query!!.lowercase())){
                                temp.add(res)
                            }
                        }
                        favoriteItemForSearch = temp
                    }

                    else  if(it =="find"){
                        val temp = ArrayList<FindBigData>()
                        findBigForSearch.forEach {res->
                            if(res.str.lowercase().contains(query!!.lowercase())){
                                temp.add(res)
                            }
                        }
                        findBigForSearch = temp
                    }
                    else  if(it =="findsmall"){
                        val temp = ArrayList<FindSmallData>()
                        findSmallForSearch.forEach {res->
                            if(res.str.lowercase().contains(query!!.lowercase())){
                                temp.add(res)
                            }
                        }
                        findSmallForSearch = temp
                    }

                    viewModel.searchFlag.value = "finish"

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d("query","change")
                    return false
                }
            })


        })

        //팝업창 추가
        val popup = PopupFragmentStartpage.getInstance()
        popup.show(supportFragmentManager,
            PopupFragmentStartpage.TAG_EVENT_DIALOG)

        //앱 테마를 noactionbar 로 설정했고 툴바를 사용할것이므로 actionbar를 우리가 만든 toolbar로 설정하는 코드
        setSupportActionBar(mainToolBar_tb1)
        val actionBar =supportActionBar

        //왼쪽에 메뉴버튼
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        //툴바 타이틀텍스트
        actionBar?.setDisplayShowTitleEnabled(false)



        //메뉴 세팅하는 함수
        naviSet()

        supportFragmentManager.beginTransaction().replace(R.id.small_layout1,MainFragment()).commit()

        val header = navi_nv.getHeaderView(0)

        header.drawerClose_btn.setOnClickListener{
            drawer_layout.closeDrawer(GravityCompat.START)
        }

    }

    fun naviSet() {

        //메뉴 선택 리스너
        navi_nv.setNavigationItemSelectedListener { menuItem ->
            var fragment : Fragment? = null

            menuItem.isChecked = true
            drawer_layout.closeDrawers()

            //선택한 메뉴의 id
            val id = menuItem.itemId

            //메뉴의 이름 가져오기
            val title = menuItem.title.toString()

            //메뉴마다 액션 지정
            if (id == R.id.find) {
                Toast.makeText(this, "$title: find", Toast.LENGTH_SHORT).show()
                viewModel.selectedFragment.value = "find"
                fragment = FindFragment()
            } else if (id == R.id.advancedSearch) {
                Toast.makeText(this, "$title: adv", Toast.LENGTH_SHORT).show()
                viewModel.selectedFragment.value = "adv"
                fragment = AdvancedSearchFragment()
            }
            else if (id == R.id.favoriteItem) {
                Toast.makeText(this, "$title: cs", Toast.LENGTH_SHORT).show()
                viewModel.selectedFragment.value = "favorite"
                fragment = FavoriteItemFragment()
            }
            else if (id == R.id.history) {
                Toast.makeText(this, "$title: cs", Toast.LENGTH_SHORT).show()
                viewModel.selectedFragment.value = "history"
                fragment = HistoryFragment()
            }
            else if (id == R.id.recycleDayInfo) {
                Toast.makeText(this, "$title: cs", Toast.LENGTH_SHORT).show()
                viewModel.selectedFragment.value = "recycle"
                fragment = RecycleDayInfoFragment()
            }
            else if (id == R.id.dailyTip) {
                Toast.makeText(this, "$title: cs", Toast.LENGTH_SHORT).show()
                viewModel.selectedFragment.value = "tip"
                fragment = DailyTipFragment()
            }
            else if (id == R.id.AppSetting) {
                Toast.makeText(this, "$title: cs", Toast.LENGTH_SHORT).show()
                viewModel.selectedFragment.value = "setting"
                fragment = AppSettingFragment()
            }

            if (fragment != null) {
                //프래그먼트 트랜잭션(프래그먼트 전환)
                supportFragmentManager.beginTransaction().replace(R.id.small_layout1,fragment).addToBackStack(null).commit()
            }
            true
        }
    }


    //툴바에 있는 버튼 선택시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("search",item.itemId.toString())
        when(item.itemId){

            //홈 버튼(메뉴버튼 눌렀을때)
            android.R.id.home->{
                //메뉴 오픈
                viewModel.isDrawerOpen.value = true
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun setOnBackPressListener(listener: OnBackPressListener?){
            mBackPressListener = listener
    }

    override fun onBackPressed() {
        if(!binding.toolbarSv.isIconified){
            viewModel.toolbarText.value = tempText
            Log.d(binding.toolbarSv.query.length.toString(),"query")
            if(binding.toolbarSv.query.isNotEmpty() || viewModel.searchFlag.value == "finish"){
                viewModel.searchFlag.value = "reset"
            }
            binding.toolbarSv.isIconified = true
        }
        else if(binding.viewModel!!.isDrawerOpen.value == true){
            drawer_layout.closeDrawer(GravityCompat.START)
            binding.viewModel!!.isDrawerOpen.value = false
        }
        else{
            if(mBackPressListener != null){
                mBackPressListener?.onBack()
            }
            else{
                if (pressedTime == 0L) {
                    Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                    pressedTime = System.currentTimeMillis()
                } else {
                    val second = (System.currentTimeMillis() - pressedTime).toInt()
                    if (second > 2000) {
                        Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                        pressedTime = 0
                    } else {
                        super.onBackPressed()
                        Log.e("!!!", "OnBackPressed:Finished, killProcess")
                        finish()
                    }
                }
            }
        }
    }
}