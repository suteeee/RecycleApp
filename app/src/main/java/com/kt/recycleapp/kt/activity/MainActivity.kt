package com.kt.recycleapp.kt.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.kt.recycleapp.java.fragment.*
import com.kt.recycleapp.kt.fragment.FavoriteItemFragment
import com.kt.recycleapp.kt.fragment.FindFragment
import com.kt.recycleapp.kt.fragment.MainFragment
import com.kt.recycleapp.kt.fragment.RecycleDayInfoFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.recycleapp.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                fragment = FindFragment()
            } else if (id == R.id.advancedSearch) {
                Toast.makeText(this, "$title: adv", Toast.LENGTH_SHORT).show()
                fragment = AdvancedSearchFragment()
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
            else if(id == R.id.test) {
                fragment = AnnounceRecyclePageFragment()
            }

            if (fragment != null) {
                //프래그먼트 트랜잭션(프래그먼트 전환)
                supportFragmentManager.beginTransaction().replace(R.id.small_layout1,fragment).commit()
            }
            true
        }
    }


    //툴바에 있는 버튼 선택시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //홈 버튼(메뉴버튼 눌렀을때)
            android.R.id.home->{
                //메뉴 오픈
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu) :Boolean{

        //메뉴 지정
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }
}