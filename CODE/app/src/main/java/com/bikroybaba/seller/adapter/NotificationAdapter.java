package com.bikroybaba.seller.adapter;

import static com.bikroybaba.seller.R.drawable.rectangular_black_border_white;
import static com.bikroybaba.seller.R.drawable.rectangular_black_border_yellow_fill;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.RecyclerviewNotificationBinding;
import com.bikroybaba.seller.model.remote.request.UpdateNotificationRequest;
import com.bikroybaba.seller.model.remote.response.NotificationResponse;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.NotificationViewModel;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends
        RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final Context context;
    private final List<NotificationResponse> notificationList;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
    NotificationViewModel notificationViewModel;
    Utility utility;

    public NotificationAdapter(Context context, List<NotificationResponse> notificationList,
                               NotificationViewModel notificationViewModel, Utility utility) {
        this.context = context;
        this.notificationList = notificationList;
        this.notificationViewModel = notificationViewModel;
        this.utility = utility;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
        RecyclerviewNotificationBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(context),
                        R.layout.recyclerview_notification, parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
            NotificationResponse notificationResponse = notificationList.get(position);
            holder.binding.title.setText(notificationResponse.getTitle());
            holder.binding.body.setText(notificationResponse.getBody());
            holder.binding.rvDate
                    .setText(sdf.format(new Date(Long.parseLong(notificationResponse.getCreatedAt()))));
            if (notificationResponse.getReadStatus().equalsIgnoreCase(KeyWord.UNREAD)) {
                holder.binding.rvNotificationLayout
                        .setBackground(context.getResources().getDrawable(rectangular_black_border_yellow_fill));
            } else {
                holder.binding.rvNotificationLayout
                        .setBackground(context.getResources().getDrawable(rectangular_black_border_white));
            }
            Glide.with(context)
                    .load(notificationResponse.getPicture())
                    .placeholder(R.drawable.ic_default_image)
                    .into(holder.binding.rvNotificationImage);
            holder.itemView.setOnLongClickListener(v -> {
                if (utility.isNetworkAvailable()) {
                    UpdateNotificationRequest updateNotificationRequest =
                            new UpdateNotificationRequest(notificationResponse.getId(),
                                    KeyWord.READ, "NO");
                    notificationViewModel.updateNotification(updateNotificationRequest);
                } else {
                    utility.showDialog(context.getResources().getString(R.string.check_internet_connection));
                }
                return false;
            });
            holder.binding.rvDelete.setOnClickListener(v -> {
                if (utility.isNetworkAvailable()) {
                    notificationList.remove(notificationResponse);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,notificationList.size());
                    UpdateNotificationRequest updateNotificationRequest =
                            new UpdateNotificationRequest(notificationResponse.getId(),
                                    KeyWord.READ, "YES");
                    notificationViewModel.updateNotification(updateNotificationRequest);
                } else {
                    utility.showDialog(context.getResources().getString(R.string.check_internet_connection));
                }
            });
            if (notificationResponse.getMetadataType().equalsIgnoreCase(KeyWord.INTERNAL)){
                if (notificationResponse.getMetadataTitle().equalsIgnoreCase(KeyWord.SELLER_ORDER_DETAILS))
                holder.binding.rvNotificationLayout
                        .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_notification_to_nav_pending_order));
            }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerviewNotificationBinding binding;

        public ViewHolder(RecyclerviewNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
