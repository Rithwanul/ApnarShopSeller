package com.bikroybaba.seller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.RecyclerviewOrderDetailsBinding;
import com.bikroybaba.seller.model.remote.response.OrderProducts;

import java.util.List;

public class OrderDetailsAdapter extends
        RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    private final Context context;
    private final List<OrderProducts> productList;

    public OrderDetailsAdapter(Context context, List<OrderProducts> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewOrderDetailsBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(context),
                        R.layout.recyclerview_order_details, parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            OrderProducts orderListResponse = productList.get(position);
            holder.binding.productName.setText(orderListResponse.getNameQuantity());
            holder.binding.productPrice.setText(orderListResponse.getPrice());
            if (orderListResponse.getOffers().size() > 0) {
                holder.binding.offerName.setVisibility(View.VISIBLE);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < orderListResponse.getOffers().size(); i++) {
                    stringBuilder.append(orderListResponse.getOffers().get(i).getTitle());
                    stringBuilder.append(",");
                }
                // Remove the last character from the StringBuilder to avoid a trailing comma.
                String commaSeparatedList = stringBuilder.substring(0, stringBuilder.length() - 1);
                holder.binding.offerName.setText("("+commaSeparatedList+")");
            }else {
                holder.binding.offerName.setVisibility(View.GONE);
            }
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerviewOrderDetailsBinding binding;

        public ViewHolder(RecyclerviewOrderDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
