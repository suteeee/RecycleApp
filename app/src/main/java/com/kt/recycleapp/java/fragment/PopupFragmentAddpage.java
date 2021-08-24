package com.kt.recycleapp.java.fragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kt.recycleapp.kotlin.activity.OnBackPressListener;
import com.kt.recycleapp.kotlin.fragment.MultyAddFragment;
import com.kt.recycleapp.kotlin.viewmodel.AddViewModel;
import com.kt.recycleapp.manager.MyPreferenceManager;
import com.kt.recycleapp.model.DatabaseReadModel;

import java.recycleapp.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//물건이 등록되지 않은 물품일 떄 실행 된다.
//내 기억으로 이건 코틀린 영역에서 이 창이 뜨게해야하므로 주광님 화이팅!


public class PopupFragmentAddpage extends DialogFragment implements  OnBackPressListener{
    public static final String TAG_EVENT_DIALOG = "testtest";
    private Button saveButton;
    private Button cancleButton;
    private String sendBarcode;
    private DatabaseReadModel data = new DatabaseReadModel();
    MutableLiveData<String> ld = new MutableLiveData<String>();

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

       // Bundle bundle = getArguments();

       /* if(bundle != null){
            sendBarcode = bundle.getString("barcode");
            Log.d(sendBarcode,"준");
            AddViewModel.Companion.setBarcode(sendBarcode);
        }
        else{
            Log.d("null","준");
        }*/

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        ArrayList arr = data.getProductsList(ld);
        saveButton = (Button) rootView.findViewById(R.id.askYes_bt1);
        cancleButton = (Button) rootView.findViewById(R.id.askNo_bt1);

       /* writeProductName = (EditText) rootView.findViewById(R.id.inputProductName_et1);

        radioGroup = rootView.findViewById(R.id.radio_gr);
        addpage = rootView.findViewById(R.id.linearLayout);*/

       /* radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.multy_rb){
                    Log.d("main","aaaaaaaaaaaaaa");
                    requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.AskDataAdd_layout,new MultyAddFragment()).commit();
                }

            }
        });*/

        Bundle bundle = getArguments();

        Map<String, Object> tmpProduct = new HashMap<>();

        if(bundle != null){
            sendBarcode = bundle.getString("barcode");
            AddViewModel.Companion.setBarcode(sendBarcode);
        }
        else{
        }

        ld.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
               /* if(s.equals("finish")){
                    spinner = rootView.findViewById(R.id.products_sp);
                    ArrayAdapter adt = new ArrayAdapter(rootView.getContext(), android.R.layout.simple_spinner_dropdown_item,arr);
                    spinner.setAdapter(adt);
                    adt.notifyDataSetChanged();
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("것것",spinner.getItemAtPosition(i).toString());
                            product = spinner.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }*/
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                /*db.collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        //key에 바코드 저장하고 value에 물품명 저장하자
                        if(task.isSuccessful()){
                            tmpProduct.put(sendBarcode, writeProductName.getText().toString());

                            db.collection("products").document(product)
                                    .update(tmpProduct)//set하면 문서날라감
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

                        }
                        else{
                            Toast.makeText(rootView.getContext(), "등록실패, 다시입력하세요", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dismiss();*/

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.small_layout1,new MultyAddFragment()).commit();
                dismiss();
            }
        });

        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onBack() {}
}