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
import com.bikroybaba.seller.databinding.FragmentLastYearBinding;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.remote.request.OrderListRequest;
import com.bikroybaba.seller.model.remote.response.OrderListResponse;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.OrderViewModel;

import io.reactivex.rxjava3.disposables.Disposable;


public class LastYearFragment extends Fragment implements EmptyListInterface {

    private FragmentLastYearBinding binding;
    private OrderViewModel viewModel;
    private Utility utility;
    private String searchKey = "";
    private Disposable disposable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_last_year, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        utility = new Utility(getActivity());
        if (utility.isNetworkAvailable()) {
            binding.lastYearSwipeRefresh.setRefreshing(true);
            binding.noInternetLayout.setVisibility(View.GONE);
            if (getSearchKey() != null) {
                searchKey = getSearchKey();
                observeOrderList();
            } else {
                observeOrderList();
            }
        } else {
            binding.lastYearSwipeRefresh.setVisibility(View.GONE);
            binding.noInternetLayout.setVisibility(View.VISIBLE);
        }
        binding.lastYearSwipeRefresh.setOnRefreshListener(() -> {
            if (utility.isNetworkAvailable()) {
                binding.lastYearSwipeRefresh.setRefreshing(true);
                binding.noInternetLayout.setVisibility(View.GONE);
                observeOrderList();
            } else {
                binding.lastYearSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
            }
        });
        binding.tryAgain.setOnClickListener(v -> {
            if (utility.isNetworkAvailable()) {
                binding.lastYearSwipeRefresh.setRefreshing(true);
                binding.noInternetLayout.setVisibility(View.GONE);
                observeOrderList();
            } else {
                binding.lastYearSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getOrderListError().observe(requireActivity(), s -> {
            if (getViewLifecycleOwner().getLifecycle().getCurrentState() ==
                    Lifecycle.State.RESUMED) {
                utility.showDialog(s);
                binding.lastYearSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);
    }

    private void observeOrderList() {
        OrderListRequest orderListRequest =
                new OrderListRequest(KeyWord.TYPE_ALL, KeyWord.KEYWORD_YEAR, "",
                        "", searchKey);
        viewModel.getOrderList(orderListRequest, LastYearFragment.this)
                .observe(requireActivity(), orderListResponses -> {
            initAdapter(orderListResponses);
            binding.lastYearSwipeRefresh.setVisibility(View.VISIBLE);
            binding.noOrderFound.setVisibility(View.GONE);
        });
    }

    private void initAdapter(PagedList<OrderListResponse> orderList) {
        binding.lastYearSwipeRefresh.setRefreshing(false);
        CompleteOrderAdapter adapter = new CompleteOrderAdapter(getActivity(), utility);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvLastYear.setLayoutManager(mLayoutManager);
        binding.rvLastYear.setHasFixedSize(true);
        binding.rvLastYear.setItemViewCacheSize(100);
        binding.rvLastYear.setAdapter(adapter);
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
            binding.lastYearSwipeRefresh.setVisibility(View.VISIBLE);
            binding.lastYearSwipeRefresh.setRefreshing(true);
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
            binding.lastYearSwipeRefresh.setVisibility(View.GONE);
            binding.noOrderFound.setVisibility(View.VISIBLE);
        } else {
            binding.lastYearSwipeRefresh.setVisibility(View.VISIBLE);
            binding.noOrderFound.setVisibility(View.GONE);
        }
    }
}