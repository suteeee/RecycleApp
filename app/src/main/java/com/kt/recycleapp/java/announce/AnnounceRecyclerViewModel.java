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
    public MutableLiveData<Boolean> isHaveBarcode = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> checkBarcodeFinish = new MutableLiveData<>(false);
    public ObservableArrayList<AnnounceData> nameList = new ObservableArrayList<>();
    private DatabaseReadModel db = DatabaseReadModel.Companion.getInstance();
    static MutableLiveData<String> kind = new MutableLiveData<>();

    public void findProductKind(String str) {
        db.findProductKind(finding,str,kind);
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

    public void checkBarcode(String barcode) {
        db.checkBarcode(barcode,isHaveBarcode,checkBarcodeFinish);
    }
}