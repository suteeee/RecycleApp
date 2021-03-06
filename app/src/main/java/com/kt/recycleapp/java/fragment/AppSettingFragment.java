package com.kt.recycleapp.java.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kt.recycleapp.kotlin.main.MainActivity;
import com.kt.recycleapp.kotlin.alert.AlertFragment;
import com.kt.recycleapp.kotlin.upload.ImageUploadFragment;
import com.kt.recycleapp.kotlin.main.MainFragment;
import com.kt.recycleapp.kotlin.main.MainViewModel;
import com.kt.recycleapp.manager.MyPreferenceManager;
import com.kt.recycleapp.model.RoomHelper;

import org.jetbrains.annotations.NotNull;

import java.recycleapp.R;

public class AppSettingFragment extends Fragment{
    private Button seeAnnounceButton;
    //private Switch darkmodSwitch;
    private Button darkmodButton;
    private Button uploadButton;
    private Button historyDeleteBtn;
    MainActivity act;
    MainViewModel actViewModel;
    MyPreferenceManager prefs;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        @SuppressLint("ResourceType")
        View rootView = inflater.inflate(R.layout.fragment_app_setting, container, false);
        //((MainActivity)getActivity()).viewModel.getToolbarText().setValue("환경 설정");

        seeAnnounceButton = (Button) rootView.findViewById(R.id.announce_bt1);
        //darkmodSwitch = (Switch) rootView.findViewById(R.id.darkmod_sch1);
        darkmodButton = (Button) rootView.findViewById(R.id.darkmodonoff_bt1);
        uploadButton = rootView.findViewById(R.id.imageUpload_btn1);
        historyDeleteBtn = rootView.findViewById(R.id.historyDelete_btn);


        prefs = new MyPreferenceManager(requireContext()); //만들었던 preferenceManager를 쓸수있게 생성

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


        //이미지업로드
        uploadButton.setOnClickListener(v -> {
            ImageUploadFragment imageUploadFragment = new ImageUploadFragment();
            imageUploadFragment.show(requireActivity().getSupportFragmentManager(),null);
        });


        //히스토리 초기화
        historyDeleteBtn.setOnClickListener( v-> {
            historyClean();
        });


        return rootView;
    }

    public void historyClean() {
        RoomHelper helper = RoomHelper.Companion.getInstance(requireContext());
        helper.databaseDao().deteteAll();
        prefs.setSQLiteIndex(0);

        AlertFragment.Companion.showAlert(act,"HistoryDeleteSuccess",true);
    }

    /*
    * 여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다
    * 여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다
    * 여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다
    * */
    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int nightmode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightmode){
            case Configuration.UI_MODE_NIGHT_NO : {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            }
            case Configuration.UI_MODE_NIGHT_YES : {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            }
        }

        try {
            Intent intent = act.getIntent();
            intent.putExtra("darkModeRefresh",0);
            act.finish();
            act.startActivity(intent); //현재 액티비티 재실행 실시
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    /*
     * 여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다
     * 여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다
     * 여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다여기 보시면 됩니다
     * */


    public void onAttach(Context context) {
        super.onAttach(context);
        act = ((MainActivity)getActivity());
        actViewModel = act.viewModel;
        actViewModel.getSelectedFragment().setValue("setting");
        actViewModel.getFragmentStack().push("setting");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        actViewModel.getFragmentStack().pop();
        actViewModel.getSelectedFragment().setValue(actViewModel.getFragmentStack().peek());
        if(actViewModel.getFragmentStack().peek().equals("main"))
            act.replaceFragmentWithCommitAllowingStateLoss(new MainFragment());
    }
}

