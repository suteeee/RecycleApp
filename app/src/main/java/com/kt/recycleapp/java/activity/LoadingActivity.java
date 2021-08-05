package com.kt.recycleapp.java.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.kt.recycleapp.kt.activity.MainActivity;
import com.kt.recycleapp.model.DatabaseReadModel;
import com.kt.recycleapp.model.MyRoomDatabase;
import com.kt.recycleapp.model.RoomHelper;

import java.recycleapp.R;
import java.util.ArrayList;
import java.util.List;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        imageLoad();
        loadingStart();
    }

    private void imageLoad() {
        DatabaseReadModel databaseReadModel = new DatabaseReadModel();
        RoomHelper helper = Room.databaseBuilder(getApplicationContext(),RoomHelper.class,"Database").allowMainThreadQueries().build();
        List<MyRoomDatabase> data = helper.databaseDao().getAll();

       for(int i = 0; i < data.size(); i++){
           String image = data.get(i).getImage();
           databaseReadModel.decode(this,image);
       }


    }

    private void loadingStart(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}