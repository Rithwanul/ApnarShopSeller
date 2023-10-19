package com.bikroybaba.seller.ui.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.bikroybaba.seller.databinding.FragmentLastweekBinding;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.remote.request.OrderListRequest;
import com.bikroybaba.seller.model.remote.response.OrderListResponse;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.OrderViewModel;

import io.reactivex.rxjava3.disposables.Disposable;

public class LastWeekFragment extends Fragment implements EmptyListInterface {

    private FragmentLastweekBinding binding;
    private OrderViewModel viewModel;
    private Utility utility;
    private String searchKey = "";
    private Disposable disposable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_lastweek, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        utility = new Utility(getActivity());
        if (utility.isNetworkAvailable()) {
            binding.lastWeekSwipeRefresh.setRefreshing(true);
            binding.noInternetLayout.setVisibility(View.GONE);
            if (getSearchKey() != null) {
                searchKey = getSearchKey();
                observeOrderList();
            } else {
                observeOrderList();
            }
        } else {
            binding.lastWeekSwipeRefresh.setVisibility(View.GONE);
            binding.noInternetLayout.setVisibility(View.VISIBLE);
        }
        binding.lastWeekSwipeRefresh.setOnRefreshListener(() -> {
            if (utility.isNetworkAvailable()) {
                binding.lastWeekSwipeRefresh.setRefreshing(true);
                binding.noInternetLayout.setVisibility(View.GONE);
                observeOrderList();
            } else {
                binding.lastWeekSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
            }
        });
        binding.tryAgain.setOnClickListener(v -> {
            if (utility.isNetworkAvailable()) {
                binding.lastWeekSwipeRefresh.setRefreshing(true);
                binding.noInternetLayout.setVisibility(View.GONE);
                observeOrderList();
            } else {
                binding.lastWeekSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getOrderListError().observe(requireActivity(), s -> {
            if (s != null) {
                if (getViewLifecycleOwner().getLifecycle().getCurrentState() ==
                        Lifecycle.State.RESUMED) {
                    utility.showDialog(s);
                    binding.lastWeekSwipeRefresh.setRefreshing(false);
                }
            }
        });

    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);
    }

    private void observeOrderList() {
        OrderListRequest orderListRequest =
                new OrderListRequest(KeyWord.TYPE_ALL, KeyWord.KEYWORD_WEEK, "",
                        "", searchKey);
        viewModel.getOrderList(orderListRequest, LastWeekFragment.this)
                .observe(requireActivity(), orderListResponses -> {
            initAdapter(orderListResponses);
            binding.lastWeekSwipeRefresh.setVisibility(View.VISIBLE);
            binding.noOrderFound.setVisibility(View.GONE);
        });

    }

    private void initAdapter(PagedList<OrderListResponse> orderList) {
        binding.lastWeekSwipeRefresh.setRefreshing(false);
        CompleteOrderAdapter adapter = new CompleteOrderAdapter(getActivity(), utility);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvLastWeek.setLayoutManager(mLayoutManager);
        binding.rvLastWeek.setAdapter(adapter);
        adapter.submitList(orderList);
    }


    @Override
    public void onResume() {
        super.onResume();
        registerBroadcastReceiver("com.aapnarshop.seller.completeOrder", broadcastReceiver);

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String search = intent.getStringExtra("search");
            binding.lastWeekSwipeRefresh.setVisibility(View.VISIBLE);
            binding.lastWeekSwipeRefresh.setRefreshing(true);
            searchKey = search;
            observeOrderList();
        }
    };


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

    @Override
    public void order(int size) {
        if (size == 0) {
            binding.lastWeekSwipeRefresh.setVisibility(View.GONE);
            binding.noOrderFound.setVisibility(View.VISIBLE);
        } else {
            binding.lastWeekSwipeRefresh.setVisibility(View.VISIBLE);
            binding.noOrderFound.setVisibility(View.GONE);
        }
    }
}