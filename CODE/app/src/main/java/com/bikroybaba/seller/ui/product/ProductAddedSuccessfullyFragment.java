package com.bikroybaba.seller.ui.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.FragmentProductAddedSuccessfullyBinding;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;

public class ProductAddedSuccessfullyFragment extends Fragment {

    private Toolbar toolbar;
    private Utility utility;
    private FragmentProductAddedSuccessfullyBinding binding;

    public ProductAddedSuccessfullyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =
                DataBindingUtil.inflate(inflater,
                        R.layout.fragment_product_added_successfully, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        utility = new Utility(getActivity());
        binding.productAddSuccess.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.product_added_successfully : R.string.product_successfully_added_bn);
//        binding.productAddedSuccessfullyDashboard.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.dashboard : R.string.dashboard_bn);
        binding.productAddedSuccessfullyAddMore.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.add_more : R.string.add_more_bn);

        binding.productAddedSuccessfullyBackBtn
                .setOnClickListener(Navigation
                        .createNavigateOnClickListener(R.id.action_nav_product_added_successfully_to_nav_add_product));
        binding.productAddedSuccessfullyDashboard
                .setOnClickListener(Navigation
                        .createNavigateOnClickListener(R.id.action_nav_product_added_successfully_to_nav_dashboard));
        binding.productAddedSuccessfullyAddMore
                .setOnClickListener(Navigation
                        .createNavigateOnClickListener(R.id.action_nav_product_added_successfully_to_nav_add_product));
    }

    private void initView() {
        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        toolbar.setVisibility(View.VISIBLE);
    }
}