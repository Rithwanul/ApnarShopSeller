package com.bikroybaba.seller.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.OrderDetailsAdapter;
import com.bikroybaba.seller.databinding.FragmentCompleteOrderDetailsBinding;
import com.bikroybaba.seller.model.remote.request.OrderDetailsRequest;
import com.bikroybaba.seller.model.remote.response.OrderProducts;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Util;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.OrderViewModel;
import com.bumptech.glide.Glide;

import java.util.List;


public class CompleteOrderDetailsFragment extends Fragment {

    private FragmentCompleteOrderDetailsBinding binding;
    private OrderViewModel orderViewModel;
    private Utility utility;
    private String orderId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater,R.layout.fragment_complete_order_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utility = new Utility(requireActivity());
        initView();
        initViewModel();
            if (getArguments() != null) {
                orderId = CompleteOrderDetailsFragmentArgs.fromBundle(getArguments()).getCompleteOrderId();
            }
            changeLanguage();
            observeOrderDetails(orderId);
    }

    private void initView() {
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        TextView textToolHeader = toolbar.findViewById(R.id.title);
        textToolHeader.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? requireActivity().getResources().getString(R.string.order_details):getResources().getString(R.string.order_details_bn));
    }

    private void initViewModel() {
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
    }

    private void changeLanguage() {
        binding.shippingAddress.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH)?R.string.shipping_address:R.string.shipping_address_bn);
        binding.totalSummary.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH)?R.string.total_summary:R.string.total_summary_bn);
        binding.subTotal.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH)?R.string.sub_total:R.string.sub_total_bn);
        binding.deliveryCharge.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH)?R.string.delivery_charge:R.string.delivery_charge_bn);
        binding.discount.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH)?R.string.discount:R.string.discount_bn);
        binding.total.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH)?R.string.total:R.string.total_bn);
        }

    private void observeOrderDetails(String orderId) {
        OrderDetailsRequest orderDetailsRequest = new OrderDetailsRequest(orderId);
        orderViewModel.getOrderDetails(orderDetailsRequest)
                .observe(requireActivity(), orderDetailsResponse -> {
            utility.hideProgress();
            Glide.with(requireActivity())
                    .load(orderDetailsResponse.getProfilePicture())
                    .placeholder(R.drawable.ic_profile)
                    .into(binding.orderDetailsProfileimage);
            binding.orderDetailsName.setText(orderDetailsResponse.getName());
            binding.orderDetailsTime
                    .setText(String.format("Placed on: %s",
                            utility.getDateTimeFromMillSecond(orderDetailsResponse.getCreatedAt())));
            binding.orderDetailsMobileNo.setText("Phone No. " + orderDetailsResponse.getPhone());
            binding.orderDetailsOrderNo
                    .setText(String.format("Order No: #%s", orderDetailsResponse.getTransactionId()));
            int maxDeliveryTime = Math.round(Float.parseFloat(orderDetailsResponse.getMaxDeliveryTime()));
            String convertedTime = Util.convertMinutesToDays(maxDeliveryTime);
            binding.tvDeliveryTime
                    .setText(getString(R.string.delivery_time) + convertedTime + " " + getString(R.string.approx));
            binding.orderDetailsPaymentType.setText(orderDetailsResponse.getPaymentType().toLowerCase());
            if (orderDetailsResponse.getStatus().equalsIgnoreCase(KeyWord.CANCELLED)) {
                if (orderDetailsResponse.getComment().equals("")){
                    binding.orderDetailsStatus.setText(orderDetailsResponse.getStatus().toLowerCase());
                }else {
                    binding.orderDetailsStatus
                            .setText(orderDetailsResponse.getStatus().toLowerCase() + ":" +orderDetailsResponse.getComment());
                }
                binding.orderDetailsStatus.setTextColor(requireActivity().getResources().getColor(R.color.red_700));
            } else {
                binding.orderDetailsStatus.setText(orderDetailsResponse.getStatus().toLowerCase());
            }
            binding.orderDetailsAddress.setText(orderDetailsResponse.getAddress());
            binding.orderDetailsSubTotal.setText(orderDetailsResponse.getSubTotal());
            if (!orderDetailsResponse.getFreeDelivery().equals("")){
                binding.orderDetailsDeliveryCharge.setText("("+orderDetailsResponse.getFreeDelivery()+") "+orderDetailsResponse.getDeliveryCharge());
            }else {
                binding.orderDetailsDeliveryCharge.setText(orderDetailsResponse.getDeliveryCharge());
            }

            if (!orderDetailsResponse.getVoucher().equals("")){
                binding.orderDetailsDiscount.setText("("+orderDetailsResponse.getVoucher()+") "+orderDetailsResponse.getDiscount());
            }else {
                binding.orderDetailsDiscount.setText(orderDetailsResponse.getDiscount());
            }
            binding.orderDetailsTotal.setText(orderDetailsResponse.getTotal());
            initAdapter(orderDetailsResponse.getOrderProducts());
        });
    }

    private void initAdapter(List<OrderProducts> orderProductList) {
        OrderDetailsAdapter adapter = new OrderDetailsAdapter(requireActivity(), orderProductList);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvOrderDetails.setLayoutManager(mLayoutManager);
        binding.rvOrderDetails.setAdapter(adapter);
    }
}