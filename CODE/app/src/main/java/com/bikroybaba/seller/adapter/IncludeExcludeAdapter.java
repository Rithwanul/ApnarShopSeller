package com.bikroybaba.seller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.RecyclerviewExcludeIncludeProductBinding;
import com.bikroybaba.seller.model.remote.request.AddOfferProduct;
import com.bikroybaba.seller.model.remote.response.OfferProductListResponse;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.viewmodel.OfferViewModel;
import com.bumptech.glide.Glide;

public class IncludeExcludeAdapter extends
        PagedListAdapter<OfferProductListResponse, IncludeExcludeAdapter.ViewHolder> {

    private final Context context;
    private final static DiffUtil.ItemCallback<OfferProductListResponse> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<OfferProductListResponse>() {
                @Override
                public boolean areItemsTheSame(OfferProductListResponse oldItem,
                                               OfferProductListResponse newItem) {
                    int oldId = Integer.parseInt(oldItem.getProductId());
                    int newId = Integer.parseInt(newItem.getProductId());
                    return oldId == newId;
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(OfferProductListResponse oldItem,
                                                  @NonNull OfferProductListResponse newItem) {
                    return oldItem.equals(newItem);
                }
            };
    private final String offerId;
    private final OfferViewModel offerViewModel;

    public IncludeExcludeAdapter(Context context, String offerId, OfferViewModel offerViewModel) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.offerId = offerId;
        this.offerViewModel = offerViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewExcludeIncludeProductBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(context),
                        R.layout.recyclerview_exclude_include_product, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OfferProductListResponse offerProduct = getItem(position);
        if (offerProduct != null) {
            Glide.with(context)
                    .load(offerProduct.getProductImage())
                    .placeholder(R.drawable.ic_default_image)
                    .into(holder.binding.excludeIncludeIv);

            holder.binding.excludeIncludeTitle.setText(offerProduct.getProductName());
            holder.binding.excludeIncludeUnit.setText(offerProduct.getUnit());
            if (offerProduct.getStatus().equalsIgnoreCase(KeyWord.FILTERED_BY_INCLUDED)) {
                holder.binding.excludeIncludeStatus.setText(context.getResources().getString(R.string.minus));
            } else if (offerProduct.getStatus().equalsIgnoreCase(KeyWord.FILTERED_BY_EXCLUDED)) {
                holder.binding.excludeIncludeStatus.setText(context.getResources().getString(R.string.plus));
            }
            holder.binding.excludeIncludeStatus.setOnClickListener(v -> {
                if (offerProduct.getStatus().equalsIgnoreCase(KeyWord.FILTERED_BY_INCLUDED)) {
                    AddOfferProduct addOfferProduct =
                            new AddOfferProduct(offerId, offerProduct.getProductId(), KeyWord.STATUS_EXCLUDE);
                    offerViewModel.addOfferProduct(addOfferProduct);
                } else if (offerProduct.getStatus().equalsIgnoreCase(KeyWord.FILTERED_BY_EXCLUDED)) {
                    AddOfferProduct addOfferProduct =
                            new AddOfferProduct(offerId, offerProduct.getProductId(), KeyWord.STATUS_INCLUDE);
                    offerViewModel.addOfferProduct(addOfferProduct);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerviewExcludeIncludeProductBinding binding;

        public ViewHolder(RecyclerviewExcludeIncludeProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
