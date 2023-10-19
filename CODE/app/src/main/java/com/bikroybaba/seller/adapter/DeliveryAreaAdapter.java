package com.bikroybaba.seller.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class DeliveryAreaAdapter<T> extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<T> items;

    public abstract RecyclerView.ViewHolder setViewHolder(ViewGroup parent);

    public abstract void onBindData(RecyclerView.ViewHolder holder, T val,int position);

    public DeliveryAreaAdapter(Context context, List<T> items){
        this.context = context;
        this.items = items;
    }
    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return setViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        onBindData(holder,items.get(position),position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public T getItem(int position){
        return items.get(position);
    }
}
