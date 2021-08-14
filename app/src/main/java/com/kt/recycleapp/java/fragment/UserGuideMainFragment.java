package com.kt.recycleapp.java.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.auth.User;
import com.kt.recycleapp.kt.activity.MainActivity;
import com.kt.recycleapp.kt.fragment.MainFragment;

import java.recycleapp.R;


public class UserGuideMainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_guide_main, container, false);

        ViewPager pager = rootView.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3); //3개까지 caching

        UserGuidePagerAdapter adapter = new UserGuidePagerAdapter(getActivity().getSupportFragmentManager(), 1);

        UserGuideFragment1 fragment1 = new UserGuideFragment1();
        adapter.addItem(fragment1);

        UserGuideFragment2 fragment2 = new UserGuideFragment2();
        adapter.addItem(fragment2);

        UserGuideFragment3 fragment3 = new UserGuideFragment3();
        adapter.addItem(fragment3);

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