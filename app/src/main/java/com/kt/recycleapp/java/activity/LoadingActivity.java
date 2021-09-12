package com.kt.recycleapp.java.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.kt.recycleapp.java.viewmodel.LodingViewModel;
import com.kt.recycleapp.kotlin.activity.MainActivity;
import com.kt.recycleapp.manager.MyPreferenceManager;
import com.kt.recycleapp.model.DatabaseReadModel;

import org.jetbrains.annotations.NotNull;

import java.recycleapp.R;

public class LoadingActivity extends AppCompatActivity {
    DatabaseReadModel model;
    private  String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int REQUEST_CODE_PERMISSIONS = 10;
    MyPreferenceManager prefs;
    int delay = 0;
    LodingViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        model = new DatabaseReadModel(getApplicationContext());
        prefs = new MyPreferenceManager(getApplicationContext()); //만들었던 preferenceManager를 쓸수있게 생성
        viewModel = new ViewModelProvider(this).get(LodingViewModel.class);

        if(prefs.getDarkmodSwitch()==false){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if(prefs.getDarkmodSwitch()==true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        if(!prefs.getCameraPermission().equals("GRANTED")){
            permissionCheck();
        }else {
            delay = 2000;
            loadingStart();
        }

        viewModel.getPermission().observe(this, it -> {
            loadingStart();
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void permissionCheck() {
        if (allPermissionsGranted()) {
            prefs.setCameraPermission("GRANTED");
            viewModel.getPermission().setValue("GRANTED");
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private boolean allPermissionsGranted()  {
        boolean result = true;
        for(String it : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(getApplicationContext(), it) != PackageManager.PERMISSION_GRANTED){
                result = false;
                break;
            }
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                try {
                    viewModel.getPermission().setValue("GRANTED");
                    prefs.setCameraPermission("GRANTED");
                }
                catch (Exception e){}
            } else {
                viewModel.getPermission().setValue("DENIED");
                prefs.setCameraPermission("DENIED");
            }
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