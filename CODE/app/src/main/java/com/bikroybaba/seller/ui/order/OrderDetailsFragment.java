package com.bikroybaba.seller.ui.order;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.OrderDetailsAdapter;
import com.bikroybaba.seller.databinding.FragmentOrderDetailsBinding;
import com.bikroybaba.seller.model.remote.request.OrderDetailsRequest;
import com.bikroybaba.seller.model.remote.request.OrderStatusRequest;
import com.bikroybaba.seller.model.remote.response.OrderDetailsResponse;
import com.bikroybaba.seller.model.remote.response.OrderProducts;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Util;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.OrderViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;


public class OrderDetailsFragment extends Fragment {

    private FragmentOrderDetailsBinding binding;
    private OrderViewModel orderViewModel;
    private String orderId;
    private Utility utility;
    private boolean isProcessingClicked = false, isOnWayClicked = false;
    private String status = "", reason = "";
    private OrderDetailsResponse orderDetailsResponse;
    int processingClickCount = 0, onWayClick = 0, onDeliveryClick = 0;
    private MaterialTextView mTvToolHeader;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        observeViewModel();

    }

    private void observeViewModel() {
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        utility = new Utility(requireActivity());
        orderId = OrderDetailsFragmentArgs.fromBundle(getArguments()).getOrderId();
        utility.showProgress(false, "");
        observeOrderDetails(orderId);
        observeOrderStatusResponse();
        binding.orderDetailsProcessingIv.setOnClickListener(v -> {
            if (processingClickCount == 0) {
                if (!status.equalsIgnoreCase(KeyWord.CANCELLED)) {
                    status = KeyWord.PROCESSING;
                    binding.orderDetailsProcessingIv
                            .setImageDrawable(requireActivity().getResources().getDrawable(R.drawable.ic_tick));
                    binding.orderDetailsView2
                            .setBackgroundColor(requireActivity().getResources().getColor(R.color.green));
                    binding.orderDetailsProcessingTv
                            .setTextColor(requireActivity().getResources().getColor(R.color.green));
                    isProcessingClicked = true;
                }
                processingClickCount++;
            } else {
                status = KeyWord.PLACED;
                isProcessingClicked = false;
                processingClickCount = 0;
                onWayClick = 0;
                binding.orderDetailsProcessingIv
                        .setImageDrawable(requireActivity().getResources().getDrawable(R.drawable.timeline_view));
                binding.orderDetailsView2.setBackgroundColor(requireActivity().getResources().getColor(R.color.black));
                binding.orderDetailsProcessingTv.setTextColor(requireActivity().getResources().getColor(R.color.black));

                binding.orderDetailsOnthewayIv
                        .setImageDrawable(requireActivity().getResources().getDrawable(R.drawable.timeline_view));
                binding.orderDetailsView3.setBackgroundColor(requireActivity().getResources().getColor(R.color.black));
                binding.orderDetailsOnthewayTv.setTextColor(requireActivity().getResources().getColor(R.color.black));
                isOnWayClicked = false;

                binding.orderDetailsDeliveredIv
                        .setImageDrawable(requireActivity().getResources().getDrawable(R.drawable.timeline_view));
                binding.orderDetailsDeliveredTv.setTextColor(requireActivity().getResources().getColor(R.color.black));
            }
        });

        binding.orderDetailsOnthewayIv.setOnClickListener(v -> {
            if (onWayClick == 0) {
                if (orderDetailsResponse.getStatus().equalsIgnoreCase(KeyWord.PROCESSING)) {
                    status = KeyWord.ON_WAY;
                    binding.orderDetailsOnthewayIv
                            .setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));
                    binding.orderDetailsView3
                            .setBackgroundColor(getResources().getColor(R.color.green));
                    binding.orderDetailsOnthewayTv
                            .setTextColor(getResources().getColor(R.color.green));
                    isOnWayClicked = true;
                } else {
                    if (isProcessingClicked) {
                        status = KeyWord.ON_WAY;
                        binding.orderDetailsOnthewayIv
                                .setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));
                        binding.orderDetailsView3
                                .setBackgroundColor(getResources().getColor(R.color.green));
                        binding.orderDetailsOnthewayTv
                                .setTextColor(getResources().getColor(R.color.green));
                        isOnWayClicked = true;
                    }
                }
                onWayClick++;
            } else {
                status = KeyWord.PROCESSING;
                isOnWayClicked = false;
                onWayClick = 0;
                onDeliveryClick = 0;
                binding.orderDetailsOnthewayIv
                        .setImageDrawable(this.getResources().getDrawable(R.drawable.timeline_view));
                binding.orderDetailsView3.setBackgroundColor(getResources().getColor(R.color.black));
                binding.orderDetailsOnthewayTv.setTextColor(getResources().getColor(R.color.black));
                binding.orderDetailsDeliveredIv
                        .setImageDrawable(getResources().getDrawable(R.drawable.timeline_view));
                binding.orderDetailsDeliveredTv.setTextColor(getResources().getColor(R.color.black));
            }
        });

        binding.orderDetailsDeliveredIv.setOnClickListener(v -> {
            if (onDeliveryClick == 0) {
                if (orderDetailsResponse.getStatus().equalsIgnoreCase(KeyWord.ON_WAY)) {
                    status = KeyWord.DELIVERED;
                    binding.orderDetailsDeliveredIv
                            .setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));
                    binding.orderDetailsDeliveredTv.setTextColor(getResources().getColor(R.color.green));
                } else {
                    if (isOnWayClicked) {
                        status = KeyWord.DELIVERED;
                        binding.orderDetailsDeliveredIv
                                .setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));
                        binding.orderDetailsDeliveredTv
                                .setTextColor(getResources().getColor(R.color.green));
                    }
                }
                onDeliveryClick++;
            } else {
                status = KeyWord.ON_WAY;
                binding.orderDetailsDeliveredIv
                        .setImageDrawable(getResources().getDrawable(R.drawable.timeline_view));
                binding.orderDetailsDeliveredTv.setTextColor(getResources().getColor(R.color.black));
                onDeliveryClick = 0;
            }
        });

        binding.orderDetailsCancelOrder.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            View _view = getLayoutInflater().inflate(R.layout.dialog_order_reject, null);
            //findViewById here
            RadioGroup radioGroup = _view.findViewById(R.id.order_reject_radio_group);
            EditText comment = _view.findViewById(R.id.order_reject_comment);
            ImageView clear = _view.findViewById(R.id.dialog_order_reject_clear);
            TextView save = _view.findViewById(R.id.dialog_order_reject_save);
            save.setText(R.string.save);
            builder.setView(_view);
            final AlertDialog dialog = builder.create();
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            clear.setOnClickListener(v1 -> {
                dialog.dismiss();
            });

            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                int id = group.getCheckedRadioButtonId();
                RadioButton reasonButton = _view.findViewById(id);
                if (reasonButton.getText().toString().equalsIgnoreCase("other")) {
                    comment.setVisibility(View.VISIBLE);
                } else {
                    comment.setVisibility(View.GONE);
                    reason = reasonButton.getText().toString();
                    comment.setText("");
                }
            });
            save.setOnClickListener(v1 -> {
                if (!comment.getText().toString().equals("") || !comment.getText().toString().isEmpty()) {
                    reason = comment.getText().toString();
                }
                status = KeyWord.CANCELLED;
                OrderStatusRequest orderStatusRequest = new OrderStatusRequest(orderId, status);
                orderStatusRequest.setReason(reason);
                orderViewModel.updateOrderStatus(orderStatusRequest);
                dialog.dismiss();
            });
        });

        binding.orderDetailsUpdate.setOnClickListener(v -> {
            OrderStatusRequest orderStatusRequest = new OrderStatusRequest(orderId, status);
            orderViewModel.updateOrderStatus(orderStatusRequest);
        });
        changeLanguage();
    }

    private void initView() {
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        mTvToolHeader = toolbar.findViewById(R.id.title);
    }

    private void changeLanguage() {
        mTvToolHeader.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? requireActivity()
                .getResources().getString(R.string.order_details) : requireActivity().getResources()
                .getString(R.string.order_details_bn));
        binding.shippingAddress.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.shipping_address : R.string.shipping_address_bn);
        binding.totalSummary.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.total_summary : R.string.total_summary_bn);
        binding.subTotal.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.sub_total : R.string.sub_total_bn);
        binding.deliveryCharge.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.delivery_charge : R.string.delivery_charge_bn);
        binding.discount.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.discount : R.string.discount_bn);
        binding.total.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.total : R.string.total_bn);
        binding.orderDetailsCancelOrder.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.cancel_order : R.string.cancel_order_bn);
