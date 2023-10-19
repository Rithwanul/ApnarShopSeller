package com.bikroybaba.seller.ui.report;

import static com.bikroybaba.seller.ui.order.DateFragment.fromDateStartHour;
import static com.bikroybaba.seller.ui.order.DateFragment.toDateLastHour;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.FragmentDateBinding;
import com.bikroybaba.seller.model.remote.request.ReportRequest;
import com.bikroybaba.seller.util.CalendarView;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.ReportViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;


public class DateReportFragment extends Fragment {

    private FragmentDateBinding binding;
    private SimpleDateFormat monthFormat, dayFormat, dateFormat, yearFormat;
    private String month, year, day_date;
    private ReportViewModel viewModel;
    private Utility utility;
    private String fromDate, toDate;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ReportRequest reportRequest =
                    new ReportRequest(intent.getStringExtra("categoryId"),
                            intent.getStringExtra("productTypeId"),
                            intent.getStringExtra("productId"),
                            "DATE", fromDate, toDate);
            viewModel.getReport(reportRequest);
        }
    };

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_date, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        binding.include.reportTime.setVisibility(View.GONE);
        utility = new Utility(getActivity());
        monthFormat = new SimpleDateFormat("MMM");
        yearFormat = new SimpleDateFormat("yyyy");
        dayFormat = new SimpleDateFormat("EEEE");
        dateFormat = new SimpleDateFormat("dd");
        getSevenDaysAgoDate();
        getTodaysDate();
        clickEvent();
        observeReportResponse();
        changeLanguage();
        binding.dateSwipeRefresh.setOnRefreshListener(() -> {
            binding.dateSwipeRefresh.setRefreshing(false);
            ReportRequest reportRequest1 =
                    new ReportRequest(utility.getCategoryId(), utility.getProductTypeId(),
                            utility.getProductId(), "DATE", fromDate, toDate);
            viewModel.getReport(reportRequest1);
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
    }

    private void getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        toDate = String.valueOf(today.getTime());
        month = monthFormat.format(today);
        year = yearFormat.format(today);
        binding.dateToDateDay.setText(dayFormat.format(today));
        day_date = dateFormat.format(today);
        binding.dateToDateDate.setText(day_date);
        switch (day_date) {
            case "01":
                binding.dateToDateMonthYear.setText(String.format("ST %s %s", month, year));
                break;
            case "02":
                binding.dateToDateMonthYear.setText(String.format("ND %s %s", month, year));
                break;
            case "03":
                binding.dateToDateMonthYear.setText(String.format("RD %s %s", month, year));
                break;
            default:
                binding.dateToDateMonthYear.setText(String.format("TH %s %s", month, year));
                break;
        }
    }

    // get 7 days ago date
    private void getSevenDaysAgoDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -6);
        Date sevenDaysBeforeDate = cal.getTime();
        fromDate = String.valueOf(sevenDaysBeforeDate.getTime());
        month = monthFormat.format(sevenDaysBeforeDate);
        year = yearFormat.format(sevenDaysBeforeDate);
        binding.dateFromDateDay.setText(dayFormat.format(sevenDaysBeforeDate));
        day_date = dateFormat.format(sevenDaysBeforeDate);
        binding.dateDate.setText(day_date);
        switch (day_date) {
            case "01":
                binding.dateFromDateMonthYear.setText(String.format("ST %s %s", month, year));
                break;
            case "02":
                binding.dateFromDateMonthYear.setText(String.format("ND %s %s", month, year));
                break;
            case "03":
                binding.dateFromDateMonthYear.setText(String.format("RD %s %s", month, year));
                break;
            default:
                binding.dateFromDateMonthYear.setText(String.format("TH %s %s", month, year));
                break;
        }
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

    //All type of click event
    private void clickEvent() {
        binding.dateFromDate.setOnClickListener(v -> {
            openCalenderDialog(binding.dateFromDateDay, binding.dateDate,
                    binding.dateFromDateMonthYear, "fromDate");
        });
        binding.dateToDate.setOnClickListener(v -> {
            openCalenderDialog(binding.dateToDateDay, binding.dateToDateDate,
                    binding.dateToDateMonthYear, "toDate");
        });
    }

    //Open calender dialog
    private void openCalenderDialog(TextView completeOrderDay, TextView completeOrderDate,
                                    TextView completeOrderMonthYear, String from_Date) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.calendar_view);
        HashSet<Date> events = new HashSet<>();
        events.add(new Date());
        CalendarView cv = dialog.findViewById(R.id.calendar_view1);
        ImageView imageView = dialog.findViewById(R.id.control_calender_cancel);

        cv.updateCalendar(events);
        imageView.setOnClickListener(view -> {
            dialog.dismiss();
        });
        // assign event handler
        cv.setEventHandler(date -> {
            // show returned day
            if (from_Date.equalsIgnoreCase("fromDate")) {
                completeOrderDay.setText(dayFormat.format(date));
                java.util.Date date1 = fromDateStartHour(date);
                fromDate = String.valueOf(date1.getTime());
                utility.logger("from" + fromDate);
                ReportRequest reportRequest1 =
                        new ReportRequest(utility.getCategoryId(), utility.getProductTypeId(),
                                utility.getProductId(), "DATE", fromDate, toDate);
                viewModel.getReport(reportRequest1);
            } else if (from_Date.equalsIgnoreCase("toDate")) {
                completeOrderDay.setText(dayFormat.format(date));
                java.util.Date date1 = toDateLastHour(date);
                toDate = String.valueOf(date1.getTime());
                utility.logger("to" + toDate);
                ReportRequest reportRequest1 =
                        new ReportRequest(utility.getCategoryId(), utility.getProductTypeId(),
                                utility.getProductId(), "DATE", fromDate, toDate);
                viewModel.getReport(reportRequest1);
            }
            completeOrderDay.setText(dayFormat.format(date));
            day_date = dateFormat.format(date);
            completeOrderDate.setText(day_date);
            month = monthFormat.format(date);
            year = yearFormat.format(date);
            switch (day_date) {
                case "01":
                    completeOrderMonthYear.setText(String.format("ST %s %s", month, year));
                    break;
                case "02":
                    completeOrderMonthYear.setText(String.format("ND %s %s", month, year));
                    break;
                case "03":
                    completeOrderMonthYear.setText(String.format("RD %s %s", month, year));
                    break;
                default:
                    completeOrderMonthYear.setText(String.format("TH %s %s", month, year));
                    break;
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    private void observeReportResponse() {
        viewModel.getReportResponse().observe(requireActivity(), reportResponse -> {
            binding.include.newProductCreatedTotal.setText(reportResponse.getNewProductCreated());
            binding.include.newProductCreatedTk.setText(String.format("%s Tk", reportResponse.getNewProductCreatedPrice()));
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
                        utility.getProductId(), "DATE", fromDate, toDate);
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