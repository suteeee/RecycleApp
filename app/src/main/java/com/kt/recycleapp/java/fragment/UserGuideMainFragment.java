package com.kt.recycleapp.java.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kt.recycleapp.kotlin.activity.MainActivity;
import com.kt.recycleapp.kotlin.fragment.MainFragment;

import java.recycleapp.R;


public class UserGuideMainFragment extends Fragment {

    private Button homeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_guide_main, container, false);

        ViewPager pager = rootView.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(6); //3개까지 caching

        UserGuidePagerAdapter adapter = new UserGuidePagerAdapter(getChildFragmentManager(), 1);    //suport가 아니다.

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



    /*
    * ************건드리지 마시오!************
    * *****아래 부분은 백버튼 구현 메소드******
    * ************건드리지 마시오!************
    * */
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)getActivity()).viewModel.getSelectedFragment().setValue("userGuide");
        ((MainActivity)getActivity()).viewModel.getFragmentStack().push("userGuide");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MainActivity act = ((MainActivity)getActivity());
        act.viewModel.getFragmentStack().pop();
        act.viewModel.getSelectedFragment().setValue(act.viewModel.getFragmentStack().peek());
        if(act.viewModel.getFragmentStack().peek().equals("main"))
            act.getSupportFragmentManager().beginTransaction().replace(R.id.small_layout1,new MainFragment()).commit();
    }

}