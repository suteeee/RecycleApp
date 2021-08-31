package com.kt.recycleapp.java.announce;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kt.recycleapp.model.DatabaseReadModel;

import java.recycleapp.R;
import java.util.ArrayList;

public class AnnounceRecyclerViewModel extends ViewModel {
    public MutableLiveData<String> itemName = new MutableLiveData<>();
    public MutableLiveData<String> finding = new MutableLiveData<>();
    public MutableLiveData<String> setting = new MutableLiveData<>();
    public MutableLiveData<Integer> imgCode = new MutableLiveData<>(R.drawable.default_nothing);
    public ObservableArrayList<AnnounceData> nameList = new ObservableArrayList<>();
    private DatabaseReadModel db = new DatabaseReadModel();
    static MutableLiveData<String> kind = new MutableLiveData<>();

    public void findProductKind(String str) {
        db.findProductKind(finding,str,kind,imgCode);
    }

    public void setData(String barcode) {
        db.settingResult(setting,kind.getValue(),nameList,barcode);
    }

    public void setNameList(ObservableArrayList<AnnounceData> nameList) {
        this.nameList = nameList;
    }

    public ObservableArrayList<AnnounceData> getNameList() {
        return nameList;
    }
}