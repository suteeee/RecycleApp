package com.kt.recycleapp.java.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.kt.recycleapp.kt.activity.MainActivity;
import com.kt.recycleapp.kt.activity.OnBackPressListener;
import com.kt.recycleapp.kt.fragment.MainFragment;
import com.kt.recycleapp.manager.MyPreferenceManager;

import java.recycleapp.R;

public class AppSettingFragment extends Fragment{
    private Button seeAnnounceButton;
    //private Switch darkmodSwitch;
    private Button darkmodButton;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        @SuppressLint("ResourceType")
        View rootView = inflater.inflate(R.layout.fragment_app_setting, container, false);
        ((MainActivity)getActivity()).viewModel.getToolbarText().setValue("환경 설정");

        seeAnnounceButton = (Button) rootView.findViewById(R.id.announce_bt1);
        //darkmodSwitch = (Switch) rootView.findViewById(R.id.darkmod_sch1);
        darkmodButton = (Button) rootView.findViewById(R.id.darkmodonoff_bt1);


        MyPreferenceManager prefs = new MyPreferenceManager(requireContext()); //만들었던 preferenceManager를 쓸수있게 생성

        if(prefs.getDarkmodSwitch()==false){
            darkmodButton.setText("OFF");
        }
        else if(prefs.getDarkmodSwitch()==true){
            darkmodButton.setText("ON");
        }

        seeAnnounceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupFragmentAnnouncepage popup = PopupFragmentAnnouncepage.getInstance();
                //이 프레그먼트에 엑티비티를 띄운다
                popup.show(getActivity().getSupportFragmentManager(),PopupFragmentAnnouncepage.TAG_EVENT_DIALOG);
            }
        });

        darkmodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prefs.getDarkmodSwitch()==false){
                    prefs.setDarkmodSwitch(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Toast.makeText(rootView.getContext(), "다크모드 활성화", Toast.LENGTH_SHORT).show();
                }
                else if(prefs.getDarkmodSwitch()==true){
                    prefs.setDarkmodSwitch(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Toast.makeText(rootView.getContext(), "주간모드 활성화", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* 스위치 왜안될까...
        darkmodSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사

                if(prefs.getDarkmodSwitch()==true){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Toast.makeText(rootView.getContext(), "다크모드 활성화", Toast.LENGTH_SHORT).show();
                    prefs.setDarkmodSwitch(false);
                    darkmodSwitch.setChecked(true); //여기에서 팅김
                }
                else if(prefs.getDarkmodSwitch()==false){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Toast.makeText(rootView.getContext(), "주간모드 활성화", Toast.LENGTH_SHORT).show();
                    prefs.setDarkmodSwitch(true);
                    darkmodSwitch.setChecked(false);

                }
            }
        });
        */





        return rootView;
    }


    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)getActivity()).viewModel.getSelectedFragment().setValue("setting");
        ((MainActivity)getActivity()).viewModel.getFragmentStack().push("setting");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity)getActivity()).viewModel.getFragmentStack().pop();
        ((MainActivity)getActivity()).viewModel.getSelectedFragment().setValue(
                ((MainActivity)getActivity()).viewModel.getFragmentStack().peek());
    }
}

