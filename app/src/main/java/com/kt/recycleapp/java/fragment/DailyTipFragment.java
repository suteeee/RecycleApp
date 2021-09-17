package com.kt.recycleapp.java.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kt.recycleapp.kotlin.main.MainActivity;
import com.kt.recycleapp.kotlin.main.MainFragment;

import java.recycleapp.R;
import java.util.HashSet;
import java.util.Set;


public class DailyTipFragment extends Fragment {
    private TextView textView;
    private ImageView imageView;
    private Button button;
    private int[] images = {0, R.drawable.tip1,R.drawable.tip2,R.drawable.tip3,R.drawable.tip4,R.drawable.tip5,
            R.drawable.tip6,R.drawable.tip7,R.drawable.tip8,R.drawable.tip9,R.drawable.tip10,
            R.drawable.tip11,R.drawable.tip12,R.drawable.tip13,R.drawable.tip14,R.drawable.tip15,
            R.drawable.tip16,R.drawable.tip17,R.drawable.tip18,R.drawable.tip19,R.drawable.tip20,
            R.drawable.tip21,R.drawable.tip22,R.drawable.tip23,R.drawable.tip24,R.drawable.tip25,
            R.drawable.tip26,R.drawable.tip27,R.drawable.tip28,R.drawable.tip29,R.drawable.tip30};



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //rootView는 액티비티를 나타냄 (container는 우리끼리 mainactivity레이아웃을 의미하는 것으로 약속)
        //아래 코드는 액티비티 자체를 가져오는 것이다
        View rootView = inflater.inflate(R.layout.fragment_daily_tip, container, false);
        //((MainActivity)getActivity()).viewModel.getToolbarText().setValue("오늘의 팁");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        textView = (TextView)rootView.findViewById(R.id.todaytip_tv1);
        imageView = (ImageView)rootView.findViewById(R.id.tippicture_iv1);
        button = (Button)rootView.findViewById(R.id.nexttip_bt1);

        button.setOnClickListener(new View.OnClickListener(){
            @Override   //ctrl+o를 누른다
            public void onClick(View view) {
                db.collection("recycleApp").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override   //성공하면 불러온다
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //랜덤한 데이터를 하나 가져온다x
                        int k = (int)(Math.random()*29)+1;
                        if(task.isSuccessful()){
                            Set<String> tmp = new HashSet<>();  //keyset 저장용
                            QueryDocumentSnapshot doc = null;   //value를 가져오기 위한 것
                            for(QueryDocumentSnapshot document : task.getResult()) {    //docunment가 하나이므로 한번만
                                tmp=document.getData().keySet();    //set에 keyset넣음
                                doc=document;   //value 저장(values해야하는거 아닌가?)
                            }

                            String arr[] = doc.getData().values().toArray(new String[0]); //배열로 전환(0 넣으면 전체가 옴)

                            int target = 0;
                            for(String i : tmp){
                                target++;
                                if(i.equals(Integer.toString(k))) {
                                    break;
                                }
                            }

                            textView.setText(arr[target-1]);
                            imageView.setImageResource(images[k]);
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




    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)getActivity()).viewModel.getSelectedFragment().setValue("tip");
        ((MainActivity)getActivity()).viewModel.getFragmentStack().push("tip");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MainActivity act = ((MainActivity)getActivity());
        act.viewModel.getFragmentStack().pop();
        act.viewModel.getSelectedFragment().setValue(act.viewModel.getFragmentStack().peek());
        if(act.viewModel.getFragmentStack().peek().equals("main"))
            act.getSupportFragmentManager().beginTransaction().replace(R.id.small_layout1,new MainFragment()).commit();
    }
}