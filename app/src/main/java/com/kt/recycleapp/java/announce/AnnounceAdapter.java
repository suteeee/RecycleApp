package com.kt.recycleapp.java.announce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kt.recycleapp.model.DatabaseReadModel;

import org.jetbrains.annotations.NotNull;

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
    public DatabaseReadModel model;

    public AnnounceViewHoler(AnnounceLayoutUnitBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.model = DatabaseReadModel.Companion.getInstance();
    }

    public void bind(AnnounceData data ,int position) {
        binding.whatisTv2.setText(data.itemName);
        binding.howtorecycleTv1.setText(data.resultInfo);
        binding.productClassisicationTv2.setText(data.kind);
        if(position == 0) model.setNameForSetImage(data.itemName);

        model.setImage(context,binding.announceIv,binding.imageLoadingPb,data.itemName);

    }
}
