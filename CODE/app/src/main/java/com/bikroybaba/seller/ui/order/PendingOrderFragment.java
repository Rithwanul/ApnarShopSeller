package com.bikroybaba.seller.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.PendingOrderAdapter;
import com.bikroybaba.seller.databinding.FragmentPendingOrderBinding;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.remote.request.OrderListRequest;
import com.bikroybaba.seller.model.remote.response.OrderListResponse;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.OrderViewModel;
import com.google.android.material.textview.MaterialTextView;


public class PendingOrderFragment extends Fragment implements EmptyListInterface {

    private FragmentPendingOrderBinding binding;
    private OrderViewModel viewModel;
    private Utility utility;
    private String language;
    private MaterialTextView textToolHeader;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_pending_order, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       initViewModel();
        utility = new Utility(getActivity());
        language = utility.getLangauge();
        if (utility.isNetworkAvailable()) {
            binding.pendingOrderSwipeRefresh.setVisibility(View.VISIBLE);
            binding.pendingOrderSwipeRefresh.setRefreshing(true);
            observeOrderList();
            binding.noInternetLayout.setVisibility(View.GONE);
        } else {
            binding.pendingOrderSwipeRefresh.setVisibility(View.GONE);
            binding.noInternetLayout.setVisibility(View.VISIBLE);
        }

        viewModel.getOrderStatusResponse().observe(requireActivity(), api_response -> {
            if (api_response.getCode() == 200) {
                observeOrderList();
            }
        });

        binding.pendingOrderSwipeRefresh.setOnRefreshListener(() -> {
            if (utility.isNetworkAvailable()) {
                binding.pendingOrderSwipeRefresh.setVisibility(View.VISIBLE);
                binding.pendingOrderSwipeRefresh.setRefreshing(true);
                observeOrderList();
                binding.noInternetLayout.setVisibility(View.GONE);
            } else {
                binding.pendingOrderSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
            }
        });

        binding.tryAgain.setOnClickListener(v -> {
            if (utility.isNetworkAvailable()) {
                binding.pendingOrderSwipeRefresh.setVisibility(View.VISIBLE);
                binding.pendingOrderSwipeRefresh.setRefreshing(true);
                observeOrderList();
                binding.noInternetLayout.setVisibility(View.GONE);
            } else {
                binding.pendingOrderSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getOrderListError().observe(requireActivity(), s -> {
            if (getViewLifecycleOwner().getLifecycle().getCurrentState() ==
                    Lifecycle.State.RESUMED) {
                utility.showDialog(s);
                binding.pendingOrderSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);
    }

    private void changeLanguage(String language) {
        textToolHeader.setText(language.equalsIgnoreCase(KeyWord.BANGLA) ? KeyWord.PENDING_ORDER_BN : KeyWord.PENDING_ORDER);

    }

    private void observeOrderList() {
        OrderListRequest orderListRequest =
                new OrderListRequest(KeyWord.TYPE_PENDING, KeyWord.TYPE_ALL, "",
                        "", "");
        viewModel.getOrderList(orderListRequest, PendingOrderFragment.this)
                .observe(requireActivity(), this::initAdapter);
    }

    private void initAdapter(PagedList<OrderListResponse> orderList) {
        binding.pendingOrderSwipeRefresh.setRefreshing(false);
        PendingOrderAdapter adapter = new PendingOrderAdapter(getActivity(), viewModel, utility);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.pendingOrderRv.setLayoutManager(mLayoutManager);
        adapter.submitList(orderList);
        binding.pendingOrderRv.setAdapter(adapter);
    }

    @Override
    public void order(int size) {
        if (size == 0) {
            binding.pendingOrderSwipeRefresh.setVisibility(View.GONE);
            binding.pendingOrderNoOrderFound.setVisibility(View.VISIBLE);
        } else {
            binding.pendingOrderSwipeRefresh.setVisibility(View.VISIBLE);
            binding.pendingOrderNoOrderFound.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable
                                              Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        textToolHeader = toolbar.findViewById(R.id.title);
        changeLanguage(language);
    }
}