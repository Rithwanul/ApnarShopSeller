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
import com.bikroybaba.seller.databinding.FragmentAllBinding;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.remote.request.OrderListRequest;
import com.bikroybaba.seller.model.remote.response.OrderListResponse;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.OrderViewModel;

import io.reactivex.rxjava3.disposables.Disposable;

public class AllFragment extends Fragment implements EmptyListInterface {

    private FragmentAllBinding binding;
    private OrderViewModel viewModel;
    private Utility utility;
    private Disposable disposable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_all, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        utility = new Utility(getActivity());
        if (utility.isNetworkAvailable()) {
            binding.allSwipeRefresh.setVisibility(View.VISIBLE);
            binding.allSwipeRefresh.setRefreshing(true);
            if (getSearchKey() != null) {
                observeOrderList(getSearchKey());
            } else {
                observeOrderList("");
            }
            binding.noInternetLayout.setVisibility(View.GONE);
        } else {
            binding.allSwipeRefresh.setVisibility(View.GONE);
            binding.noInternetLayout.setVisibility(View.VISIBLE);
        }

        binding.allSwipeRefresh.setOnRefreshListener(() -> {
            if (utility.isNetworkAvailable()) {
                binding.allSwipeRefresh.setRefreshing(true);
                observeOrderList("");
                binding.noInternetLayout.setVisibility(View.GONE);
            } else {
                binding.allSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
            }
        });
        binding.tryAgain.setOnClickListener(v -> {
            if (utility.isNetworkAvailable()) {
                binding.allSwipeRefresh.setRefreshing(true);
                observeOrderList("");
                binding.noInternetLayout.setVisibility(View.GONE);
            } else {
                binding.allSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getOrderListError().observe(requireActivity(), s -> {
            if (getViewLifecycleOwner().getLifecycle().getCurrentState() ==
                    Lifecycle.State.RESUMED) {
                utility.showDialog(s);
                binding.allSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);
    }

    private void observeOrderList(String searchKey) {
        OrderListRequest orderListRequest =
                new OrderListRequest(KeyWord.TYPE_ALL, KeyWord.KEYWORD_ALL, "", "",
                        searchKey);
        viewModel.getOrderList(orderListRequest, AllFragment.this)
                .observe(requireActivity(), this::initAdapter);
    }

    private void initAdapter(PagedList<OrderListResponse> orderList) {
        binding.allSwipeRefresh.setRefreshing(false);
        CompleteOrderAdapter adapter = new CompleteOrderAdapter(getActivity(), utility);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvAll.setLayoutManager(mLayoutManager);
        adapter.submitList(orderList);
        binding.rvAll.setAdapter(adapter);
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
            binding.allSwipeRefresh.setRefreshing(true);
            observeOrderList(search);
           /* Observable<String> observable = Observable.create((ObservableOnSubscribe<String>) emitter -> {

                if (!emitter.isDisposed()) {
                    emitter.onNext(search);
                }
            }).debounce(1000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            observable.subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    disposable =d;
                }

                @Override
                public void onNext(@NonNull String s) {

                    if (s.length()>0){
                        searchKey = s;
                        observeOrderList();
                        utility.hideKeyboard(binding.getRoot());
                        Log.d("test", "onNext: "+searchKey);
                    }else {
                        searchKey = "";
                        observeOrderList();
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

    BroadcastReceiver orderListBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int listSize = intent.getIntExtra("orderList", -1);
            if (listSize == 0) {
                binding.allSwipeRefresh.setVisibility(View.GONE);
                binding.noOrderFound.setVisibility(View.VISIBLE);
            } else {
                binding.allSwipeRefresh.setVisibility(View.VISIBLE);
                binding.noOrderFound.setVisibility(View.GONE);
            }
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
            binding.allSwipeRefresh.setVisibility(View.GONE);
            binding.noOrderFound.setVisibility(View.VISIBLE);
        } else {
            binding.allSwipeRefresh.setVisibility(View.VISIBLE);
            binding.noOrderFound.setVisibility(View.GONE);
        }
    }
}