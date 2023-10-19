package com.bikroybaba.seller.ui.offer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.FragmentOfferCreatedSuccessfullyBinding;
import com.moktar.zmvvm.base.base.BaseFragment;
import com.moktar.zmvvm.base.base.NoViewModel;


public class OfferCreatedSuccessfullyFragment extends
        BaseFragment<NoViewModel, FragmentOfferCreatedSuccessfullyBinding> {

    private String fromDate;
    private String toDate;
    private String discount;


    @Override
    public int setContent() {
        return  R.layout.fragment_offer_created_successfully;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showContentView();
        fromDate = OfferCreatedSuccessfullyFragmentArgs.fromBundle(getArguments()).getOfferDate();
        toDate = OfferCreatedSuccessfullyFragmentArgs.fromBundle(getArguments()).getOfferToDate();
        discount = OfferCreatedSuccessfullyFragmentArgs.fromBundle(getArguments()).getOfferDiscount();
        String message = OfferCreatedSuccessfullyFragmentArgs.fromBundle(getArguments()).getMessage();
        bindingView.discount.setText(discount);
        bindingView.offerPeriod.setText(String.format("%s To %s", fromDate, toDate));
        bindingView.message.setText(message);
        bindingView.productAddedSuccessfullyBackBtn
                .setOnClickListener(
                        Navigation.createNavigateOnClickListener(R.id.action_nav_offer_created_successfully_to_nav_create_offer)
                );

    }
}