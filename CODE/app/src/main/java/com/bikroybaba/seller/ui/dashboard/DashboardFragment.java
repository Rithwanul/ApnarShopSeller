package com.bikroybaba.seller.ui.dashboard;

import static com.bikroybaba.seller.ui.HomeActivity.navController;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.FragmentDashboardBinding;
import com.bikroybaba.seller.model.remote.request.AttributeRequest;
import com.bikroybaba.seller.ui.HomeActivity;
import com.bikroybaba.seller.viewmodel.DashboardViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.moktar.zmvvm.base.base.BaseFragment;

public class DashboardFragment extends BaseFragment<DashboardViewModel, FragmentDashboardBinding>
        implements HomeActivity.OnLanguageChangeListener {
    @Override
    public int setContent() {
        return R.layout.fragment_dashboard;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((HomeActivity) requireActivity()).setOnLanguageChangeListener(this);
        showContentView();
        initView();
        loadData();
    }

    private void loadDataAttribute() {
        AttributeRequest attributeRequest = new AttributeRequest();
        attributeRequest.setAttributeName("SIZE");
        /*viewModel.getAttributeLivedata("SIZE").observe(requireActivity(), attributeResponse -> {
            Log.d("Data", "loadDataAttribute: ->> " + attributeResponse.getSuccess());
        });*/

        viewModel.getAttributeDataNameLiveData("BLUE", 2).observe(requireActivity(), attributeResponse -> {
            Log.d("Data", "loadDataAttribute: ->> " + attributeResponse.getAttributes().get(0).getAttributeName());
        });

    }



    @Override
    protected void loadData() {
        observeResponse();
    }

    private void observeResponse() {
        viewModel.getProductCount().observe(requireActivity(), productCount -> {
            if (productCount != null) {
                bindingView.dashboardNewOrderCount.setText(productCount.getNewOrder());
                bindingView.dashboardTotalActiveProductCount.setText(productCount.getActiveProduct());
                bindingView.dashboardLimitedProductCount.setText(productCount.getLimitedProduct());
            }

        });
        viewModel.getUserLivedata().observe(getViewLifecycleOwner(), userProfile -> {
            if (userProfile != null) {
                MaterialToolbar toolbar = requireActivity().findViewById(R.id.toolbar);
                MaterialTextView textToolHeader = toolbar.findViewById(R.id.title);
                textToolHeader.setText(userProfile.getShopName());
            }
        });
    }

    private void initView() {
        // dashboard items listener
        bindingView.dashboardNewOrder
                .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_dashboard_to_nav_pending_order));
        bindingView.llNewOrder
                .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_dashboard_to_nav_pending_order));
        bindingView.dashboardAddNewProduct
                .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_dashboard_to_nav_add_product));
        bindingView.dashboardYourProduct
                .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_dashboard_to_nav_all_product));
        bindingView.llActiveProduct
                .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_dashboard_to_nav_all_product));
        bindingView.dashboardPreviousOrder.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_dashboard_to_nav_complete_order));
        // bottom tab item listener
        bindingView.dashboardSettingsIcon
                .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_dashboard_to_nav_settings));
        bindingView.dashboardReport
                .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_dashboard_to_nav_report));
        bindingView.dashboardOffer
                .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_dashboard_to_nav_create_offer));
        bindingView.dashboardDeliveryArea
                .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_dashboard_to_delivery_area));
        bindingView.dashboardOfferListIcon
                .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_dashboard_to_nav_offer_list));

        bindingView.dashboardStockProduct.setOnClickListener(v -> {
            DashboardFragmentDirections.ActionNavDashboardToNavAllProduct action =
                    DashboardFragmentDirections.actionNavDashboardToNavAllProduct();
            action.setFilter("limited");
            navController.navigate(action);
        });
        bindingView.llLimitProduct.setOnClickListener(v -> {
            DashboardFragmentDirections.ActionNavDashboardToNavAllProduct action =
                    DashboardFragmentDirections.actionNavDashboardToNavAllProduct();
            action.setFilter("limited");
            navController.navigate(action);
        });
        bindingView.dashboardSwipeRefresh.setOnRefreshListener(() -> {
            observeResponse();
            bindingView.dashboardSwipeRefresh.setRefreshing(false);

        });
    }

    @Override
    public void onLanguageChange(Context context) {
        refreshLayout(context);
    }

    private void refreshLayout(Context context) {
        bindingView.dashboardNewOrderTv
                .setText(context.getResources().getString(R.string.new_order));
        bindingView.dashboardNewOrder2
                .setText(context.getResources().getString(R.string.new_order));
        bindingView.dashboardPerviousOrderTv
                .setText(context.getResources().getString(R.string.previous_order));
        bindingView.dashboardYourProductTv
                .setText(context.getResources().getString(R.string.your_product));
        bindingView.dashboardAddNewProductTv
                .setText(context.getResources().getString(R.string.add_new_product));
        bindingView.dashboardTotalActiveProduct
                .setText(context.getResources().getString(R.string.total_active_product));
        bindingView.dashboardLimitedProductTv
                .setText(context.getResources().getString(R.string.limited_product));
        bindingView.dashboardStockLimitedTv
                .setText(context.getResources().getString(R.string.stock_limited_product));
        // bottom tab menu
        bindingView.dashboardSettings
                .setText(context.getResources().getString(R.string.menu_settings));
        bindingView.dashboardOfferTv
                .setText(context.getResources().getString(R.string.menu_offer));
        bindingView.dashboardReportTv
                .setText(context.getResources().getString(R.string.menu_report));
        bindingView.dashboardSignoutTv
                .setText(context.getResources().getString(R.string.menu_delivery_area_selection));
        bindingView.dashboardOfferList
                .setText(context.getResources().getString(R.string.menu_offer_list));
    }
}