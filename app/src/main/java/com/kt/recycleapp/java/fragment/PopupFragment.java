package com.kt.recycleapp.java.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kt.recycleapp.kt.activity.OnBackPressListener;

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
