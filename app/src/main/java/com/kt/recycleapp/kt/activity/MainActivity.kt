package com.kt.recycleapp.kt.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Preview
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.kt.recycleapp.java.fragment.AdvancedSearchFragment
import com.kt.recycleapp.java.fragment.AppSettingFragment
import com.kt.recycleapp.java.fragment.DailyTipFragment
import com.kt.recycleapp.java.fragment.HistoryFragment
import com.kt.recycleapp.kt.fragment.CameraSettingFragment
import com.kt.recycleapp.kt.fragment.FavoriteItemFragment
import com.kt.recycleapp.kt.fragment.FindFragment
import com.kt.recycleapp.kt.fragment.RecycleDayInfoFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.recycleapp.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(mainToolBar_tb1)
        val actionBar =supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        actionBar?.setDisplayShowTitleEnabled(false)

        navi_nv.setNavigationItemSelectedListener { menuItem ->
            var fragment : Fragment? = null


            menuItem.isChecked = true
            drawer_layout.closeDrawers()
            val id = menuItem.itemId
            val title = menuItem.title.toString()
            if (id == R.id.find) {
                Toast.makeText(this, "$title: find", Toast.LENGTH_SHORT).show()
                fragment = FindFragment()
            } else if (id == R.id.advancedSearch) {
                Toast.makeText(this, "$title: adv", Toast.LENGTH_SHORT).show()
                fragment = AdvancedSearchFragment()
            } else if (id == R.id.cameraSetting) {
                Toast.makeText(this, "$title: cs", Toast.LENGTH_SHORT).show()
                fragment = CameraSettingFragment()
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

            if (fragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.container_layout1,fragment).commit()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    fun cameraSet() {
        val preview = Preview.Builder().build()
        //val viewFinder :PreviewView =
    }

    fun barcodeOption() {
        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(
                FirebaseVisionBarcode.FORMAT_EAN_8,
                FirebaseVisionBarcode.FORMAT_EAN_13)
            .build()
    }
}