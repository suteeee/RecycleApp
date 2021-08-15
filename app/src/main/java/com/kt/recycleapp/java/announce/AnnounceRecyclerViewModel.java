package com.kt.recycleapp.java.announce;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kt.recycleapp.model.DatabaseReadModel;

import java.util.ArrayList;

public class AnnounceRecyclerViewModel extends ViewModel {
    public MutableLiveData<String> itemName = new MutableLiveData<>();
    public MutableLiveData<String> finding = new MutableLiveData<>();
    public MutableLiveData<String> setting = new MutableLiveData<>();
    public ObservableArrayList<AnnounceData> nameList = new ObservableArrayList<>();
    private DatabaseReadModel db = new DatabaseReadModel();
    static MutableLiveData<String> kind = new MutableLiveData<>();

    public void findProductKind(String str) {
        db.findProductKind(finding,str,kind); //일반쓰레기
    }

    public void setData(String str) {
        db.settingResult(setting,kind.getValue(),nameList,str);
    }

    public void setNameList(ObservableArrayList<AnnounceData> nameList) {
        this.nameList = nameList;
    }

    public ObservableArrayList<AnnounceData> getNameList() {
        return nameList;
    }
}