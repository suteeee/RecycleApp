package com.kt.recycleapp.java.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

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

import java.recycleapp.R;

public class AppSettingFragment extends Fragment implements OnBackPressListener {
    private Button seeAnnounceButton;
    private Switch darkmodSwitch;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        @SuppressLint("ResourceType")
        View rootView = inflater.inflate(R.layout.fragment_app_setting, container, false);

        seeAnnounceButton = (Button) rootView.findViewById(R.id.announce_bt1);
        darkmodSwitch = (Switch) rootView.findViewById(R.id.darkmod_sch1);

        seeAnnounceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupFragmentAnnouncepage popup = PopupFragmentAnnouncepage.getInstance();
                //이 프레그먼트에 엑티비티를 띄운다
                popup.show(getActivity().getSupportFragmentManager(),PopupFragmentAnnouncepage.TAG_EVENT_DIALOG);
            }
        });

        //다크모드하면 앱이 자꾸 재실행 됨 이를 해결해야함
        darkmodSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사
                if (isChecked){ //버튼이 체크 됬다면
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    darkmodSwitch.setChecked(true);
                    Toast.makeText(rootView.getContext(), "다크모드 활성화", Toast.LENGTH_SHORT).show();

                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    darkmodSwitch.setChecked(false);
                    Toast.makeText(rootView.getContext(), "주간모드 활성화", Toast.LENGTH_SHORT).show();
                }
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

