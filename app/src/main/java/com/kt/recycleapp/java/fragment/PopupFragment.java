package com.kt.recycleapp.java.fragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kt.recycleapp.manager.MyPreferenceManager;
import com.kt.recycleapp.manager.Prefs;

import java.recycleapp.R;


public class PopupFragment extends DialogFragment implements  View.OnClickListener{
    public static final String TAG_EVENT_DIALOG = "dialog_event";

    public PopupFragment(){

    }

    public static PopupFragment getInstance(){
        PopupFragment popup = new PopupFragment();
        return popup;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_popup, container, false);
        Button buttonFirst = rootView.findViewById(R.id.onedaynotsee_bt1);
        Button buttonSecond = rootView.findViewById(R.id.neversee_bt1);
        Log.d(rootView.getContext().toString(),"123456");

        MyPreferenceManager prefs = new MyPreferenceManager(getContext()); //만들었던 preferenceManager를 쓸수있게 생성
        //Prefs prefs = new Prefs(getContext());

        buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //코드 작성(따로 메소드 빼야할 듯)
            }
        });

        buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //코드 작성(따로 메소드 빼야할 듯)
                prefs.setIsNeverShow(true); //설정값 영구지속


            }
        });

        //메인엑티비티에 박아야할 코드
        //PopupFragment popup = PopupFragment.getInstance();
        //popup.show(getSupportFragmentManager()),PopupFragment.TAG_EVENT_DIALOG);


        return rootView;
    }

    @Override
    public void onClick(View view) {

    }
}
