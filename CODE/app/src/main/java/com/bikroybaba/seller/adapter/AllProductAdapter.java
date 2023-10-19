package com.bikroybaba.seller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.RecyclerviewAllProductBinding;
import com.bikroybaba.seller.model.entity.AllProduct;
import com.bikroybaba.seller.model.remote.request.ProductQuantity;
import com.bikroybaba.seller.model.remote.response.Offers;
import com.bikroybaba.seller.ui.product.AllProductFragmentDirections;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.ProductViewModel;
import com.bumptech.glide.Glide;

public class AllProductAdapter extends
        PagedListAdapter<AllProduct, AllProductAdapter.ViewHolder> {
    private final static DiffUtil.ItemCallback<AllProduct> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<AllProduct>() {
                @Override
                public boolean areItemsTheSame(AllProduct oldItem, AllProduct newItem) {
                    int oldId = Integer.parseInt(oldItem.getId());
                    int newId = Integer.parseInt(newItem.getId());
                    return oldId == newId;
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(AllProduct oldItem,
                                                  @NonNull AllProduct newItem) {
                    return oldItem.equals(newItem);
                }
            };
    private final ProductViewModel productViewModel;
    private final Context context;
    private final boolean isUpdated = false;
    private final UpdateUI updateUI;
    private final Utility utility;
    private int count = 0;

    public AllProductAdapter(Context context, ProductViewModel productViewModel, UpdateUI updateUI) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.productViewModel = productViewModel;
        this.updateUI = updateUI;
        utility = new Utility(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewAllProductBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(context),
                        R.layout.recyclerview_all_product, parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllProduct product = getItem(position);
        changeLanguage(holder);
        holder.binding.recyclerViewAllProductProductName.setText(product.getTitle());
        holder.binding.recyclerViewAllProductAvailableProduct.setText(product.getRemaining()
                + "*" + product.getUnitQuantity() + " " + product.getUnit() + " "
                + (utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? context.getString(R.string.avaible) : context.getString(R.string.avaible_bn)));
        holder.binding.recyclerViewAllProductProductPrice.setText("৳" + " " + product.getUnitPrice());
        holder.binding.recyclerViewAllProductProductUnit
                .setText((utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? context.getString(R.string.per) : context.getString(R.string.per_bn))
                        + " " + product.getUnit());

        if (product.getStatus().equalsIgnoreCase(KeyWord.ACTIVE)) {
            holder.binding.recyclerviewStatus
                    .setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.active : R.string.active_bn);
            holder.binding.recyclerviewStatus
                    .setBackground(ContextCompat.getDrawable(context, R.drawable.green_background_capsul_shape_stoke));
        } else if (product.getStatus().equalsIgnoreCase(KeyWord.INACTIVE)) {
            holder.binding.recyclerviewStatus
                    .setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.inactive : R.string.inactive_bn);
            holder.binding.recyclerviewStatus
                    .setBackground(ContextCompat.getDrawable(context, R.drawable.red_background_capsul_stoke));

        } else if (product.getStatus().equalsIgnoreCase(KeyWord.PENDING)) {
            holder.binding.recyclerviewStatus.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.pending : R.string.pending_bn);
            holder.binding.recyclerviewStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.red_background_capsul_stoke));
        }
        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.ic_default_image)
                .into(holder.binding.recyclerViewAllProductProductImage);
        if (product.getOffers().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            holder.binding.offerInfo.setVisibility(View.VISIBLE);
            for (Offers offers : product.getOffers()) {
                if (offers.getOfferCategory().equalsIgnoreCase(KeyWord.FIXED)) {
                    holder.binding.newPrice.setVisibility(View.VISIBLE);
                    holder.binding.recyclerViewAllProductProductPrice
                            .setBackground(ContextCompat.getDrawable(context, R.drawable.stick_through));
                    int previousPrice = Math.round(Float.parseFloat(product.getUnitPrice()));
                    int offerPrice = Math.round(Float.parseFloat(offers.getAmount()));
                    int newPrice = previousPrice - offerPrice;
                    holder.binding.newPrice.setText(KeyWord.TK_SIGN + newPrice);
                    stringBuilder.append(KeyWord.TK_SIGN).append(offers.getAmount()).append(" off");
                    stringBuilder.append(",");
                }
                if (offers.getOfferCategory().equalsIgnoreCase(KeyWord.PERCENTILE)) {
                    holder.binding.newPrice.setVisibility(View.VISIBLE);
                    holder.binding.recyclerViewAllProductProductPrice
                            .setBackground(ContextCompat.getDrawable(context, R.drawable.stick_through));
                    int previousPrice = Math.round(Float.parseFloat(product.getUnitPrice()));
                    int offerPrice = Math.round(Float.parseFloat(offers.getAmount()));
                    int newPrice = previousPrice - (previousPrice * offerPrice / 100);
                    holder.binding.newPrice.setText(newPrice + " Tk");
                    holder.binding.newPrice.setText(KeyWord.TK_SIGN + newPrice);
                    stringBuilder.append(offers.getAmount()).append("% off");
                    stringBuilder.append(",");
                } else if (offers.getOfferCategory().equalsIgnoreCase(KeyWord.BUNDLE)) {
                    stringBuilder.append("Buy ").append(offers.getMinAmount()).append(" Get ").append(offers.getAmount());
                    stringBuilder.append(",");
                }
            }

            // Remove the last character from the StringBuilder to avoid a trailing comma.
            String commaSeparatedList = stringBuilder.substring(0, stringBuilder.length() - 1);
            holder.binding.offerInfo.setText("(" + commaSeparatedList + ")");
        }

        holder.binding.recyclerViewAllProductPlus.setOnClickListener(v -> {
            count = Integer.parseInt(holder.binding.recyclerViewAllProductCount.getText().toString()) + 1;
            holder.binding.recyclerViewAllProductCount.setSelection(holder.binding.recyclerViewAllProductCount.getText().length());
            holder.binding.recyclerViewAllProductCount.setText(String.valueOf(count));
        });
        holder.binding.recyclerViewAllProductMinusBtn.setOnClickListener(v -> {
            if (Integer.parseInt(holder.binding.recyclerViewAllProductCount.getText().toString()) > 0) {
                count = Integer.parseInt(holder.binding.recyclerViewAllProductCount.getText().toString()) - 1;
                holder.binding.recyclerViewAllProductCount.setSelection(holder.binding.recyclerViewAllProductCount.getText().length());
                holder.binding.recyclerViewAllProductCount.setText(String.valueOf(count));
            }
        });

        holder.binding.recyclerViewAllProductUpdate.setOnClickListener(v -> {
            setProductId(product.getId());
            ProductQuantity productQuantity =
                    new ProductQuantity(product.getId(),
                            holder.binding.recyclerViewAllProductCount.getText().toString());
            productViewModel.updateProductQuantity(productQuantity)
                    .observe((LifecycleOwner) context, api_response -> {
                        if (api_response.getCode() == 200) {
                            if (product.getId().equals(getProductId())) {
                                holder.binding.recyclerViewAllProductAvailableProduct
                                        .setText(api_response.getData().getAsString() + "*"
                                                + product.getUnitQuantity() + " " + product.getUnit()
                                                + " " + "available");
                                holder.binding.recyclerViewAllProductCount.setText("0");
                                updateUI.update();
                            }
                        }
                    });
        });
        holder.binding.tvEdit.setOnClickListener(v -> {
            AllProductFragmentDirections.ActionNavAllProductToNavEditProduct action =
                    AllProductFragmentDirections.actionNavAllProductToNavEditProduct();
            action.setProductId(product.getId());
            Navigation.findNavController(v).navigate(action);
        });
    }

    private void changeLanguage(ViewHolder holder) {
        String language = utility.getLangauge();
        holder.binding.tvEdit.setText(language.equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.edit : R.string.edit_bn);
        holder.binding.recyclerViewAllProductUpdate
                .setText(R.string.update);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerviewAllProductBinding binding;
        public ViewHolder(RecyclerviewAllProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public String getProductId() {
        SharedPreferences sharedPref =
                context.getSharedPreferences("Product", Context.MODE_PRIVATE);
        return sharedPref.getString("id", "");
    }

    public void setProductId(String id) {
        SharedPreferences sharedPref =
                context.getSharedPreferences("Product", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("id", id);
        editor.apply();
    }

    public interface UpdateUI {
        void update();
    }
}
