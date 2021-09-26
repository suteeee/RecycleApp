package com.kt.recycleapp.java.announce;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kt.recycleapp.java.fragment.PopupFragmentAddpage;
import com.kt.recycleapp.java.fragment.PopupFragmentStartpage;
import com.kt.recycleapp.kotlin.Internet;
import com.kt.recycleapp.kotlin.alert.AlertFragment;
import com.kt.recycleapp.kotlin.main.MainActivity;
import com.kt.recycleapp.kotlin.listener.OnBackPressListener;
import com.kt.recycleapp.kotlin.main.MainFragment;
import com.kt.recycleapp.kotlin.main.MainViewModel;
import com.kt.recycleapp.manager.MyPreferenceManager;

import java.recycleapp.R;
import java.recycleapp.databinding.AnnounceRecyclerFragmentBinding;

public class AnnounceRecyclerFragment extends Fragment implements OnBackPressListener {

    private AnnounceRecyclerViewModel mViewModel;
    private AnnounceRecyclerFragmentBinding binding;
    private AnnounceAdapter adapter;
    private Bundle bundle;
    String barcode;
    DialogFragment frg = new PopupFragmentAddpage();
    MainActivity act;
    MainViewModel viewModel;
    Boolean haveBarcode = true;
    Boolean isCapture = false;
    MyPreferenceManager prefs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.announce_recycler_fragment,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        mViewModel = new ViewModelProvider(this).get(AnnounceRecyclerViewModel.class);
        bundle = getArguments();
        barcode = bundle.getString("barcode");
        isCapture = bundle.getBoolean("capture");
        prefs = new MyPreferenceManager(requireContext());

        binding.setViewmodel(mViewModel);
        mViewModel.itemName.setValue(barcode);

        int iStatus = Internet.INSTANCE.getStatus(requireContext());

        if(iStatus == Internet.INSTANCE.getMOBILE_DATA() && !(prefs.getMobileInternetShow())) {
            act.showToast(getResources().getString(R.string.mobileData));
            prefs.setMobileInternetShow(true);
        }
        else if(iStatus == Internet.INSTANCE.getNOT_CONNECT()){
            act.showToast(getResources().getString(R.string.internetNotConnected));
        }


        mViewModel.checkBarcode(barcode);
        mViewModel.isHaveBarcode.observe(getViewLifecycleOwner(), it -> {
            if(it) {
                binding.doUploadBtn.setVisibility(View.INVISIBLE);
            }
        });

        mViewModel.itemName.observe(getViewLifecycleOwner(), s -> {
            if(!s.isEmpty()){
                if(binding.announcePb.getVisibility() != View.VISIBLE)binding.announcePb.setVisibility(View.VISIBLE);
                mViewModel.findProductKind(barcode);
            }
        });

        mViewModel.finding.observe(getViewLifecycleOwner(), s->{
            if(s.equals("finish")){
                haveBarcode = mViewModel.isHaveBarcode.getValue();
                mViewModel.setData(barcode);
                mViewModel.finding.setValue("waiting");
            }
        });

        mViewModel.setting.observe(getViewLifecycleOwner(), it -> {
            Log.d("Test",it);
            if(it.equals("finish")) {
                binding.announcePb.setVisibility(View.INVISIBLE);
                haveBarcode = mViewModel.isHaveBarcode.getValue();
                if(!haveBarcode) binding.doUploadBtn.setVisibility(View.VISIBLE);
                mViewModel.setting.setValue("wating");
            }
        });

        binding.doUploadBtn.setOnClickListener( v -> {
            bundle.putString("barcode", barcode);
            frg.setArguments(bundle);
            frg.show(act.getSupportFragmentManager(), PopupFragmentStartpage.TAG_EVENT_DIALOG);
        });
        return binding.getRoot();
    }

    @Override
    public void onBack() {

        mViewModel.checkBarcode(barcode);
        mViewModel.checkBarcodeFinish.observe(getViewLifecycleOwner(), res-> {
            if(res){
                Boolean check = act.viewModel.isPopup().getValue();

                Log.d("Main1",check.toString() + res.toString());
                if(act.viewModel.getSelectedFragment().getValue().equals("main") && check && !mViewModel.isHaveBarcode.getValue()){
                    Log.d("MainBack","5");
                    bundle.putString("barcode", barcode);
                    frg.setArguments(bundle);
                    frg.show(act.getSupportFragmentManager(), PopupFragmentStartpage.TAG_EVENT_DIALOG);
                    act.viewModel.isPopup().setValue(false);
                }else{
                    Log.d("MainBack","6");
                    act.setOnBackPressListener(null);
                    act.getSupportFragmentManager().beginTransaction().replace(R.id.small_layout1, new MainFragment()).commit();
                }
            }
        });
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        act = (MainActivity)getActivity();
        viewModel = act.viewModel;
        act.setOnBackPressListener(this);
    }


}