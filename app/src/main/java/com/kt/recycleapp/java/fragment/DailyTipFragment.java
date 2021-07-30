package com.kt.recycleapp.java.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.ktx.Firebase;
import com.kt.recycleapp.kt.activity.MainActivity;
import com.kt.recycleapp.kt.activity.OnBackPressListener;
import com.kt.recycleapp.kt.fragment.MainFragment;

import org.jetbrains.annotations.NotNull;

import java.recycleapp.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class DailyTipFragment extends Fragment implements OnBackPressListener {
    private TextView textView;
    private Button button;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //rootView는 액티비티를 나타냄 (container는 우리끼리 mainactivity레이아웃을 의미하는 것으로 약속)
        //아래 코드는 액티비티 자체를 가져오는 것이다
        View rootView = inflater.inflate(R.layout.fragment_daily_tip, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        textView = (TextView)rootView.findViewById(R.id.todaytip_tv1);
        button = (Button)rootView.findViewById(R.id.nexttip_bt1);

        button.setOnClickListener(new View.OnClickListener(){
            @Override   //ctrl+o를 누른다
            public void onClick(View view) {
                db.collection("recycleApp").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override   //성공하면 불러온다
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //랜덤한 데이터를 하나 가져온다
                        int k = (int)(Math.random()*30);
                        if(task.isSuccessful()){
                            Set<String> tmp = new HashSet<>();
                            //for(QueryDocumentSnapshot document : task.getResult()){ //문서 전체 가져옴, //document("")를 했기 때문에 할 필요 없어짐
                            for(QueryDocumentSnapshot document : task.getResult()) {    //docunment가 하나이므로 한번만 돔
                                tmp=document.getData().keySet();    //즉 한번에 keySet 다가져옴
                            }



                            //test

                            //그걸 todaytip_tv1 의 text를 변경한다.
                            //textView.setText();
                        }
                        else{
                            Toast.makeText(getContext(), "에러 발생!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return rootView;
    }




    public void onBack() {
        MainActivity act = (MainActivity)getActivity();
        ((MainActivity) act).setOnBackPressListener(null);
        act.getSupportFragmentManager().beginTransaction().replace(R.id.small_layout1,new MainFragment()).commit();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressListener(this);
    }


}