//        binding.orderDetailsUpdate.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.save : R.string.save_bn);
    }

    private void observeOrderStatusResponse() {
        orderViewModel.getOrderStatusResponse().observe(requireActivity(), api_response -> {
            if (api_response.getCode() == 200) {
                utility.showDialog(api_response.getData().getAsString());
                if (status.equalsIgnoreCase(KeyWord.CANCELLED)) {
                    binding.orderDetailsStatus.setText(status.toLowerCase() + ":" + reason);
                    binding.orderDetailsStatus.setTextColor(requireActivity().getResources().getColor(R.color.red_700));
                } else {
                    binding.orderDetailsStatus.setText(status.toLowerCase());
                }
            } else {
                utility.showDialog(api_response.getData().getAsString());
            }
        });

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void observeOrderDetails(String orderId) {
        OrderDetailsRequest orderDetailsRequest = new OrderDetailsRequest(orderId);
        orderViewModel.getOrderDetails(orderDetailsRequest).observe(requireActivity(),
                orderDetailsResponse -> {
                    this.orderDetailsResponse = orderDetailsResponse;
                    initAdapter(orderDetailsResponse.getOrderProducts());
                    utility.hideProgress();
                    Glide.with(requireActivity())
                            .load(orderDetailsResponse.getProfilePicture())
                            .placeholder(R.drawable.ic_profile)
                            .into(binding.orderDetailsProfileimage);
                    binding.orderDetailsName.setText(orderDetailsResponse.getName());
                    binding.orderDetailsTime.setText(String.format("Placed on: %s",
                            utility.getDateTimeFromMillSecond(orderDetailsResponse.getCreatedAt())));
                    binding.orderDetailsMobileNo.setText("Phone No: " + orderDetailsResponse.getPhone());
                    int maxDeliveryTime = Math.round(Float.parseFloat(orderDetailsResponse.getMaxDeliveryTime()));
                    String convertedTime = Util.convertMinutesToDays(maxDeliveryTime);
                    binding.tvDeliveryTime
                            .setText(getString(R.string.delivery_time) + convertedTime + " " + getString(R.string.approx));
                    binding.orderDetailsOrderNo.setText(String.format("Order No: #%s",
                            orderDetailsResponse.getTransactionId()));
                    binding.orderDetailsPaymentType.setText(orderDetailsResponse.getPaymentType().toLowerCase());
                    if (orderDetailsResponse.getStatus().equalsIgnoreCase(KeyWord.CANCELLED)) {
                        if (orderDetailsResponse.getComment().equals("")) {
                            binding.orderDetailsStatus.setText(orderDetailsResponse.getStatus().toLowerCase());
                        } else {
                            binding.orderDetailsStatus.setText(orderDetailsResponse.getStatus().toLowerCase() + ":" + orderDetailsResponse.getComment());
                        }
                        binding.orderDetailsStatus.setTextColor(requireActivity().getResources().getColor(R.color.red_700));
                    } else {
                        binding.orderDetailsStatus.setText(orderDetailsResponse.getStatus().toLowerCase());
                    }
                    binding.orderDetailsAddress.setText(orderDetailsResponse.getAddress());
                    binding.orderDetailsSubTotal.setText(orderDetailsResponse.getSubTotal());
                    if (!orderDetailsResponse.getFreeDelivery().equals("")) {
                        binding.orderDetailsDeliveryCharge
                                .setText("(" + orderDetailsResponse.getFreeDelivery() + ") " + orderDetailsResponse.getDeliveryCharge());
                    } else {
                        binding.orderDetailsDeliveryCharge.setText(orderDetailsResponse.getDeliveryCharge());
                    }

                    if (!orderDetailsResponse.getVoucher().equals("")) {
                        binding.orderDetailsDiscount
                                .setText("(" + orderDetailsResponse.getVoucher() + ") -" + orderDetailsResponse.getDiscount());
                    } else {
                        binding.orderDetailsDiscount.setText("-" + orderDetailsResponse.getDiscount());
                    }
                    binding.orderDetailsTotal.setText(orderDetailsResponse.getTotal());
                    if (orderDetailsResponse.getStatus().equalsIgnoreCase(KeyWord.PLACED)) {
                        binding.orderDetailsPlacedIv.setImageDrawable(requireActivity()
                                .getResources().getDrawable(R.drawable.ic_tick));
                        binding.orderDetailsView1.setBackgroundColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsPlacedTv.setTextColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsPlacedIv.setEnabled(false);
                    } else if (orderDetailsResponse.getStatus().equalsIgnoreCase(KeyWord.PROCESSING)) {
                        binding.orderDetailsPlacedIv.setImageDrawable(requireActivity()
                                .getResources().getDrawable(R.drawable.ic_tick));
                        binding.orderDetailsView1.setBackgroundColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsPlacedTv.setTextColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsPlacedIv.setEnabled(false);

                        binding.orderDetailsProcessingIv.setImageDrawable(requireActivity()
                                .getResources().getDrawable(R.drawable.ic_tick));
                        binding.orderDetailsView2.setBackgroundColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsProcessingTv.setTextColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsProcessingIv.setEnabled(false);
                    } else if (orderDetailsResponse.getStatus().equalsIgnoreCase(KeyWord.ON_WAY)) {
                        binding.orderDetailsPlacedIv.setImageDrawable(requireActivity()
                                .getResources().getDrawable(R.drawable.ic_tick));
                        binding.orderDetailsView1.setBackgroundColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsPlacedTv.setTextColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsPlacedIv.setEnabled(false);

                        binding.orderDetailsProcessingIv.setImageDrawable(requireActivity()
                                .getResources().getDrawable(R.drawable.ic_tick));
                        binding.orderDetailsView2.setBackgroundColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsProcessingTv.setTextColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsProcessingIv.setEnabled(false);

                        binding.orderDetailsOnthewayIv.setImageDrawable(requireActivity()
                                .getResources().getDrawable(R.drawable.ic_tick));
                        binding.orderDetailsView3.setBackgroundColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsOnthewayTv.setTextColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsOnthewayIv.setEnabled(false);
                    } else if (orderDetailsResponse.getStatus().equalsIgnoreCase(KeyWord.DELIVERED)) {
                        binding.orderDetailsPlacedIv.setImageDrawable(requireActivity()
                                .getResources().getDrawable(R.drawable.ic_tick));
                        binding.orderDetailsView1.setBackgroundColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsPlacedTv.setTextColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsPlacedIv.setEnabled(false);

                        binding.orderDetailsProcessingIv.setImageDrawable(requireActivity()
                                .getResources().getDrawable(R.drawable.ic_tick));
                        binding.orderDetailsView2.setBackgroundColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsProcessingTv.setTextColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsProcessingIv.setEnabled(false);

                        binding.orderDetailsOnthewayIv.setImageDrawable(requireActivity()
                                .getResources().getDrawable(R.drawable.ic_tick));
                        binding.orderDetailsView3.setBackgroundColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsOnthewayTv.setTextColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsOnthewayIv.setEnabled(false);

                        binding.orderDetailsDeliveredIv.setImageDrawable(requireActivity()
                                .getResources().getDrawable(R.drawable.ic_tick));
                        binding.orderDetailsDeliveredTv.setTextColor(requireActivity()
                                .getResources().getColor(R.color.green));
                        binding.orderDetailsDeliveredIv.setEnabled(false);
                    }
                });
    }

    private void initAdapter(List<OrderProducts> orderProductList) {
        OrderDetailsAdapter adapter =
                new OrderDetailsAdapter(requireActivity(), orderProductList);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvOrderDetails.setLayoutManager(mLayoutManager);
        binding.rvOrderDetails.setAdapter(adapter);
    }
}