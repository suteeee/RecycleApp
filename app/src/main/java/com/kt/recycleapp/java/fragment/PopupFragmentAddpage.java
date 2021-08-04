package com.kt.recycleapp.java.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kt.recycleapp.manager.MyPreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.recycleapp.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//물건이 등록되지 않은 물품일 떄 실행 된다.
//내 기억으로 이건 코틀린 영역에서 이 창이 뜨게해야하므로 주광님 화이팅!


public class PopupFragmentAddpage extends DialogFragment implements  View.OnClickListener{
    private EditText writeBarcode;
    private EditText writeProductName;
    private Button saveButton;
    private String sendBarcode;


    public PopupFragmentAddpage(){
        Bundle bundle = getArguments();

        if(bundle != null){
            sendBarcode = bundle.getString("barcode");
            Log.d(sendBarcode,"준");
        }
        else{
            Log.d("null","준");
        }
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


        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                db.collection("recycleApp").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        //key에 바코드 저장하고 value에 물품명 저장하자

                        Map<String, Object> products = new HashMap<>();
                        products.put("name", "Los Angeles");
                        products.put("state", "CA");
                        products.put("country", "USA");

                        db.collection("cities").document("LA")
                                .set(products)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                    }
                });
            }
        });



        //메인엑티비티에 박아야할 코드(코틀린으로 변환해놨음)
        //PopupFragment popup = PopupFragment.getInstance();
        //popup.show(getSupportFragmentManager()),PopupFragment.TAG_EVENT_DIALOG);


        return rootView;
    }

    @Override
    public void onClick(View view) {

    }
}