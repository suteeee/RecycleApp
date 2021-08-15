package com.kt.recycleapp.java.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.auth.User;

import java.recycleapp.R;


public class UserGuideMainFragment extends Fragment {

    private Button homeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_guide_main, container, false);

        ViewPager pager = rootView.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(6); //3개까지 caching

        UserGuidePagerAdapter adapter = new UserGuidePagerAdapter(getActivity().getSupportFragmentManager(), 1);

        UserGuideFragment1 fragment1 = new UserGuideFragment1();
        adapter.addItem(fragment1);

        UserGuideFragment2 fragment2 = new UserGuideFragment2();
        adapter.addItem(fragment2);

        UserGuideFragment3 fragment3 = new UserGuideFragment3();
        adapter.addItem(fragment3);

        UserGuideFragment4 fragment4 = new UserGuideFragment4();
        adapter.addItem(fragment4);

        UserGuideFragment5 fragment5 = new UserGuideFragment5();
        adapter.addItem(fragment5);

        UserGuideFragment6 fragment6 = new UserGuideFragment6();
        adapter.addItem(fragment6);

        pager.setAdapter(adapter);
        homeButton = (Button) rootView.findViewById(R.id.gotohome_bt1);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(0);
            }
        });



        return rootView;
    }
}