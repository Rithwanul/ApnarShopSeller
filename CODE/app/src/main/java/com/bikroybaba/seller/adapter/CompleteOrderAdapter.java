package com.bikroybaba.seller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.RecyclerviewCompleteOrderBinding;
import com.bikroybaba.seller.model.remote.response.OrderListResponse;
import com.bikroybaba.seller.ui.order.CompleteOrderFragmentDirections;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bumptech.glide.Glide;


public class CompleteOrderAdapter extends
        PagedListAdapter<OrderListResponse, CompleteOrderAdapter.ViewHolder> {

    private final Context context;
    private final Utility utility;

    private final static DiffUtil.ItemCallback<OrderListResponse> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<OrderListResponse>() {
                @Override
                public boolean areItemsTheSame(OrderListResponse oldItem,
                                               OrderListResponse newItem) {
                    int oldId = Integer.parseInt(oldItem.getOrderId());
                    int newId = Integer.parseInt(newItem.getOrderId());
                    return oldId == newId;
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(OrderListResponse oldItem,
                                                  @NonNull OrderListResponse newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public CompleteOrderAdapter(Context context, Utility utility) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.utility = utility;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewCompleteOrderBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(context),
                        R.layout.recyclerview_complete_order, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderListResponse orderListResponse = getItem(position);
        holder.binding.rvCompleteOrderDetails
                .setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.detail : R.string.detail_bn);
        holder.binding.rvCompleteOrderName.setText(orderListResponse.getBuyerName());
        holder.binding.rvCompleteOrderMobileNumber.setText(orderListResponse.getBuyerPhone());
        holder.binding.rvCompleteOrderOrderNumber.setText(String.format("Order no #%s", orderListResponse.getOrderTransactionId()));
        holder.binding.rvCompleteOrderOrderTime.setText(utility.getDateTimeFromMillSecond(orderListResponse.getCreatedAt()));

        if (orderListResponse.getOrderStatus().equalsIgnoreCase(KeyWord.CANCELLED)) {
            holder.binding.rvCompleteOrderOrderStatus
                    .setText(String.format("%s:%s", orderListResponse.getOrderStatus(),
                            orderListResponse.getOrderReason()));
            holder.binding.rvCompleteOrderOrderStatus
                    .setTextColor(context.getResources().getColor(R.color.red_700));
            holder.binding.rvCompleteOrder
                    .setCardBackgroundColor(context.getResources().getColor(R.color.app_yellow2));

        } else {
            holder.binding.rvCompleteOrderOrderStatus
                    .setText(orderListResponse.getOrderStatus());
            holder.binding.rvCompleteOrderOrderStatus
                    .setTextColor(context.getResources().getColor(R.color.green));
            holder.binding.rvCompleteOrder
                    .setCardBackgroundColor(context.getResources().getColor(R.color.app_white1));
        }
        if (!orderListResponse.getBuyerProfileImage().equals("")) {
            Glide.with(context)
                    .load(orderListResponse.getBuyerProfileImage())
                    .placeholder(R.drawable.ic_profile)
                    .into(holder.binding.rvCompleteOrderProfileImage);
        }
        holder.itemView.setOnClickListener(v -> {
            CompleteOrderFragmentDirections.ActionNavCompleteOrderToNavCompleteOrderDetails action =
                    CompleteOrderFragmentDirections.actionNavCompleteOrderToNavCompleteOrderDetails();
            action.setCompleteOrderId(orderListResponse.getOrderId());
            Navigation.findNavController(v).navigate(action);
            utility.hideKeyboard(holder.itemView);
        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerviewCompleteOrderBinding binding;
        public ViewHolder(RecyclerviewCompleteOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
