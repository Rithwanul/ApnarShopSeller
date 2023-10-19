package com.bikroybaba.seller.ui.order;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.CompleteOrderAdapter;
import com.bikroybaba.seller.databinding.FragmentDate2Binding;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.remote.request.OrderListRequest;
import com.bikroybaba.seller.model.remote.response.OrderListResponse;
import com.bikroybaba.seller.util.CalendarView;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.OrderViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import io.reactivex.rxjava3.disposables.Disposable;

public class DateFragment extends Fragment implements EmptyListInterface {

    private FragmentDate2Binding binding;
    private SimpleDateFormat monthFormat, dayFormat, dateFormat, yearFormat;
    private String month, year, day_date;
    private OrderViewModel viewModel;
    private Utility utility;
    private String fromDate, toDate;
    private String searchKey = "";
    private Disposable disposable;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String search = intent.getStringExtra("search");
            binding.dateSwipeRefresh.setVisibility(View.VISIBLE);
            binding.dateSwipeRefresh.setRefreshing(true);
            searchKey = search;
            observeOrderList(fromDate, toDate);
           /* Observable<String> observable = Observable.create((ObservableOnSubscribe<String>) emitter -> {

                if (!emitter.isDisposed()) {
                    emitter.onNext(search);
                }
            }).debounce(1000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            observable.subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    disposable = d;
                }

                @Override
                public void onNext(@NonNull String s) {

                    if (s.length() > 0) {
                        searchKey = s;
                        observeOrderList(fromDate,toDate);
                        utility.hideKeyboard(binding.getRoot());
                    } else {
                        searchKey = "";
                        observeOrderList(fromDate,toDate);
                        utility.hideKeyboard(binding.getRoot());
                        disposable.dispose();
                    }

                }

                @Override
                public void onError(@NonNull Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });*/
        }
    };

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_date2, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        initView();
        utility = new Utility(getActivity());
        getSevenDaysAgoDate();
        getTodayDate();
        if (utility.isNetworkAvailable()) {
            binding.dateSwipeRefresh.setRefreshing(true);
            binding.noInternetLayout.setVisibility(View.GONE);
            if (getSearchKey() != null) {
                searchKey = getSearchKey();
                observeOrderList(fromDate, toDate);
            } else {
                observeOrderList(fromDate, toDate);
            }
        } else {
            binding.dateSwipeRefresh.setVisibility(View.GONE);
            binding.noInternetLayout.setVisibility(View.VISIBLE);
        }
        clickEvent();
        binding.dateSwipeRefresh.setOnRefreshListener(() -> {
            if (utility.isNetworkAvailable()) {
                binding.dateSwipeRefresh.setRefreshing(true);
                binding.noInternetLayout.setVisibility(View.GONE);
                observeOrderList(fromDate, toDate);
            } else {
                binding.dateSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
            }
        });

        binding.tryAgain.setOnClickListener(v -> {
            if (utility.isNetworkAvailable()) {
                binding.dateSwipeRefresh.setRefreshing(true);
                binding.noInternetLayout.setVisibility(View.GONE);
                observeOrderList(fromDate, toDate);
            } else {
                binding.dateSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getOrderListError().observe(requireActivity(), s -> {
            if (getViewLifecycleOwner().getLifecycle().getCurrentState() ==
                    Lifecycle.State.RESUMED) {
                utility.showDialog(s);
                binding.dateSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void initView() {
        monthFormat = new SimpleDateFormat("MMM");
        yearFormat = new SimpleDateFormat("yyyy");
        dayFormat = new SimpleDateFormat("EEEE");
        dateFormat = new SimpleDateFormat("dd");
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);
    }

    private void observeOrderList(String fromDate, String toDate) {
        OrderListRequest orderListRequest =
                new OrderListRequest(KeyWord.TYPE_ALL, KeyWord.KEYWORD_DATE, fromDate, toDate, searchKey);
        viewModel.getOrderList(orderListRequest, DateFragment.this)
                .observe(requireActivity(), this::initAdapter);
    }

    private void initAdapter(PagedList<OrderListResponse> orderList) {
        binding.dateSwipeRefresh.setRefreshing(false);
        CompleteOrderAdapter adapter = new CompleteOrderAdapter(getActivity(), utility);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvDate.setLayoutManager(mLayoutManager);
        adapter.submitList(orderList);
        binding.rvDate.setAdapter(adapter);
    }

    private void getTodayDate() {
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
        cal.setTime(new java.util.Date());
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

    @Override
    public void onResume() {
        super.onResume();
        registerBroadcastReceiver("com.aapnarshop.seller.completeOrder", broadcastReceiver);
    }

    //Open calender dialog
    private void openCalenderDialog(TextView completeOrderDay, TextView completeOrderDate,
                                    TextView completeOrderMonthYear, String from_Date) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.calendar_view);
        HashSet<java.util.Date> events = new HashSet<>();
        events.add(new java.util.Date());
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
                observeOrderList(fromDate, toDate);
            } else if (from_Date.equalsIgnoreCase("toDate")) {
                completeOrderDay.setText(dayFormat.format(date));
                java.util.Date date1 = toDateLastHour(date);
                toDate = String.valueOf(date1.getTime());
                observeOrderList(fromDate, toDate);
            }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(broadcastReceiver);
    }

    private void registerBroadcastReceiver(String s, BroadcastReceiver broadcastReceiver) {
        IntentFilter intentFilter = new IntentFilter(s);
        requireActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    private String getSearchKey() {
        SharedPreferences preferences =
                requireActivity().getSharedPreferences("search", Context.MODE_PRIVATE);
        return preferences.getString("key", "");
    }

    public static java.util.Date toDateLastHour(java.util.Date date1) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 59);
        return cal.getTime();
    }

    public static java.util.Date fromDateStartHour(java.util.Date date1) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public void order(int size) {
        if (size == 0) {
            binding.dateSwipeRefresh.setVisibility(View.GONE);
            binding.noOrderFound.setVisibility(View.VISIBLE);
        } else {
            binding.dateSwipeRefresh.setVisibility(View.VISIBLE);
            binding.noOrderFound.setVisibility(View.GONE);
        }
    }
}