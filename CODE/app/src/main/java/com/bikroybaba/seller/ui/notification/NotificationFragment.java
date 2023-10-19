package com.bikroybaba.seller.ui.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.NotificationAdapter;
import com.bikroybaba.seller.databinding.FragmentNotificationBinding;
import com.bikroybaba.seller.model.remote.response.NotificationResponse;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.NotificationViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private final List<NotificationResponse> notificationList = new ArrayList<>();
    private final List<NotificationResponse> tempNotificationList = new ArrayList<>();
    private Utility utility;
    private String language;
    private FragmentNotificationBinding binding;
    private NotificationViewModel notificationViewModel;
    private FrameLayout notificationLayout;
    private TextView notificationBadge;
    private Toolbar toolbar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_notification, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utility = new Utility(getActivity());
        initViewModel();
        language = utility.getLangauge();
        binding.notificationSwipeRefresh.setRefreshing(true);
        if (utility.isNetworkAvailable()) {
            observeNotificationResponse();
        } else {
            binding.notificationSwipeRefresh.setVisibility(View.GONE);
            binding.noInternetLayout.setVisibility(View.VISIBLE);
            binding.tryAgain.setText(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.try_again_bn) : getString(R.string.try_again));
//            binding.noInternetTv.setText(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.no_internet_connection_bn) : getString(R.string.no_internet_connection));
        }
        binding.notificationSwipeRefresh.setOnRefreshListener(() -> {
            if (utility.isNetworkAvailable()) {
                binding.notificationSwipeRefresh.setVisibility(View.VISIBLE);
                binding.noInternetLayout.setVisibility(View.GONE);
                observeNotificationResponse();
            } else {
                binding.notificationSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
                binding.tryAgain.setText(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.try_again_bn) : getString(R.string.try_again));
//                binding.noInternetTv.setText(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.no_internet_connection_bn) : getString(R.string.no_internet_connection));
            }
        });
        binding.tryAgain.setOnClickListener(v -> {
            if (utility.isNetworkAvailable()) {
                binding.notificationSwipeRefresh.setVisibility(View.VISIBLE);
                binding.noInternetLayout.setVisibility(View.GONE);
                observeNotificationResponse();
            } else {
                binding.notificationSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
                binding.tryAgain.setText(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.try_again_bn) : getString(R.string.try_again));
//                binding.noInternetTv.setText(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.no_internet_connection_bn) : getString(R.string.no_internet_connection));
            }
        });
        notificationViewModel.getUpdatedNotificationResponse().observe(requireActivity(), api_response -> {
            if (api_response != null && api_response.getCode() == 200) {
                if (utility.isNetworkAvailable()) {
                    binding.notificationSwipeRefresh.setVisibility(View.VISIBLE);
                    binding.noInternetLayout.setVisibility(View.GONE);
                    observeNotificationResponse();
                } else {
                    binding.notificationSwipeRefresh.setVisibility(View.GONE);
                    binding.noInternetLayout.setVisibility(View.VISIBLE);
                    binding.tryAgain.setText(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.try_again_bn) : getString(R.string.try_again));
//                    binding.noInternetTv.setText(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.no_internet_connection_bn) : getString(R.string.no_internet_connection));
                }
            }
        });

    }

    private void initViewModel() {
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
    }

    private void observeNotificationResponse() {
        notificationViewModel.getNotification().observe(requireActivity(), notificationResponseList -> {
            if (notificationResponseList.size() > 0) {
                initAdapter(notificationResponseList);
                binding.notificationSwipeRefresh.setVisibility(View.VISIBLE);
                binding.noInternetLayout.setVisibility(View.GONE);
                binding.notificationSwipeRefresh.setRefreshing(false);
                binding.noDataFound.setVisibility(View.GONE);
            } else {
                binding.notificationSwipeRefresh.setVisibility(View.GONE);
                binding.noDataFound.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initAdapter(List<NotificationResponse> notificationResponseList) {
        tempNotificationList.clear();
        for (NotificationResponse notificationResponse : notificationResponseList) {
            if (notificationResponse.getReadStatus().equalsIgnoreCase(KeyWord.UNREAD)) {
                tempNotificationList.add(notificationResponse);
            }
        }
        if (tempNotificationList.size() > 99) {
            notificationBadge.setText(99 + "+");
        } else {
            notificationBadge.setText(String.valueOf(tempNotificationList.size()));
        }
        notificationList.clear();
        notificationList.addAll(notificationResponseList);
        NotificationAdapter adapter =
                new NotificationAdapter(getActivity(),
                        notificationResponseList, notificationViewModel, utility);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.notificationRv.setLayoutManager(mLayoutManager);
        binding.notificationRv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        notificationLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        notificationLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationLayout.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable
                                              Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        TextView toolbarHeader = toolbar.findViewById(R.id.title);
        notificationLayout = toolbar.findViewById(R.id.notificationLayout);
        notificationBadge = toolbar.findViewById(R.id.notificationBadge);
        toolbarHeader.setText(language.equals(KeyWord.BANGLA) ? KeyWord.NOTIFICATION_BN : getActivity().getResources().getString(R.string.menu_notification));
    }
}