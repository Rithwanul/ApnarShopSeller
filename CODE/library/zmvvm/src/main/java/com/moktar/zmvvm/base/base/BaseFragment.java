package com.moktar.zmvvm.base.base;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textview.MaterialTextView;
import com.moktar.zmvvm.R;
import com.moktar.zmvvm.base.utils.ClassUtil;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment<VM extends AndroidViewModel, VDB extends ViewDataBinding>
        extends Fragment {

    // ViewModel
    protected VM viewModel;
    // Layout view
    protected VDB bindingView;
    // whether the fragment is displayed
    protected boolean mIsVisible = false;
    // Loading
    private View loadingView;
    // Failed to load
    private View errorView;
    // Empty layout
    private View emptyView;
    // Animation
    private AnimationDrawable mAnimationDrawable;
    private CompositeDisposable mCompositeDisposable;

    protected AppCompatActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_base, null);
        bindingView = DataBindingUtil
                .inflate(activity.getLayoutInflater(),
                        setContent(), null, false);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        RelativeLayout mContainer = inflate.findViewById(R.id.container);
        mContainer.addView(bindingView.getRoot());
        bindingView.getRoot().setVisibility(View.GONE);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable
                                      Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
    }

    /**
     * Realize the slow loading of Fragment data here.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    protected void onInvisible() {
    }

    /**
     * Load data when displaying, it needs to be used like this
     * Note that the declaration isPrepared, first initialize
     * The life cycle will first execute setUserVisibleHint and then execute onActivityCreated
     * The loading data is displayed for the first time after onActivityCreated, and only loaded once
     */
    protected void loadData() {
    }

    protected void onVisible() {
        loadData();
    }

   /* @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewModel();
    }*/

    /**
     * Initialize ViewModel
     */
    private void initViewModel() {
        Class<VM> viewModelClass = ClassUtil.getViewModel(this);
        if (viewModelClass != null) {
            this.viewModel = new ViewModelProvider(this).get(viewModelClass);
        }
    }

    protected <T extends View> T getView(int id) {
        return getView().findViewById(id);
    }

    /**
     * layout
     */
    public abstract int setContent();

    /**
     * Actions after clicking after failed to load
     */
    protected void onRefresh() {

    }

    /**
     * Display loading status
     */
    protected void showLoading() {
        ViewStub viewStub = getView(R.id.vs_loading);
        if (viewStub != null) {
            loadingView = viewStub.inflate();
            AppCompatImageView img = loadingView.findViewById(R.id.img_progress);
            mAnimationDrawable = (AnimationDrawable) img.getDrawable();
        }
        if (loadingView != null && loadingView.getVisibility() != View.VISIBLE) {
            loadingView.setVisibility(View.VISIBLE);
        }
        // Start animation
        if (mAnimationDrawable != null && !mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        if (bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        if (emptyView != null && emptyView.getVisibility() != View.GONE) {
            emptyView.setVisibility(View.GONE);
        }
    }

    /**
     * Loading completed status
     */
    protected void showContentView() {
        if (bindingView.getRoot().getVisibility() != View.VISIBLE) {
            bindingView.getRoot().setVisibility(View.VISIBLE);
        }
        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // Stop animation
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        if (emptyView != null && emptyView.getVisibility() != View.GONE) {
            emptyView.setVisibility(View.GONE);
        }
    }

    /**
     * Load failed and click to reload the state
     */
    protected void showError() {
        ViewStub viewStub = getView(R.id.vs_error_refresh);
        if (viewStub != null) {
            errorView = viewStub.inflate();
            // Click to load failed layout
            errorView.setOnClickListener(v -> {
                showLoading();
                onRefresh();
            });
        }
        if (errorView != null) {
            errorView.setVisibility(View.VISIBLE);
        }
        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // Stop animation
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
        if (emptyView != null && emptyView.getVisibility() != View.GONE) {
            emptyView.setVisibility(View.GONE);
        }
    }

    protected void showEmptyView(String text) {
        // Need to do this, otherwise the second display will fail
        ViewStub viewStub = getView(R.id.vs_empty);
        if (viewStub != null) {
            emptyView = viewStub.inflate();
            ((MaterialTextView) emptyView.findViewById(R.id.tv_tip_empty)).setText(text);
        }
        if (emptyView != null) {
            emptyView.setVisibility(View.VISIBLE);
        }

        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // Stop animation
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        if (bindingView != null && bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
    }
    protected void hideKeyboard(View view) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
   /* protected NavOptions getNavOptions() {

        return new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .build();
    }*/
    public void addSubscription(Disposable disposable) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = new CompositeDisposable();
        }
        this.mCompositeDisposable.add(disposable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bindingView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            this.mCompositeDisposable.clear();
        }
    }

    public void removeDisposable() {
        if (this.mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            this.mCompositeDisposable.dispose();
        }
    }

}
