package com.bikroybaba.seller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.ItemOfferListBinding;
import com.bikroybaba.seller.model.remote.request.OfferStatus;
import com.bikroybaba.seller.model.remote.response.OfferDetailsResponse;
import com.bikroybaba.seller.ui.offer.CreateOfferFragment;
import com.bikroybaba.seller.ui.offer.OfferListFragmentDirections;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.OfferViewModel;

public class OfferListAdapter extends
        PagedListAdapter<OfferDetailsResponse, OfferListAdapter.ViewHolder> {

    private final Context context;
    private final Utility utility;
    private boolean isActive;
    private final OfferViewModel offerViewModel;

    private final static DiffUtil.ItemCallback<OfferDetailsResponse> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<OfferDetailsResponse>() {
                @Override
                public boolean areItemsTheSame(OfferDetailsResponse oldItem,
                                               OfferDetailsResponse newItem) {
                    int oldId = Integer.parseInt(oldItem.getId());
                    int newId = Integer.parseInt(newItem.getId());
                    return oldId == newId;
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(OfferDetailsResponse oldItem,
                                                  @NonNull OfferDetailsResponse newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public OfferListAdapter(Context context,OfferViewModel offerViewModel) {
        super(DIFF_CALLBACK);
        this.context = context;
        utility = new Utility(context);
        this.offerViewModel = offerViewModel;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOfferListBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(context),
                        R.layout.item_offer_list, parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OfferDetailsResponse offerProduct = getItem(position);
        holder.binding.tvEdit.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH)?R.string.edit:R.string.edit_bn);
        if (offerProduct != null) {
            if (offerProduct.getOfferTypeResponse().getOfferTypeTitle().equalsIgnoreCase(KeyWord.TOTAL_BILL)) {
                if (offerProduct.getOfferCategory().getOfferCategoryTitle().equalsIgnoreCase(KeyWord.FIXED)) {
                    holder.binding.runningOfferMessage.setText("Offer name: " + offerProduct.getOfferName()+
                            ", Offer Type: "+utility.firstTextCapitalize(offerProduct.getOfferTypeResponse().getOfferTypeTitle())
                            + ", Discount: "+ KeyWord.TK_SIGN + offerProduct.getAmount()
                            +"("+utility.firstTextCapitalize(offerProduct.getOfferCategory().getOfferCategoryTitle())+")"
                            + ", Minimum buy: "+ KeyWord.TK_SIGN + offerProduct.getMinAmount()
                            + ", Voucher code: " + offerProduct.getGeneratedCode());

                    holder.binding.runningOfferValidity.setText("Validity: "
                            + CreateOfferFragment.convertDate(offerProduct.getStartTime(), "dd/MM/yyyy")
                            + " to " + CreateOfferFragment.convertDate(offerProduct.getEndTime(), "dd/MM/yyyy"));

                } else {
                    holder.binding.runningOfferMessage.setText("Offer name: " + offerProduct.getOfferName()+
                            ", Offer Type: " + utility.firstTextCapitalize(offerProduct.getOfferTypeResponse().getOfferTypeTitle())
                            + ", Discount: "+ offerProduct.getAmount()+"% (Percentage)"
                            + ", Minimum buy: "+ KeyWord.TK_SIGN + offerProduct.getMinAmount()
                            + ", Maximum Discount: "+ KeyWord.TK_SIGN + offerProduct.getMaxAmount());

                    holder.binding.runningOfferValidity.setText("Validity: "
                            + CreateOfferFragment.convertDate(offerProduct.getStartTime(), "dd/MM/yyyy")
                            + " to " + CreateOfferFragment.convertDate(offerProduct.getEndTime(), "dd/MM/yyyy"));

                }

            } else if (offerProduct.getOfferTypeResponse().getOfferTypeTitle().equalsIgnoreCase(KeyWord.FREE_DELIVERY)) {
                holder.binding.runningOfferMessage.setText("Offer name: " + offerProduct.getOfferName()+
                        ", Offer Type: "+utility.firstTextCapitalize(offerProduct.getOfferTypeResponse().getOfferTypeTitle())
                        + ", Minimum buy: "+ KeyWord.TK_SIGN + offerProduct.getMinAmount());


                holder.binding.runningOfferValidity.setText("Validity: "
                        + CreateOfferFragment.convertDate(offerProduct.getStartTime(), "dd/MM/yyyy")
                        + " to " + CreateOfferFragment.convertDate(offerProduct.getEndTime(), "dd/MM/yyyy"));


            } else if (offerProduct.getOfferTypeResponse().getOfferTypeTitle().equalsIgnoreCase(KeyWord.PRODUCT_BASED)) {
                if (offerProduct.getOfferCategory().getOfferCategoryTitle().equalsIgnoreCase(KeyWord.FIXED)) {
                    holder.binding.runningOfferMessage.setText("Offer name: " + offerProduct.getOfferName()+
                            ", Offer Type: "+utility.firstTextCapitalize(offerProduct.getOfferTypeResponse().getOfferTypeTitle())
                            + ", Discount: "+ KeyWord.TK_SIGN + offerProduct.getAmount()
                            +"("+utility.firstTextCapitalize(offerProduct.getOfferCategory().getOfferCategoryTitle())+")");
                    holder.binding.runningOfferValidity.setText("Validity: "
                            + CreateOfferFragment.convertDate(offerProduct.getStartTime(), "dd/MM/yyyy")
                            + " to " + CreateOfferFragment.convertDate(offerProduct.getEndTime(), "dd/MM/yyyy"));

                } else if (offerProduct.getOfferCategory().getOfferCategoryTitle().equalsIgnoreCase(KeyWord.PERCENTILE)) {
                    holder.binding.runningOfferMessage.setText("Offer name: " + offerProduct.getOfferName()+
                            ", Offer Type: "+utility.firstTextCapitalize(offerProduct.getOfferTypeResponse().getOfferTypeTitle())
                            + ", Discount: "+ offerProduct.getAmount()+"% (Percentage)");
                    holder.binding.runningOfferValidity.setText("Validity: "
                            + CreateOfferFragment.convertDate(offerProduct.getStartTime(), "dd/MM/yyyy")
                            + " to " + CreateOfferFragment.convertDate(offerProduct.getEndTime(), "dd/MM/yyyy"));

                } else if (offerProduct.getOfferCategory().getOfferCategoryTitle().equalsIgnoreCase(KeyWord.BUNDLE)) {
                    holder.binding.runningOfferMessage.setText("Offer name: " + offerProduct.getOfferName()+
                            ", Offer Type: "+utility.firstTextCapitalize(offerProduct.getOfferTypeResponse().getOfferTypeTitle())
                            + ", Discount: Buy "+ offerProduct.getMinAmount()+" Get "
                            + offerProduct.getAmount()+"("+utility.firstTextCapitalize(offerProduct.getOfferCategory().getOfferCategoryTitle())+")");

                    holder.binding.runningOfferValidity.setText("Validity: "
                            + CreateOfferFragment.convertDate(offerProduct.getStartTime(), "dd/MM/yyyy")
                            + " to " + CreateOfferFragment.convertDate(offerProduct.getEndTime(), "dd/MM/yyyy"));
                }
            }
        }
        if (offerProduct != null) {
            if (offerProduct.getStatus().equalsIgnoreCase(KeyWord.ACTIVE)) {
                isActive = true;
                holder.binding.runningOfferStop.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH)?R.string.stop_this_offer:R.string.stop_this_offer_bn);
                holder.binding.runningOfferStop.setBackground(ContextCompat.getDrawable(context,R.drawable.red_background_capsul_shape_fill));
                holder.binding.runningOfferStop.setTextColor(ContextCompat.getColor(context,R.color.app_white1));
            } else if (offerProduct.getStatus().equalsIgnoreCase(KeyWord.INACTIVE)) {
                isActive = false;
                holder.binding.runningOfferStop.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH)?R.string.start_this_offer:R.string.stop_this_offer_bn);
                holder.binding.runningOfferStop.setBackground(ContextCompat.getDrawable(context,R.drawable.green_background_capsul_fill));
                holder.binding.runningOfferStop.setTextColor(ContextCompat.getColor(context,R.color.app_white1));
            }
        }

        holder.binding.runningOfferStop.setOnClickListener(v -> {
            if (utility.isNetworkAvailable()) {
                if (!isActive) {
                    OfferStatus offerStatus = null;
                    if (offerProduct != null) {
                        offerStatus = new OfferStatus(offerProduct.getId(), KeyWord.ACTIVE);
                    }
                    offerViewModel.updateOfferStatus(offerStatus);
                    holder.binding.runningOfferStop.setText("Stop this offer");
                    holder.binding.runningOfferStop.setBackground(ContextCompat.getDrawable(context,R.drawable.red_background_capsul_shape_fill));
                    holder.binding.runningOfferStop.setTextColor(ContextCompat.getColor(context,R.color.app_white1));
                    isActive = true;

                } else {
                    OfferStatus offerStatus = null;
                    if (offerProduct != null) {
                        offerStatus = new OfferStatus(offerProduct.getId(), KeyWord.INACTIVE);
                    }
                    offerViewModel.updateOfferStatus(offerStatus);
                    holder.binding.runningOfferStop.setText("Start this offer");
                    holder.binding.runningOfferStop.setBackground(ContextCompat.getDrawable(context,R.drawable.green_background_capsul_fill));
                    holder.binding.runningOfferStop.setTextColor(ContextCompat.getColor(context,R.color.app_white1));
                    isActive = false;
                }
            } else {
                utility.showDialog(context.getString(R.string.check_internet_connection));
            }
        });

        holder.binding.runningOfferEdit.setOnClickListener(v -> {
            OfferListFragmentDirections.ActionNavOfferListToNavEditOffer2 action =
                    OfferListFragmentDirections.actionNavOfferListToNavEditOffer2(offerProduct);
            Navigation.findNavController(v).navigate(action);
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        ItemOfferListBinding binding;
        public ViewHolder(ItemOfferListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
