package com.bikroybaba.seller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.RecyclerviewPendingOrderBinding;
import com.bikroybaba.seller.model.remote.request.OrderStatusRequest;
import com.bikroybaba.seller.model.remote.response.OrderListResponse;
import com.bikroybaba.seller.ui.order.PendingOrderFragmentDirections;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.OrderViewModel;
import com.bumptech.glide.Glide;


public class PendingOrderAdapter extends
        PagedListAdapter<OrderListResponse, PendingOrderAdapter.ViewHolder> {

    private final Context context;
    private final OrderViewModel orderViewModel;
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
    private String all_order;
    private String reason = "";

    public PendingOrderAdapter(Context context, OrderViewModel orderViewModel, Utility utility) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.orderViewModel = orderViewModel;
        this.utility = utility;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewPendingOrderBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(context),
                        R.layout.recyclerview_pending_order, parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderListResponse orderListResponse = getItem(position);
        if (orderListResponse != null) {
            holder.binding.rvPendingOrderDetails
                    .setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.detail : R.string.detail_bn);
            holder.binding.rvPendingOrderName.setText(orderListResponse.getBuyerName());
            holder.binding.rvPendingOrderMobileNumber.setText(orderListResponse.getBuyerPhone());
            holder.binding.rvPendingOrderOrderNumber
                    .setText(String.format("Order #%s", orderListResponse.getOrderTransactionId()));
            Glide.with(context)
                    .load(orderListResponse.getBuyerProfileImage())
                    .placeholder(R.drawable.ic_profile)
                    .centerCrop()
                    .into(holder.binding.rvPendingOrderProfileImage);

            holder.binding.rvPendingOrderOrderTime
                    .setText(utility.getDateTimeFromMillSecond(orderListResponse.getCreatedAt()));
            if (orderListResponse.getOrderStatus().equalsIgnoreCase(KeyWord.PLACED)) {
                holder.binding.rvPendingOrderOrderStatus.setText("Placed");
                holder.binding.rvPendingOrder.setCardBackgroundColor(context.getResources().getColor(R.color.app_yellow2));


                holder.binding.rvPendingOrderUpdateOrder.setText("Processing");
            } else if (orderListResponse.getOrderStatus().equalsIgnoreCase(KeyWord.PROCESSING)) {
                holder.binding.rvPendingOrderOrderStatus.setText("Processing");

                holder.binding.rvPendingOrderUpdateOrder.setText("On way");
            } else if (orderListResponse.getOrderStatus().equalsIgnoreCase(KeyWord.ON_WAY)) {
                holder.binding.rvPendingOrderOrderStatus.setText("On way");
                holder.binding.rvPendingOrderUpdateOrder.setText("Delivered");
            }

            holder.binding.rvPendingOrderUpdateOrder.setOnClickListener(v -> {
                if (utility.isNetworkAvailable()) {
                    if (orderListResponse.getOrderStatus().equalsIgnoreCase(KeyWord.PLACED)) {
                        OrderStatusRequest orderStatusRequest =
                                new OrderStatusRequest(orderListResponse.getOrderId(), KeyWord.PROCESSING);
                        orderViewModel.updateOrderStatus(orderStatusRequest);
                    } else if (orderListResponse.getOrderStatus().equalsIgnoreCase(KeyWord.PROCESSING)) {
                        OrderStatusRequest orderStatusRequest =
                                new OrderStatusRequest(orderListResponse.getOrderId(), KeyWord.ON_WAY);
                        orderViewModel.updateOrderStatus(orderStatusRequest);
                    } else if (orderListResponse.getOrderStatus().equalsIgnoreCase(KeyWord.ON_WAY)) {
                        OrderStatusRequest orderStatusRequest =
                                new OrderStatusRequest(orderListResponse.getOrderId(), KeyWord.DELIVERED);
                        orderViewModel.updateOrderStatus(orderStatusRequest);
                        holder.binding.rvPendingOrderUpdateOrder.setVisibility(View.GONE);
                        holder.binding.rvPendingOrderCancelOrder.setVisibility(View.GONE);
                    }
                } else {
                    utility.showDialog(context.getString(R.string.no_internet_connection));
                }
            });
            holder.binding.rvPendingOrderCancelOrder.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_order_reject, null);
                //findViewById here
                RadioGroup radioGroup = view.findViewById(R.id.order_reject_radio_group);
                EditText comment = view.findViewById(R.id.order_reject_comment);
                ImageView clear = view.findViewById(R.id.dialog_order_reject_clear);
                TextView save = view.findViewById(R.id.dialog_order_reject_save);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                clear.setOnClickListener(v1 -> {
                    dialog.dismiss();
                });
                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    int id = group.getCheckedRadioButtonId();
                    RadioButton reasonButton = view.findViewById(id);
                    if (reasonButton.getText().toString().equalsIgnoreCase("other")) {
                        comment.setVisibility(View.VISIBLE);
                    } else {
                        comment.setVisibility(View.GONE);
                    }
                });
                save.setOnClickListener(v1 -> {
                    if (!comment.getText().toString().equals("") ||
                            !comment.getText().toString().isEmpty()) {
                        reason = comment.getText().toString();
                    } else {
                        int id = radioGroup.getCheckedRadioButtonId();
                        RadioButton reasonButton = view.findViewById(id);
                        reason = reasonButton.getText().toString();
                    }
                    OrderStatusRequest orderStatusRequest =
                            new OrderStatusRequest(orderListResponse.getOrderId(), KeyWord.CANCELLED);
                    orderStatusRequest.setReason(reason);
                    orderViewModel.updateOrderStatus(orderStatusRequest);
                    dialog.dismiss();
                });
            });
            holder.itemView.setOnClickListener(v -> {
                PendingOrderFragmentDirections.ActionNavPendingOrderToNavOrderDetails action =
                        PendingOrderFragmentDirections.actionNavPendingOrderToNavOrderDetails();
                action.setOrderId(orderListResponse.getOrderId());
                Navigation.findNavController(v).navigate(action);
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerviewPendingOrderBinding binding;

        public ViewHolder(RecyclerviewPendingOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
