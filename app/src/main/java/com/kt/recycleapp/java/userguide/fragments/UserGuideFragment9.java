package com.kt.recycleapp.java.userguide.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kt.recycleapp.java.userguide.GuideViewModel;

import java.recycleapp.R;
import java.util.ArrayList;


public class UserGuideFragment9 extends Fragment {
    private TextView count;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_guide9, container, false);

        count = (TextView) rootView.findViewById(R.id.count_bt9);
        Spannable span1 = (Spannable) count.getText();
        Spannable span2 = (Spannable) count.getText();
        span1.setSpan(new ForegroundColorSpan(Color.GRAY), 0,1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        span2.setSpan(new ForegroundColorSpan(Color.parseColor("#FE9A2E")), 1, 2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        ImageView img = rootView.findViewById(R.id.infoImg_9);
        Glide.with(img).load(R.drawable.g9).override(500).into(img);

        return rootView;
    }
}