package com.bikroybaba.seller.ui.offer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.GenericAdapter;
import com.bikroybaba.seller.adapter.OfferListAdapter;
import com.bikroybaba.seller.databinding.FragmentRunningOfferBinding;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.remote.request.OfferListRequest;
import com.bikroybaba.seller.model.remote.response.OfferDetailsResponse;
import com.bikroybaba.seller.model.remote.response.OfferTypeResponse;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.OfferViewModel;
import com.moktar.zmvvm.base.base.BaseFragment;

import java.util.List;

public class RunningOfferFragment extends BaseFragment<OfferViewModel, FragmentRunningOfferBinding>
        implements EmptyListInterface {

    private Utility utility;
    private String shopId;
    private String offerType = "";

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String searchKey = intent.getStringExtra("search");
            bindingView.runningSwipeRefresh.setRefreshing(true);
            OfferListRequest offerListRequest =
                    new OfferListRequest(KeyWord.TIMELINE_RUNNING, offerType, searchKey, shopId);
            viewModel.getOfferDetailsList(offerListRequest, RunningOfferFragment.this)
                    .observe(requireActivity(), offerListResponses -> {
                        initAdapter(offerListResponses);
                    });
        }
    };


    @Override
    public int setContent() {
        return R.layout.fragment_running_offer;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showContentView();
        utility = new Utility(getActivity());
        bindingView.runningSwipeRefresh.setRefreshing(true);
        viewModel.getUserLivedata().observe(requireActivity(), userProfile -> shopId = userProfile.getShopId());
        getOfferTypeList();
        bindingView.tryAgain.setOnClickListener(v -> getOfferTypeList());
        bindingView.runningSwipeRefresh.setOnRefreshListener(this::getOfferTypeList);
    }


    private void getOfferTypeList() {
        if (utility.isNetworkAvailable()) {
            viewModel.getOfferTypeList().observe(requireActivity(), this::showOfferType);
            bindingView.noInternetLayout.setVisibility(View.GONE);
        } else {
            bindingView.runningSwipeRefresh.setVisibility(View.GONE);
            bindingView.noInternetLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showOfferType(List<OfferTypeResponse> offerTypeResponseList) {
        GenericAdapter<OfferTypeResponse> adapter =
                new GenericAdapter<>(requireActivity(), offerTypeResponseList) {
                    @Override
                    public View getCustomView(int position, View view, ViewGroup parent) {
                        OfferTypeResponse model = getItem(position);
                        View spinnerRow = LayoutInflater.from(parent.getContext())
                                .inflate(com.bikroybaba.seller.R.layout.custom_spinner_item, parent, false);
                        TextView label = spinnerRow.findViewById(com.bikroybaba.seller.R.id.custom_spinner);
                        if (model != null) {
                            label.setText(utility.firstTextCapitalize(model.getOfferTypeTitle()));
                        }
                        return spinnerRow;
                    }
                };
        bindingView.runningOfferOfferTypeDropdown.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        bindingView.runningOfferOfferTypeDropdown
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        offerType = offerTypeResponseList.get(position).getOfferTypeId();
                        OfferListRequest offerListRequest =
                                new OfferListRequest(KeyWord.TIMELINE_RUNNING,
                                        offerTypeResponseList.get(position).getOfferTypeId(), "",
                                        shopId);
                        viewModel.getOfferDetailsList(offerListRequest, RunningOfferFragment.this)
                                .observe(requireActivity(), offerListResponses -> initAdapter(offerListResponses));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
    }


    @Override
    public void order(int size) {
        if (size == 0) {
            bindingView.runningSwipeRefresh.setVisibility(View.GONE);
            bindingView.noProductFound.setVisibility(View.VISIBLE);
        } else {
            bindingView.runningSwipeRefresh.setVisibility(View.VISIBLE);
            bindingView.noProductFound.setVisibility(View.GONE);
        }
    }

    private void initAdapter(PagedList<OfferDetailsResponse> offerDetailsResponses) {
        bindingView.runningSwipeRefresh.setRefreshing(false);
        OfferListAdapter adapter = new OfferListAdapter(getActivity(), viewModel);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        bindingView.runningRv.setLayoutManager(mLayoutManager);
        bindingView.runningRv.setAdapter(adapter);
        adapter.submitList(offerDetailsResponses);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("com.aapnarshop.seller.offerlist");
        requireActivity().registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(broadcastReceiver);
    }
}