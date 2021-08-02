package com.kt.recycleapp.java.fragment;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.recycleapp.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities;


public class RecycleDayInfoFragment extends Fragment {
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.KOREA);
    Calendar calendar = Calendar.getInstance();
    String weekDay = dateFormat.format(calendar.getTime());
    long tmp = System.currentTimeMillis() + (1000 * 60 * 60 * 24);
    String nextDay = dateFormat.format(tmp);




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycle_day_info, container, false);

        TextView textViewToday = rootView.findViewById(R.id.dayoftheweektoday_bt1);
        TextView textViewNextday = rootView.findViewById(R.id.dayoftheweektomorrow_bt1);

        textViewToday.setText("오늘은 " + weekDay + "입니다");
        textViewNextday.setText("내일은 " + nextDay + "입니다");





        return rootView;

    }
}