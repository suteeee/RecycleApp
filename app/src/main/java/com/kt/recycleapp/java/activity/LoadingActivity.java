package com.kt.recycleapp.java.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.kt.recycleapp.kotlin.activity.MainActivity;
import com.kt.recycleapp.manager.MyPreferenceManager;

import java.recycleapp.R;

public class LoadingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        MyPreferenceManager prefs = new MyPreferenceManager(getApplicationContext()); //만들었던 preferenceManager를 쓸수있게 생성

        if(prefs.getDarkmodSwitch()==false){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if(prefs.getDarkmodSwitch()==true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        loadingStart();

    }


    private void loadingStart(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                //startActivity(new Intent(현재Activity.this, 불러올Activity.class));
                //overridePendingTransition(R.anim.현재(사라질)Activity애니메이션, R.anim.현재(사라질)Activity애니메이션);

                //Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                //startActivity(intent);
                // overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
                finish();
            }
        }, 2000);
    }
}