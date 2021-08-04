package com.kt.recycleapp.java.fragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kt.recycleapp.manager.MyPreferenceManager;

import java.recycleapp.R;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PopupFragmentAnnouncepage extends DialogFragment implements  View.OnClickListener {
    public static final String TAG_EVENT_DIALOG = "testtest";
    private Button buttonClose;

    public PopupFragmentAnnouncepage(){

    }

    public static PopupFragmentAnnouncepage getInstance(){
        PopupFragmentAnnouncepage popup = new PopupFragmentAnnouncepage();
        return popup;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popup_announcepage, container, false);
        buttonClose = rootView.findViewById(R.id.close_bt2);

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();  //화면닫기
            }
        });


        //메인엑티비티에 박아야할 코드(코틀린으로 변환해놨음)
        //PopupFragment popup = PopupFragment.getInstance();
        //popup.show(getSupportFragmentManager()),PopupFragment.TAG_EVENT_DIALOG);


        return rootView;
    }

    @Override
    public void onClick(View view) {

    }

}