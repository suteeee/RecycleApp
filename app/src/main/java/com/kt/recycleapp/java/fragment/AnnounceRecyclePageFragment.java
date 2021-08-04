 package com.kt.recycleapp.java.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kt.recycleapp.java.viewmodel.AnnounceRecyclePageViewModel;
import com.kt.recycleapp.kt.activity.MainActivity;
import com.kt.recycleapp.kt.activity.OnBackPressListener;
import com.kt.recycleapp.kt.fragment.MainFragment;

import java.recycleapp.R;
import java.recycleapp.databinding.FragmentAnnounceRecyclePageBinding;

public class AnnounceRecyclePageFragment extends Fragment implements OnBackPressListener {
    FragmentAnnounceRecyclePageBinding binding;
    AnnounceRecyclePageViewModel viewModel;
    Bundle bundle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //rootView는 액티비티를 나타냄 (container는 우리끼리 mainactivity레이아웃을 의미하는 것으로 약속)
        //아래 코드는 액티비티 자체를 가져오는 것이다

        //36~45번줄 뷰바인딩 및 전달받은 물품 이름 셋팅 하는 코드
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_announce_recycle_page, container, false);
        viewModel = new ViewModelProvider(this).get(AnnounceRecyclePageViewModel.class);

        bundle = getArguments();

        if(bundle != null){
            viewModel.itemName = bundle.getString("item");
            Log.d(viewModel.itemName,"bundle");
            binding.whatisTv2.setText(viewModel.itemName);
        }


        return binding.getRoot();

    }

    public void onBack() {
        MainActivity act = (MainActivity)getActivity();
        ((MainActivity) act).setOnBackPressListener(null);
        Log.d("back","것");

        DialogFragment frg = new PopupFragmentAddpage();
        String barcode = bundle.getString("barcode");

        Log.d(barcode,"것");

        if(barcode == null){
            barcode = "준";
        }

        bundle.putString("barcode", barcode);
        frg.setArguments(bundle);

       frg.show(act.getSupportFragmentManager(),
               PopupFragmentStartpage.TAG_EVENT_DIALOG);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressListener(this);
    }
}
