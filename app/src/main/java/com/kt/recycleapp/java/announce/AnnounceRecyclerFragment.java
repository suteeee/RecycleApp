package com.kt.recycleapp.java.announce;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kt.recycleapp.java.fragment.PopupFragmentAddpage;
import com.kt.recycleapp.java.fragment.PopupFragmentStartpage;
import com.kt.recycleapp.kotlin.activity.MainActivity;
import com.kt.recycleapp.kotlin.activity.OnBackPressListener;
import com.kt.recycleapp.kotlin.fragment.MainFragment;
import com.kt.recycleapp.kotlin.viewmodel.MainViewModel;

import org.jetbrains.annotations.NotNull;

import java.recycleapp.R;
import java.recycleapp.databinding.AnnounceRecyclerFragmentBinding;
import java.util.HashSet;
import java.util.Set;

public class AnnounceRecyclerFragment extends Fragment implements OnBackPressListener {

    private AnnounceRecyclerViewModel mViewModel;
    private AnnounceRecyclerFragmentBinding binding;
    private AnnounceAdapter adapter;
    private Bundle bundle;
    String barcode;
    DialogFragment frg = new PopupFragmentAddpage();
    MainActivity act;
    Boolean haveBarcode;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.announce_recycler_fragment,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        mViewModel = new ViewModelProvider(this).get(AnnounceRecyclerViewModel.class);
        bundle = getArguments();
        barcode = bundle.getString("barcode");
        binding.setViewmodel(mViewModel);
        mViewModel.itemName.setValue(barcode);
        act = (MainActivity)getActivity();

        mViewModel.checkBarcode(barcode);
        mViewModel.isHaveBarcode.observe(getViewLifecycleOwner(), it -> {
            haveBarcode = it;
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
                Log.d("fff",mViewModel.kind.getValue());
                mViewModel.setData(barcode);
                binding.announcePb.setVisibility(View.INVISIBLE);
                mViewModel.finding.setValue("waiting");
                if(!haveBarcode) binding.doUploadBtn.setVisibility(View.VISIBLE);
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
        ((MainActivity)context).setOnBackPressListener(this);
    }


}