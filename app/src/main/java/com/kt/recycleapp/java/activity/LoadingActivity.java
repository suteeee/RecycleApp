package com.kt.recycleapp.java.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.annotations.NotNull;
import com.kt.recycleapp.kotlin.activity.MainActivity;
import com.kt.recycleapp.manager.MyPreferenceManager;
import com.kt.recycleapp.model.DatabaseReadModel;

import java.recycleapp.R;

public class LoadingActivity extends AppCompatActivity {
    DatabaseReadModel model;
    private  String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int REQUEST_CODE_PERMISSIONS = 10;
    MyPreferenceManager prefs;
    int delay = 2000;
    MutableLiveData<Boolean> startFlag = new MutableLiveData<>(false);

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        model = DatabaseReadModel.Companion.getInstance();
        prefs = new MyPreferenceManager(getApplicationContext()); //만들었던 preferenceManager를 쓸수있게 생성



        if(prefs.getDarkmodSwitch()==false){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if(prefs.getDarkmodSwitch()==true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        startFlag.observe(this, flag -> {
            if(flag) {
                startFlag.setValue(false);
                loadingStart();
            }
        });

        permissionCheck();
    }

    private boolean allPermissionsGranted()  {
        boolean result = true;
        for(String it : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(getApplicationContext(), it) != PackageManager.PERMISSION_GRANTED){
                if(it.equals(Manifest.permission.CAMERA)) {
                    prefs.setCameraPermission("DENIED");
                }else {
                    prefs.setStoragePermission("DENIED");
                }
                result = false;
            }
            else {

                if(it.equals(Manifest.permission.CAMERA)) {
                    prefs.setCameraPermission("GRANTED");
                }else {
                    prefs.setStoragePermission("GRANTED");
                }
            }
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void permissionCheck() {
        if (allPermissionsGranted()) {
            Log.d("per","0");
            prefs.setCameraPermission("GRANTED");

            startFlag.setValue(true);
            //loadingStart();
        } else {
            Log.d("per","1");
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            //loadingStart();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            Log.d("per","2");

            prefsPermissionSetAndLoading(requestCode, grantResults);
        }catch (Exception e) {
            Log.d("per","3");
            startFlag.setValue(false);
           //loadingStart();
        }
    }

    private void prefsPermissionSetAndLoading(int requestCode, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("per","4");
                prefs.setCameraPermission("GRANTED");
            } else {
                Log.d("per","5");
                prefs.setCameraPermission("DENIED");
            }

            if (grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Log.d("per","6");
                prefs.setStoragePermission("GRANTED");
            } else {
                Log.d("per","7");
                prefs.setStoragePermission("DENIED");
            }
            Log.d("per","8");
            loadingStart();
        }
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
        }, delay);
    }
}