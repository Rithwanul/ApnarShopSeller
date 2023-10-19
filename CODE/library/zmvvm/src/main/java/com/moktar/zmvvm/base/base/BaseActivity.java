package com.moktar.zmvvm.base.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.moktar.zmvvm.R;
import com.moktar.zmvvm.base.utils.ClassUtil;
import com.moktar.zmvvm.base.utils.CommonUtils;
import com.moktar.zmvvm.base.utils.LocaleHelper;
import com.moktar.zmvvm.base.utils.StatusBarUtil;
import com.moktar.zmvvm.databinding.ActivityBaseBinding;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity<VM extends AndroidViewModel, VDB extends ViewDataBinding>
        extends AppCompatActivity {

    // ViewModel
    protected VM viewModel;
    // Layout view
    protected VDB bindingView;
    private View errorView;
    private View loadingView;
    private ActivityBaseBinding mBaseBinding;
    private AnimationDrawable mAnimationDrawable;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        mBaseBinding = DataBindingUtil
                .inflate(LayoutInflater.from(this),
                        R.layout.activity_base, null, false);
        bindingView = DataBindingUtil.
                inflate(LayoutInflater.from(this), layoutResID, null, false);

        // content
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        RelativeLayout mContainer =
              mBaseBinding.getRoot().findViewById(R.id.container);
        mContainer.addView(bindingView.getRoot());
        getWindow().setContentView(mBaseBinding.getRoot());

        loadingView = ((ViewStub) findViewById(R.id.vs_loading)).inflate();
        ImageView img = loadingView.findViewById(R.id.img_progress);

        // Loading animation
        mAnimationDrawable = (AnimationDrawable) img.getDrawable();
        // Turn on animation when entering the page by default
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }

        setToolBar(mBaseBinding.toolBar);
        bindingView.getRoot().setVisibility(View.GONE);
        initStatusBar();
        initViewModel();
    }

    protected void initStatusBar() {
        //Set a transparent status bar, compatible with 4.4
        StatusBarUtil.setStatusBarColor(this, CommonUtils.getColor(R.color.colorWhite));
    }

    /**
     * Initialize ViewModel
     */
    private void initViewModel() {
        Class<VM> viewModelClass = ClassUtil.getViewModel(this);
        if (viewModelClass != null) {
            this.viewModel = new ViewModelProvider(this).get(viewModelClass);
        }
    }

    /**
     * Set title bar
     */
    protected void setToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //Remove the default Title display
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }
        toolbar.setNavigationOnClickListener(v -> supportFinishAfterTransition());
    }

    @Override
    public void setTitle(CharSequence text) {
        mBaseBinding.toolBar.setTitle(text);
    }

    public void setNoTitle() {
        mBaseBinding.toolBar.setVisibility(View.GONE);
    }

    protected void showLoading() {
        if (loadingView != null && loadingView.getVisibility() != View.VISIBLE) {
            loadingView.setVisibility(View.VISIBLE);
        }
        // Start animation
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        if (bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
    }

    protected void showContentView() {
        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // Stop animation
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        if (bindingView.getRoot().getVisibility() != View.VISIBLE) {
            bindingView.getRoot().setVisibility(View.VISIBLE);
        }
    }

    protected void showError() {
        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // Stop animation
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (errorView == null) {
            ViewStub viewStub = findViewById(R.id.vs_error_refresh);
            errorView = viewStub.inflate();
            // Click to load failed layout
            errorView.setOnClickListener(v -> {
                showLoading();
                onRefresh();
            });
        } else {
            errorView.setVisibility(View.VISIBLE);
        }
        if (bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
    }
    protected void hideActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    protected void showActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            //Remove the default Title display
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
        toolbar.setNavigationOnClickListener(v -> supportFinishAfterTransition());
    }
    protected Activity getActivity(){
        return this;
    }
    /**
     * Click refresh after failure
     */
    protected void onRefresh() {

    }

    public void addSubscription(Disposable s) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = new CompositeDisposable();
        }
        this.mCompositeDisposable.add(s);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.fontScale != 1) {
            getResources();
        }
    }

    /**
     * Prohibit changing font size
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            // clear 和 dispose的区别是：  disposed = true;
            this.mCompositeDisposable.clear();
        }
    }

    public void removeDisposable() {
        if (this.mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            this.mCompositeDisposable.dispose();
        }
    }
}
