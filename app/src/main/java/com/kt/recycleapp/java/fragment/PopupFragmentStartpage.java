package com.kt.recycleapp.java.fragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kt.recycleapp.manager.MyPreferenceManager;

import java.recycleapp.R;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PopupFragmentStartpage extends DialogFragment implements  View.OnClickListener{
    public static final String TAG_EVENT_DIALOG = "dialog_event";


    public PopupFragmentStartpage(){

    }

    public static PopupFragmentStartpage getInstance(){
        PopupFragmentStartpage popup = new PopupFragmentStartpage();
        return popup;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popup_startpage, container, false);
        Button buttonFirst = rootView.findViewById(R.id.onedaynotsee_bt1);
        Button buttonSecond = rootView.findViewById(R.id.neversee_bt1);
        Button buttonThird = rootView.findViewById(R.id.close_bt1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");


        MyPreferenceManager prefs = new MyPreferenceManager(requireContext()); //만들었던 preferenceManager를 쓸수있게 생성

        //Log.d(String(dateFormat.format(System.currentTimeMillis())), "test1");
       // Log.d((prefs.getStoredTime()), "test2");
        String dateToStr=dateFormat.format(System.currentTimeMillis());

        if(!dateToStr.equals(prefs.getStoredTime())){    //하루 내에 재클릭 확인여부로 다음 날짜일때

            if(prefs.getIsNeverShow()==true){   //다시보지않기 선택햇을 경우
                dismiss();
            }

            buttonFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long now = System.currentTimeMillis();
                    Date current = new Date(now);
                    String clickTime = dateFormat.format(current);
                    prefs.setStoredTime(clickTime);
                    dismiss();  //화면닫기
                }
            });

            buttonSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prefs.setIsNeverShow(true); //설정값 영구지속
                    dismiss();  //화면닫기
                }
            });

            buttonThird.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();  //화면닫기
                }
            });
        }
        else{      //같은날 일 때 안뜨게함
            dismiss();
        }



        //메인엑티비티에 박아야할 코드(코틀린으로 변환해놨음)
        //PopupFragment popup = PopupFragment.getInstance();
        //popup.show(getSupportFragmentManager()),PopupFragment.TAG_EVENT_DIALOG);


        return rootView;
    }

    @Override
    public void onClick(View view) {

    }
}
