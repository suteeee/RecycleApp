 package com.kt.recycleapp.java.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kt.recycleapp.java.viewmodel.AnnounceRecyclePageViewModel;
import com.kt.recycleapp.kt.activity.MainActivity;
import com.kt.recycleapp.kt.activity.OnBackPressListener;
import com.kt.recycleapp.kt.fragment.MainFragment;

import org.jetbrains.annotations.NotNull;

import java.recycleapp.R;
import java.recycleapp.databinding.FragmentAnnounceRecyclePageBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

 public class AnnounceRecyclePageFragment extends Fragment implements OnBackPressListener {
    FragmentAnnounceRecyclePageBinding binding;
    AnnounceRecyclePageViewModel viewModel;
    Bundle bundle;
    String barcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //rootView는 액티비티를 나타냄 (container는 우리끼리 mainactivity레이아웃을 의미하는 것으로 약속)
        //아래 코드는 액티비티 자체를 가져오는 것이다

        //36~45번줄 뷰바인딩 및 전달받은 물품 이름 셋팅 하는 코드
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_announce_recycle_page, container, false);
        viewModel = new ViewModelProvider(this).get(AnnounceRecyclePageViewModel.class);
        ((MainActivity)getActivity()).viewModel.getToolbarText().setValue("분리수거 방법 안내");

        bundle = getArguments();

        if(bundle != null){
            String name = bundle.getString("item");
            barcode = bundle.getString("barcode");
            if(name == null){ viewModel.itemName = barcode; }
            else {viewModel.itemName = name; }

            binding.whatisTv2.setText(viewModel.itemName);

            //Log.d("것제발",barcode);
        }


        return binding.getRoot();

    }

    public void onBack() {
        MainActivity act = (MainActivity)getActivity();
        ((MainActivity) act).setOnBackPressListener(null);


        DialogFragment frg = new PopupFragmentAddpage();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                Set<String> key = new HashSet<>();
                Boolean haveBarcode = false;
                for(String x : key){
                    if(barcode.equals(x)){
                        haveBarcode = true;
                        break;
                    }
                }

                if(!haveBarcode){

                }
                else{


                }
            }
        });




        bundle.putString("barcode", barcode);
        frg.setArguments(bundle);

       frg.show(act.getSupportFragmentManager(), PopupFragmentStartpage.TAG_EVENT_DIALOG);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressListener(this);
    }
}
