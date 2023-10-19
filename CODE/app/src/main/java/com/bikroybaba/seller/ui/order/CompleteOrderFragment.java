package com.bikroybaba.seller.ui.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.AllOrderViewPagerAdapter;
import com.bikroybaba.seller.databinding.FragmentCompleteOrderBinding;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.google.android.material.textview.MaterialTextView;

import io.reactivex.rxjava3.disposables.Disposable;

public class CompleteOrderFragment extends Fragment {

    private Toolbar toolbar;
    private MaterialTextView textToolHeader;
    private FragmentCompleteOrderBinding binding;
    private Utility utility;
    private AllOrderViewPagerAdapter adapter;
    private String language;
    private Disposable disposable;

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_complete_order, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clearSearch();
        utility = new Utility(getActivity());
        language = utility.getLangauge();
        initFragment();
        binding.completeOrderSearch.setIconifiedByDefault(false);
        binding.completeOrderSearch.setQueryHint(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.search_by_order_no_bn) : getString(R.string.search_by_order_no));
        binding.completeOrderSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                requireActivity().sendBroadcast(new Intent("com.aapnarshop.seller.completeOrder")
                        .putExtra("search", newText));
                saveSearchKey(newText);
                return true;
            }
        });
    }

    private void initFragment() {
        adapter = new AllOrderViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new AllFragment(), getString(R.string.all));
        adapter.addFragment(new LastWeekFragment(), language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.last_week_bn) : getString(R.string.last_week));
        adapter.addFragment(new LastMonthFragment(), language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.last_month_bn) : getString(R.string.last_month));
        adapter.addFragment(new LastYearFragment(), language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.last_year_bn) : getString(R.string.last_year));
        adapter.addFragment(new DateFragment(), language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.date_bn) : getString(R.string.date));
        binding.viewPager.setAdapter(adapter);
        binding.tabs.setupWithViewPager(binding.viewPager);
    }

    private void saveSearchKey(String key) {
        SharedPreferences preferences =
                requireActivity().getSharedPreferences("search", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("key", key);
        editor.apply();
    }

    private void clearSearch() {
        SharedPreferences preferences =
                requireActivity().getSharedPreferences("search", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable
                                              Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        textToolHeader = toolbar.findViewById(R.id.title);
//        textToolHeader.setText(language.equalsIgnoreCase(KeyWord.BANGLA) ? R.string.previous_order_bn : R.string.previous_order);
    }
}
