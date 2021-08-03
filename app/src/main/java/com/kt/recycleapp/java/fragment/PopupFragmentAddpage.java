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


public class PopupFragmentAddpage extends DialogFragment implements  View.OnClickListener{
    public static final String TAG_EVENT_DIALOG = "dialog_event";


    public PopupFragmentAddpage(){

    }

    public static PopupFragmentAddpage getInstance(){
        PopupFragmentAddpage popup = new PopupFragmentAddpage();
        return popup;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popup_addpage, container, false);
        MyPreferenceManager prefs = new MyPreferenceManager(requireContext()); //만들었던 preferenceManager를 쓸수있게 생성




        //메인엑티비티에 박아야할 코드(코틀린으로 변환해놨음)
        //PopupFragment popup = PopupFragment.getInstance();
        //popup.show(getSupportFragmentManager()),PopupFragment.TAG_EVENT_DIALOG);


        return rootView;
    }

    @Override
    public void onClick(View view) {

    }
}