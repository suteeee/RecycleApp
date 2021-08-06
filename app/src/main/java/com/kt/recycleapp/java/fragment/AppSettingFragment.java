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
        ((MainActivity)getActivity()).viewModel.getToolbarText().setValue("환경 설정");

        seeAnnounceButton = (Button) rootView.findViewById(R.id.announce_bt1);
        darkmodSwitch = (Switch) rootView.findViewById(R.id.darkmod_sch1);

        //prefs.onDarkMode가   true라면  darkmodSwitch.setChecked(true);
        //prefs.onDarkMode가  false라면 darkmodSwitch.setChecked(false);



        seeAnnounceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupFragmentAnnouncepage popup = PopupFragmentAnnouncepage.getInstance();
                //이 프레그먼트에 엑티비티를 띄운다
                popup.show(getActivity().getSupportFragmentManager(),PopupFragmentAnnouncepage.TAG_EVENT_DIALOG);
            }
        });

        //앱 재실행시 초기화되는데 이건 흠..
        darkmodSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사
                if (isChecked){ //버튼이 체크 됬다면
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    darkmodSwitch.setChecked(true); //굳이 필요할까? 왜냐면 꺼진상태에서 켜진상태가 되면서 이 메소드가 실행 되는건데 다시한번 더 true로 해주는거임
                    Toast.makeText(rootView.getContext(), "다크모드 활성화", Toast.LENGTH_SHORT).show();

                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    darkmodSwitch.setChecked(false);//굳이 필요할까? 왜냐면 꺼진상태에서 켜진상태가 되면서 이 메소드가 실행 되는건데 다시한번 더 true로 해주는거임
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

