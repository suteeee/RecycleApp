package com.kt.recycleapp.java.userguide;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kt.recycleapp.java.userguide.fragments.UserGuideFragment;
import com.kt.recycleapp.kotlin.main.MainActivity;
import com.kt.recycleapp.kotlin.main.MainFragment;

import java.recycleapp.R;
import java.util.ArrayList;


public class UserGuideMainFragment extends Fragment {

    private GuideViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_guide_main, container, false);
        viewModel = new ViewModelProvider(this).get(GuideViewModel.class);


        ViewPager pager = rootView.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(10); //3개까지 caching

        viewModel.initList();

        UserGuidePagerAdapter adapter = new UserGuidePagerAdapter(getChildFragmentManager(), 1);    //suport가 아니다.

        for(Fragment f : viewModel.getFrgList()) {
            adapter.addItem(f);
        }

        pager.setAdapter(adapter);

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