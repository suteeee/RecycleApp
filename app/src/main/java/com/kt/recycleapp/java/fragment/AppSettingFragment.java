package com.kt.recycleapp.java.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.recycleapp.R;

public class AppSettingFragment extends PreferenceFragmentCompat {
    SharedPreferences pref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_app_setting, rootKey);

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());    //잘 모르겠으나 SharedPreference객체 참조하여 설정상태 제어
        boolean soundButton = pref.getBoolean("soundcontrol1", true);   //key값 찾아서 default값체크



    }
}