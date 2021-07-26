package com.kt.recycleapp.kt.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.kt.recycleapp.kt.fragment.FindFragment
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

        navi_nv.setNavigationItemSelectedListener { menuItem ->
            var fragment : Fragment? = null


            menuItem.isChecked = true
            drawer_layout.closeDrawers()
            val id = menuItem.itemId
            val title = menuItem.title.toString()
            if (id == R.id.t) {
                Toast.makeText(this, "$title: find", Toast.LENGTH_SHORT).show()
                fragment = FindFragment()
            } else if (id == R.id.tt) {
                Toast.makeText(this, "$title: 126.", Toast.LENGTH_SHORT).show()
                //fragment = A
            } else if (id == R.id.ttt) {
                Toast.makeText(this, "$title: 5465", Toast.LENGTH_SHORT).show()
            }

            supportFragmentManager.beginTransaction().replace(R.id.container_layout1,fragment!!).commit()
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
}