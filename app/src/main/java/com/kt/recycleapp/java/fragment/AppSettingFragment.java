package com.kt.recycleapp.java.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

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

