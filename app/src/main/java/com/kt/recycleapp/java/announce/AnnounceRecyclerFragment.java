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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.announce_recycler_fragment,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        mViewModel = new ViewModelProvider(this).get(AnnounceRecyclerViewModel.class);
        bundle = getArguments();
        barcode = bundle.getString("barcode");
        binding.setViewmodel(mViewModel);

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
            }
        });

        mViewModel.setting.observe(getViewLifecycleOwner(), s->{
            if(s.equals("finish")){

            }
        });

        mViewModel.itemName.setValue(barcode);


        return binding.getRoot();
    }

    @Override
    public void onBack() {
        MainActivity act = (MainActivity)getActivity();

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

        Boolean check = ((MainActivity) getActivity()).viewModel.isPopup().getValue();

        Log.d(check.toString(),"ffff");
        if(act.viewModel.getSelectedFragment().getValue().equals("main") && check){
            Log.d("dd","ffff");
            bundle.putString("barcode", barcode);
            DialogFragment frg = new PopupFragmentAddpage();
            frg.setArguments(bundle);
            frg.show(act.getSupportFragmentManager(), PopupFragmentStartpage.TAG_EVENT_DIALOG);
            ((MainActivity)getActivity()).viewModel.isPopup().setValue(false);
        }
        else{
            act.setOnBackPressListener(null);
            ((MainActivity)getActivity()).viewModel.isPopup().setValue(true);
            FragmentTransaction t = act.getSupportFragmentManager().beginTransaction();
            if(act.viewModel.getSelectedFragment().getValue().equals("main")){
                t.remove(this).commit();
                t.add(R.id.small_layout1,new MainFragment());
            }
            else {
                ((MainActivity)getActivity()).viewModel.isPopup().setValue(true);
                t.remove(this).commit();
            }
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressListener(this);
    }
}