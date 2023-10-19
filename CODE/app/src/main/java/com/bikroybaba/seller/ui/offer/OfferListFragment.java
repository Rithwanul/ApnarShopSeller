package com.bikroybaba.seller.ui.offer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.OfferListViewPagerAdapter;
import com.bikroybaba.seller.databinding.FragmentOfferListBinding;
import com.moktar.zmvvm.base.base.BaseFragment;
import com.moktar.zmvvm.base.base.NoViewModel;


public class OfferListFragment extends BaseFragment<NoViewModel, FragmentOfferListBinding> {


    @Override
    public int setContent() {
        return R.layout.fragment_offer_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showContentView();
        initView();
        initTabs();
        bindingView.completeOrderSearch.setIconifiedByDefault(false);
        bindingView.completeOrderSearch.setQueryHint(getString(R.string.search));
        bindingView.completeOrderSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                requireActivity().sendBroadcast(new Intent("com.aapnarshop.seller.offerlist").putExtra("search", newText));
                return false;
            }
        });

    }

    private void initView() {
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        TextView textToolHeader = toolbar.findViewById(R.id.title);
        textToolHeader.setText(requireActivity().getString(R.string.offerList));
    }

    private void initTabs() {
        OfferListViewPagerAdapter adapter = new OfferListViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new RunningOfferFragment(), getString(R.string.running));
        adapter.addFragment(new ExpiredOfferFragment(), getString(R.string.expired));
        adapter.addFragment(new AllOfferFragment(), getString(R.string.all));
        bindingView.viewPager.setAdapter(adapter);
        bindingView.tabs.setupWithViewPager(bindingView.viewPager);
    }
}