package com.kt.recycleapp.java.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kt.recycleapp.kt.activity.MainActivity;
import com.kt.recycleapp.kt.activity.OnBackPressListener;
import com.kt.recycleapp.kt.fragment.MainFragment;
import com.kt.recycleapp.manager.MyPreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.recycleapp.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class PopupFragmentAddpage extends DialogFragment implements  View.OnClickListener, OnBackPressListener {
    private EditText writeBarcode;
    private EditText writeProductName;
    private Button saveButton;
    private String sendBarcode;


    public PopupFragmentAddpage(){

    }



    public static PopupFragmentAddpage getInstance(){
        PopupFragmentAddpage popup = new PopupFragmentAddpage();
        return popup;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popup_addpage, container, false);
        MyPreferenceManager prefs = new MyPreferenceManager(requireContext()); //만들었던 preferenceManager를 쓸수있게 생성

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        writeBarcode = (EditText) rootView.findViewById(R.id.inputBarcode_et1);
        writeProductName = (EditText) rootView.findViewById(R.id.inputProductName_et1);
        saveButton = (Button) rootView.findViewById(R.id.askYes_bt1);


        Bundle bundle = getArguments();

        if(bundle != null){
            sendBarcode = bundle.getString("barcode");
        }



        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                db.collection("recycleApp").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        //key에 바코드 저장하고 value에 물품명 저장하자

                        if(task.isSuccessful()){
                            Map<String, Object> products = new HashMap<>();
                            products.put(sendBarcode, writeProductName.toString());
                            Toast.makeText(rootView.getContext(), "상품이 등록되었습니다!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(rootView.getContext(), "등록실패, 다시입력하세요", Toast.LENGTH_SHORT).show();
                        }


                        /*
                        db.collection("cities").document("LA")
                                .set(products)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(rootView.getContext(), "상품이 등록되었습니다!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(rootView.getContext(), "등록실패", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        */

                    }
                });
            }
        });



        //메인엑티비티에 박아야할 코드(코틀린으로 변환해놨음)
        //PopupFragment popup = PopupFragment.getInstance();
        //popup.show(getSupportFragmentManager()),PopupFragment.TAG_EVENT_DIALOG);


        return rootView;
    }

    public void onBack() {
        MainActivity act = (MainActivity)getActivity();
        ((MainActivity) act).setOnBackPressListener(null);
        act.getSupportFragmentManager().beginTransaction().replace(R.id.small_layout1,new MainFragment()).commit();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressListener(this);
    }


    @Override
    public void onClick(View view) {

    }
}