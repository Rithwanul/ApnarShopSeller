package com.bikroybaba.seller.ui.report;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.FragmentYearlyBinding;
import com.bikroybaba.seller.model.remote.request.ReportRequest;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.ReportViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;


public class YearlyReportFragment extends Fragment {

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
    private FragmentYearlyBinding binding;
    private Utility utility;
    private ReportViewModel viewModel;

    public YearlyReportFragment() {
        // Required empty public constructor
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ReportRequest reportRequest =
                    new ReportRequest(intent.getStringExtra("categoryId"),
                            intent.getStringExtra("productTypeId"),
                            intent.getStringExtra("productId"),
                            "YEAR", "", "");
            viewModel.getReport(reportRequest);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_yearly, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        utility = new Utility(getActivity());
        changeLanguage();
        observeReportResponse();
        binding.yearlySwipeRefresh.setOnRefreshListener(() -> {
            binding.yearlySwipeRefresh.setRefreshing(false);
            ReportRequest reportRequest1 =
                    new ReportRequest(utility.getCategoryId(), utility.getProductTypeId(),
                            utility.getProductId(), "YEAR", "", "");
            viewModel.getReport(reportRequest1);
            observeReportResponse();
        });

    }

    private void changeLanguage() {
        binding.include.newProduct.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.new_product_created : R.string.new_product_created_bn);
        binding.include.productSold.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.product_sold : R.string.product_sold_bn);
        binding.include.totalOrder.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.total_order : R.string.total_order_bn);
        binding.include.deliverdOrder.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.delivered_order : R.string.delivered_order_bn);
        binding.include.cancelOrder.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.cancelled_order : R.string.cancelled_order_bn);
        binding.include.totalRatingTv.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.total_rating : R.string.total_rating_bn);
        binding.include.totalVisitTv.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.total_visit : R.string.total_visit_bn);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
    }

    private void observeReportResponse() {
        viewModel.getReportResponse().observe(requireActivity(), reportResponse -> {
            binding.include.newProductCreatedTotal.setText(reportResponse.getNewProductCreated());
            binding.include.newProductCreatedTk.setText(String.format("%s Tk", reportResponse.getNewProductCreatedPrice()));
            binding.include.reportTime.setText(String.format("%s - %s", sdf.format(new Date(Long.parseLong(reportResponse.getStartTime()))), sdf.format(new Date(Long.parseLong(reportResponse.getEndTime())))));
            binding.include.productSoldTotal.setText(reportResponse.getProductSold());
            binding.include.productSoldTk.setText(String.format("%s Tk", reportResponse.getProductSoldPrice()));
            binding.include.totalOrderTotal.setText(reportResponse.getTotalOrder());
            binding.include.totalOrderTk.setText(String.format("%s Tk", reportResponse.getTotalOrderPrice()));
            binding.include.deliveredOrderTotal.setText(reportResponse.getDeliveredOrder());
            binding.include.deliveredOrderTk.setText(String.format("%s Tk", reportResponse.getDeliveredOrderPrice()));
            binding.include.cancelOrderTotal.setText(reportResponse.getCancelledOrder());
            binding.include.cancelOrderTk.setText(String.format("%s Tk", reportResponse.getCancelledOrderedPrice()));
            binding.include.totalRatingTotal.setText(reportResponse.getTotalRating());
            binding.include.totalRating.setText(reportResponse.getTotalRatingAvg());
            binding.include.totalVisit.setText(reportResponse.getTotalVisit());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ReportRequest reportRequest1 =
                new ReportRequest(utility.getCategoryId(), utility.getProductTypeId(),
                        utility.getProductId(), "YEAR", "", "");
        viewModel.getReport(reportRequest1);
        IntentFilter intentFilter = new IntentFilter("com.aapnarshop.seller.VIEW.FRAGMENT.report");
        requireActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        requireActivity().unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

}