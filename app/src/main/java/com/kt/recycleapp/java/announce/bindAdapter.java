package com.kt.recycleapp.java.announce;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class bindAdapter {
    @BindingAdapter("announce")
    public static void set(RecyclerView recyclerView, ObservableArrayList<AnnounceData> item){
        LinearLayoutManager lm = new LinearLayoutManager(recyclerView.getContext());
        AnnounceAdapter adt = new AnnounceAdapter();
        lm.setOrientation(RecyclerView.HORIZONTAL);

        recyclerView.setAdapter(adt);
        recyclerView.setLayoutManager(lm);
        ((AnnounceAdapter)recyclerView.getAdapter()).setItems(item);
        ((AnnounceAdapter)recyclerView.getAdapter()).notifyDataSetChanged();
    }

}
