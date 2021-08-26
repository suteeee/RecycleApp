package com.kt.recycleapp.java.announce;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kt.recycleapp.model.DatabaseReadModel;

import org.jetbrains.annotations.NotNull;

import java.recycleapp.R;
import java.recycleapp.databinding.AnnounceLayoutUnitBinding;
import java.util.ArrayList;

public class AnnounceAdapter extends RecyclerView.Adapter<AnnounceViewHoler> {
    public ArrayList<AnnounceData> items = new ArrayList<>();

    public void setItems(ArrayList<AnnounceData> items){
        this.items = items;
    }

    public ArrayList<AnnounceData> getItems() {
        return this.items;
    }

    @NotNull
    @Override
    public AnnounceViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AnnounceLayoutUnitBinding binding = AnnounceLayoutUnitBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new AnnounceViewHoler(binding,parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull  AnnounceViewHoler holder, int position) {
        holder.bind(items.get(position),position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class AnnounceViewHoler extends RecyclerView.ViewHolder{
    AnnounceLayoutUnitBinding binding;
    Context context;
    public DatabaseReadModel model = new DatabaseReadModel();

    public AnnounceViewHoler(AnnounceLayoutUnitBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
    }

    public void bind(AnnounceData data ,int position) {
        binding.whatisTv2.setText(data.itemName);
        binding.howtorecycleTv1.setText(data.resultInfo);
        model.setDefaultImage(context,binding.announceIv,binding.imageLoadingPb);


    }
}
