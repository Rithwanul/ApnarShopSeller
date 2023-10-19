package com.bikroybaba.seller.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.bikroybaba.seller.BuildConfig;
import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.ActivityHomeBinding;
import com.bikroybaba.seller.viewmodel.HomeViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.moktar.zmvvm.base.base.BaseActivity;
import com.moktar.zmvvm.base.utils.LocaleHelper;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseActivity<HomeViewModel, ActivityHomeBinding>
        implements NavController.OnDestinationChangedListener {

    private AppCompatImageView drawerLogo;
    public DrawerLayout drawer;
    public static NavController navController;
    boolean isOrderExpand = false;
    boolean isProductExpand = false;
    boolean isOfferExpand = false;
    private MaterialTextView name, shopType, mobileNumber;
    private CircleImageView profileImage;
    private NavOptions.Builder navBuilder;
    private NavDestination mDestination;
    private OnLanguageChangeListener languageChangeListener;
    private SwitchCompat mSwitchCompat;
    private MaterialToolbar mToolbar;

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        showContentView();
        setNoTitle();
        initDrawerView();
        initLocale();
        setMenuItemsListener();
        observeViewModel();
    }

    private void initDrawerView() {
        mToolbar = bindingView.include.toolbar;
        setSupportActionBar(mToolbar);
        drawerLogo = bindingView.include.ivSquareDot;
        drawer = bindingView.drawerLayout;
        name = bindingView.nameHomePage;
        shopType = bindingView.shopType;
        mobileNumber = bindingView.mobileNumber;
        profileImage = bindingView.profileImage;
        mSwitchCompat = bindingView.switchCompat;
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(this);
        // set version name on bottom of the sliding menu
        bindingView.homePageVersion.setText(getString(R.string.menu_version, BuildConfig.VERSION_NAME));
        //Add transition drawer to fragment
        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_in_left).
                setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_right)
                .setPopExitAnim(R.anim.slide_out_right);
        // sliding menu hamburger icon  listener
        drawerLogo.setOnClickListener(view -> {
            if (!drawer.isDrawerOpen(GravityCompat.START))
                drawer.open();
            else drawer.close();
            if (mDestination.getId() == R.id.nav_order_details) {
                navController.navigate(R.id.nav_pending_order, null, navBuilder.build());
                drawer.closeDrawer(GravityCompat.START);
            } else if (mDestination.getId() == R.id.nav_complete_order_details) {
                navController.navigate(R.id.nav_complete_order, null, navBuilder.build());
                drawer.closeDrawer(GravityCompat.START);
            } else if (mDestination.getId() == R.id.nav_edit_product) {
                navController.navigate(R.id.nav_all_product, null, navBuilder.build());
                drawer.closeDrawer(GravityCompat.START);
            } else if (mDestination.getId() == R.id.nav_edit_offer) {
                navController.navigate(R.id.nav_offer_list, null, navBuilder.build());
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        // sliding menu back arrow listener
        bindingView.navHeaderMainBackArrow.setOnClickListener(v -> drawer.close());
    }

    private void observeViewModel() {
        // set up user profile on top of the sliding menu
        viewModel.getUserLiveData().observe(this, userProfile -> {
            if (userProfile != null) {
                name.setText(userProfile.getUserName());
                shopType.setText(userProfile.getShopType());
                mobileNumber.setText(userProfile.getPhoneNumber());
                Glide.with(this)
                        .load(userProfile.getUserProfileImage())
                        .placeholder(R.drawable.ic_profile)
                        .into(profileImage);
            }
        });
    }

    private void setMenuItemsListener() {
        // dashboard menu item listener
        bindingView.dahsboardLayout.setOnClickListener(v -> {
            navController.navigate(R.id.nav_dashboard, null, navBuilder.build());
            drawer.closeDrawer(GravityCompat.START);

        });
        // order menu item listener
        bindingView.orderLayout.setOnClickListener(v -> {
            if (!isOrderExpand) {
                if (isProductExpand) {
                    bindingView.productExpandLayout.setVisibility(View.GONE);
                    bindingView.productArrowImage.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
                    isProductExpand = false;
                }
                if (isOfferExpand) {
                    bindingView.offerExpandLayout.setVisibility(View.GONE);
                    bindingView.offerArrowImage.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
                    isOfferExpand = false;
                }
                bindingView.orderExpandLayout.setVisibility(View.VISIBLE);
                bindingView.orderArrowImage.setImageResource(R.drawable.ic_baseline_expand_more_24);
                isOrderExpand = true;
            } else {
                bindingView.orderExpandLayout.setVisibility(View.GONE);
                bindingView.orderArrowImage.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
                isOrderExpand = false;
            }
        });
        // pending order sub menu item listener
        bindingView.homePagePendingOrder.setOnClickListener(v -> {
            navController.navigate(R.id.nav_pending_order, null, navBuilder.build());
            drawer.closeDrawer(GravityCompat.START);
        });
        // all order sub menu item listener
        bindingView.homePageAllOrder.setOnClickListener(v -> {
            navController.navigate(R.id.nav_complete_order, null, navBuilder.build());
            drawer.closeDrawer(GravityCompat.START);
        });
        // product menu item listener
        bindingView.productLayout.setOnClickListener(v -> {
            if (!isProductExpand) {
                if (isOrderExpand) {
                    bindingView.orderExpandLayout.setVisibility(View.GONE);
                    bindingView.orderArrowImage.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
                    isOrderExpand = false;
                }
                if (isOfferExpand) {
                    bindingView.offerExpandLayout.setVisibility(View.GONE);
                    bindingView.offerArrowImage.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
                    isOfferExpand = false;
                }
                bindingView.productExpandLayout.setVisibility(View.VISIBLE);
                bindingView.productArrowImage.setImageResource(R.drawable.ic_baseline_expand_more_24);
                isProductExpand = true;
            } else {
                bindingView.productExpandLayout.setVisibility(View.GONE);
                bindingView.productArrowImage.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
                isProductExpand = false;
            }
        });
        // add product sub menu item listener
        bindingView.homePageAddProduct.setOnClickListener(v -> {
            navController.navigate(R.id.nav_add_product, null, navBuilder.build());
            drawer.closeDrawer(GravityCompat.START);
        });
        // add product sub menu item listener
        bindingView.homePageAllProduct.setOnClickListener(v -> {
            navController.navigate(R.id.nav_all_product, null, navBuilder.build());
            drawer.closeDrawer(GravityCompat.START);
        });
        // offer menu item listener
        bindingView.offerLayout.setOnClickListener(v -> {
            if (!isOfferExpand) {
                if (isOrderExpand) {
                    bindingView.orderExpandLayout.setVisibility(View.GONE);
                    bindingView.orderArrowImage.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
                    isOrderExpand = false;
                }
                if (isProductExpand) {
                    bindingView.productExpandLayout.setVisibility(View.GONE);
                    bindingView.productArrowImage.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
                    isProductExpand = false;
                }
                bindingView.offerExpandLayout.setVisibility(View.VISIBLE);
                bindingView.offerArrowImage.setImageResource(R.drawable.ic_baseline_expand_more_24);
                isOfferExpand = true;
            } else {
                bindingView.offerExpandLayout.setVisibility(View.GONE);
                bindingView.offerArrowImage.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
                isOfferExpand = false;
            }
        });
        // create offer sub menu item listener
        bindingView.homePageCreateOffer.setOnClickListener(v -> {
            navController.navigate(R.id.nav_create_offer, null, navBuilder.build());
            drawer.closeDrawer(GravityCompat.START);
        });
        // offer list sub menu item listener
        bindingView.homePageOfferList.setOnClickListener(v -> {
            navController.navigate(R.id.nav_offer_list, null, navBuilder.build());
            drawer.closeDrawer(GravityCompat.START);
        });
        // delivery area selection listener
        bindingView.deliveryAreaLayout.setOnClickListener(v -> {
            navController.navigate(R.id.delivery_area, null, navBuilder.build());
            drawer.closeDrawer(GravityCompat.START);
        });
        // report menu item listener
        bindingView.reportLayout.setOnClickListener(v -> {
            navController.navigate(R.id.nav_report, null, navBuilder.build());
            drawer.closeDrawer(GravityCompat.START);

        });
        // profile menu item listener
        bindingView.profileLayout.setOnClickListener(v -> {
            navController.navigate(R.id.nav_profile, null, navBuilder.build());
            drawer.closeDrawer(GravityCompat.START);
        });
        // rating menu item listener
        bindingView.ratingLayout.setOnClickListener(v -> {
            navController.navigate(R.id.nav_rating, null, navBuilder.build());
            drawer.closeDrawer(GravityCompat.START);
        });
        // notification menu item listener
        bindingView.notificationLayout.setOnClickListener(v -> {
            navController.navigate(R.id.nav_notification, null, navBuilder.build());
            drawer.closeDrawer(GravityCompat.START);
        });
        // setting menu item listener
        bindingView.settingLayout.setOnClickListener(v -> {
            navController.navigate(R.id.nav_settings, null, navBuilder.build());
            drawer.closeDrawer(GravityCompat.START);
        });
        // support menu item listener
        bindingView.homePageSupport.setOnClickListener(v -> {
            navController.navigate(R.id.nav_support, null, navBuilder.build());
            drawer.closeDrawer(GravityCompat.START);
        });
        // logout menu item listener
        bindingView.logoutLayout.setOnClickListener(v -> {
            viewModel.deleteUser();
            startActivity(new Intent(HomeActivity.this, AuthenticationActivity.class));
            drawer.closeDrawer(GravityCompat.START);
            finish();
        });
        // language switch listener
        mSwitchCompat.setOnClickListener(view -> {
            Context context;
            if (mSwitchCompat.isChecked()) {
                context = LocaleHelper.setLocale(HomeActivity.this, "en");
            } else {
                context = LocaleHelper.setLocale(HomeActivity.this, "bn");
            }
            if (mDestination.getId() == R.id.nav_dashboard) {
                languageChangeListener.onLanguageChange(context);
            }
            refreshLayout(context);
        });
    }

    private void refreshLayout(Context context) {
        bindingView.homePageDashboard.setText(context.getResources().getString(R.string.menu_dashboard));
        bindingView.homePageOrder.setText(context.getResources().getString(R.string.menu_order));
        bindingView.homePagePendingOrder.setText(context.getResources().getString(R.string.menu_pending_order));
        bindingView.homePageAllOrder.setText(context.getResources().getString(R.string.menu_all_order));
        bindingView.homePageProduct.setText(context.getResources().getString(R.string.menu_product));
        bindingView.homePageAddProduct.setText(context.getResources().getString(R.string.menu_add_product));
        bindingView.homePageAllProduct.setText(context.getResources().getString(R.string.menu_all_product));
        bindingView.homePageOffer.setText(context.getResources().getString(R.string.menu_offer));
        bindingView.homePageCreateOffer.setText(context.getResources().getString(R.string.menu_create_offer));
        bindingView.homePageOfferList.setText(context.getResources().getString(R.string.menu_offer_list));
        bindingView.homePageDeliveryArea
                .setText(context.getResources().getString(R.string.menu_delivery_area_selection));
        bindingView.homePageReport.setText(context.getResources().getString(R.string.menu_report));
        bindingView.homePageProfile.setText(context.getResources().getString(R.string.menu_profile));
        bindingView.homePageRating.setText(context.getResources().getString(R.string.menu_rating));
        bindingView.homePageNotification.setText(context.getResources().getString(R.string.menu_notification));
        bindingView.homePageSettings.setText(context.getResources().getString(R.string.menu_settings));
        bindingView.homePageLogout.setText(context.getResources().getString(R.string.menu_logout));
        bindingView.homePageLanguage.setText(context.getResources().getString(R.string.menu_language));
        bindingView.homePageSupport.setText(context.getResources().getString(R.string.menu_support));
        bindingView.homePageVersion.setText(getString(R.string.menu_version, BuildConfig.VERSION_NAME));
    }

    private void initLocale() {
        String lang = LocaleHelper.getLanguage(this);
        if (lang.equalsIgnoreCase("en")) {
            mSwitchCompat.setChecked(true);
        } else if (lang.equalsIgnoreCase("bn")) {
            mSwitchCompat.setChecked(false);
        }
    }

    public void setOnLanguageChangeListener(OnLanguageChangeListener languageChangeListener) {
        this.languageChangeListener = languageChangeListener;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        drawer.closeDrawers();
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller,
                                     @NonNull NavDestination destination,
                                     @Nullable Bundle arguments) {
        mDestination = destination;
        if (mDestination.getId() == R.id.nav_order_details
                || mDestination.getId() == R.id.nav_complete_order_details
                || mDestination.getId() == R.id.nav_edit_product
                || mDestination.getId() == R.id.nav_edit_offer) {
            setToolbarLogo(R.drawable.ic_back);
        } else {
            setToolbarLogo(R.drawable.ic_drawer);
        }
    }

    private void setToolbarLogo(int resId) {
        drawerLogo.setImageResource(resId);
    }

    public interface OnLanguageChangeListener {
        void onLanguageChange(Context context);
    }
}