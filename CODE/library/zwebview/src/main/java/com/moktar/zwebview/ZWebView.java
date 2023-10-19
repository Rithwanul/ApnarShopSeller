package com.moktar.zwebview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * The webpage can handle:
 * Click the corresponding control:
 *-Progress bar display
 *-Upload picture (version compatible)
 *-Play web video in full screen
 *-Call WeChat Alipay
 *-Make calls, send text messages, send emails
 *-Return to the upper level of the page and display the title of the page
 * JS interactive part:
 *-Front-end code is embedded in js (lack of flexibility)
 *-The webpage comes with js jump
 *
 * @author moktar
 * link to https://github.com/youlookwhat/ByWebView
 */
public class ZWebView {

    private final int mErrorLayoutId;
    private final String mErrorTitle;
    private final Activity activity;
    private final ZWebChromeClient mWebChromeClient;
    private WebView mWebView;
    private WebProgress mProgressBar;
    private View mErrorView;
    private ZLoadJsHolder zLoadJsHolder;

    private ZWebView(Builder builder) {
        this.activity = builder.mActivity;
        this.mErrorTitle = builder.mErrorTitle;
        this.mErrorLayoutId = builder.mErrorLayoutId;

        FrameLayout parentLayout = new FrameLayout(activity);
        // Set up WebView
        setWebView(builder.mCustomWebView);
        parentLayout.addView(mWebView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // Progress bar layout
        handleWebProgress(builder, parentLayout);
        if (builder.mIndex != -1) {
            builder.mWebContainer.addView(parentLayout, builder.mIndex, builder.mLayoutParams);
        } else {
            builder.mWebContainer.addView(parentLayout, builder.mLayoutParams);
        }
        // Configuration
        handleSetting();
        // Video, photos, progress bar
        mWebChromeClient = new ZWebChromeClient(activity, this);
        mWebChromeClient.setOnByWebChromeCallback(builder.mOnTitleProgressCallback);
        mWebView.setWebChromeClient(mWebChromeClient);

        // Error page, end of page, processing DeepLink
        ZWebViewClient mZWebViewClient = new ZWebViewClient(activity, this);
        mZWebViewClient.setOnByWebClientCallback(builder.mOnZWebClientCallback);
        mWebView.setWebViewClient(mZWebViewClient);

        handleJsInterface(builder);
    }

    public static Builder with(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity can not be null .");
        }
        return new Builder(activity);
    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    private void handleJsInterface(Builder builder) {
        if (!TextUtils.isEmpty(builder.mInterfaceName) && builder.mInterfaceObj != null) {
            mWebView.addJavascriptInterface(builder.mInterfaceObj, builder.mInterfaceName);
        }
    }

    public ZLoadJsHolder getLoadJsHolder() {
        if (zLoadJsHolder == null) {
            zLoadJsHolder = new ZLoadJsHolder(mWebView);
        }
        return zLoadJsHolder;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void handleSetting() {
        WebSettings ws = mWebView.getSettings();
        // Save form data
        ws.setSaveFormData(true);
        // Should it support the use of its screen zoom controls and gesture zoom
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // Start application cache
        ws.setAppCacheEnabled(true);
        // Set cache mode
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19 is deprecated
        // The width of the web content is adaptive to the screen
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        // Tell WebView to enable JavaScript execution。The default is false。
        ws.setJavaScriptEnabled(true);
        //  After the page is loaded, let go of the picture
        ws.setBlockNetworkImage(false);
        // To use localStorage, it must be turned on
        ws.setDomStorageEnabled(true);
        // Typography adapts to the screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        } else {
            ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        // Whether the WebView opens in a new window (you may not be able to open the web page after adding it)
//        ws.setSupportMultipleWindows(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // WebView since 5.0 does not allow mixed mode by default, HTTP resources cannot be loaded in https, and need to be set to enable。
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    /**
     * Set the default zoom size of the font (change the font size of the webpage, setTextSize api14 is deprecated)
     *
     * @param textZoom Default 100
     */
    public void setTextZoom(int textZoom) {
        mWebView.getSettings().setTextZoom(textZoom);
    }

    private void handleWebProgress(Builder builder, FrameLayout parentLayout) {
        if (builder.mUseWebProgress) {
            mProgressBar = new WebProgress(activity);
            if (builder.mProgressStartColor != 0 && builder.mProgressEndColor != 0) {
                mProgressBar.setColor(builder.mProgressStartColor, builder.mProgressEndColor);
            } else if (builder.mProgressStartColor != 0) {
                mProgressBar.setColor(builder.mProgressStartColor, builder.mProgressStartColor);
            } else if (!TextUtils.isEmpty(builder.mProgressStartColorString)
                    && !TextUtils.isEmpty(builder.mProgressEndColorString)) {
                mProgressBar.setColor(builder.mProgressStartColorString, builder.mProgressEndColorString);
            } else if (!TextUtils.isEmpty(builder.mProgressStartColorString)
                    && TextUtils.isEmpty(builder.mProgressEndColorString)) {
                mProgressBar.setColor(builder.mProgressStartColorString, builder.mProgressStartColorString);
            }
            int progressHeight = ZWebTools.dip2px(parentLayout.getContext(), WebProgress.WEB_PROGRESS_DEFAULT_HEIGHT);
            if (builder.mProgressHeightDp != 0) {
                mProgressBar.setHeight(builder.mProgressHeightDp);
                progressHeight = ZWebTools.dip2px(parentLayout.getContext(), builder.mProgressHeightDp);
            }
            mProgressBar.setVisibility(View.GONE);
            parentLayout.addView(mProgressBar, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, progressHeight));
        }
    }

    public void loadUrl(String url) {
        if (!TextUtils.isEmpty(url) && url.endsWith("mp4") && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mWebView.loadData(ZWebTools.getVideoHtmlBody(url), "text/html", "UTF-8");
        } else {
            mWebView.loadUrl(url);
        }
        if (mProgressBar != null) {
            mProgressBar.show();
        }
        hideErrorView();
    }

    public void reload() {
        hideErrorView();
        mWebView.reload();
    }

    public void onResume() {
        mWebView.onResume();
        // After opening the article details in the Alipay web version, I cannot click the button Next
        mWebView.resumeTimers();
    }

    public void onPause() {
        mWebView.onPause();
        mWebView.resumeTimers();
    }

    public void onDestroy() {
        if (mWebChromeClient != null && mWebChromeClient.getVideoFullView() != null) {
            mWebChromeClient.getVideoFullView().removeAllViews();
        }
        if (mWebView != null) {
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.removeAllViews();
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
    }

    /**
     * Callback after selecting the picture, call onActivityResult in Activity
     */
    public void handleFileChooser(int requestCode, int resultCode, Intent intent) {
        if (mWebChromeClient != null) {
            mWebChromeClient.handleFileChooser(requestCode, resultCode, intent);
        }
    }

    public boolean handleKeyEvent(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return isBack();
        }
        return false;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public boolean isBack() {
        // Full screen playback Exit full screen
        if (mWebChromeClient.inCustomView()) {
            mWebChromeClient.onHideCustomView();
            return true;

            // Return to the previous page of the webpage
        } else if (mWebView.canGoBack()) {
            hideErrorView();
            mWebView.goBack();
            return true;
        }
        return false;
    }

    public WebView getWebView() {
        return mWebView;
    }

    /**
     * Configure a custom WebView
     */
    private void setWebView(WebView mCustomWebView) {
        if (mCustomWebView != null) {
            mWebView = mCustomWebView;
        } else {
            mWebView = new WebView(activity);
        }
    }

    public WebProgress getProgressBar() {
        return mProgressBar;
    }

    /**
     * Show wrong layout
     */
    public void showErrorView() {
        try {
            if (mErrorView == null) {
                FrameLayout parent = (FrameLayout) mWebView.getParent();
                mErrorView = LayoutInflater.from(parent.getContext()).inflate((mErrorLayoutId == 0) ? R.layout.z_load_url_error : mErrorLayoutId, null);
                mErrorView.setOnClickListener(v -> reload());
                parent.addView(mErrorView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                mErrorView.setVisibility(View.VISIBLE);
            }
            mWebView.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide wrong layout
     */
    public void hideErrorView() {
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
    }

    public View getErrorView() {
        return mErrorView;
    }

    String getErrorTitle() {
        return mErrorTitle;
    }

    public static class Builder {
        private final Activity mActivity;
        private Fragment mFragment;
        // Use progress bar by default
        private boolean mUseWebProgress = true;
        // Progress bar start color
        private int mProgressStartColor;
        private String mProgressStartColorString;
        // Progress bar end color
        private int mProgressEndColor;
        private String mProgressEndColorString;
        // Progress bar height
        private int mProgressHeightDp;
        private int mErrorLayoutId;
        private int mIndex = -1;
        private String mErrorTitle;
        private WebView mCustomWebView;
        private String mInterfaceName;
        private Object mInterfaceObj;
        private ViewGroup mWebContainer;
        private ViewGroup.LayoutParams mLayoutParams;
        private OnTitleProgressCallback mOnTitleProgressCallback;
        private OnZWebClientCallback mOnZWebClientCallback;

        public Builder(Activity activity) {
            this.mActivity = activity;
        }

        public Builder(Activity activity, Fragment fragment) {
            this.mActivity = activity;
            this.mFragment = fragment;
        }

        /**
         * WebView container
         */
        public Builder setWebParent(@NonNull ViewGroup webContainer, ViewGroup.LayoutParams layoutParams) {
            this.mWebContainer = webContainer;
            this.mLayoutParams = layoutParams;
            return this;
        }

        /**
         * WebView container
         *
         * @param webContainer external WebView container
         * @param index added position
         * @param layoutParams corresponding LayoutParams
         */
        public Builder setWebParent(@NonNull ViewGroup webContainer, int index, ViewGroup.LayoutParams layoutParams) {
            this.mWebContainer = webContainer;
            this.mIndex = index;
            this.mLayoutParams = layoutParams;
            return this;
        }

        /**
         * @param isUse Whether to use the progress bar, the default is true
         */
        public Builder useWebProgress(boolean isUse) {
            this.mUseWebProgress = isUse;
            return this;
        }

        /**
         * Set progress bar color
         *
         * @param color Example：ContextCompat.getColor(this, R.color.red)
         */
        public Builder useWebProgress(int color) {
            return useWebProgress(color, color, 3);
        }

        /**
         * Set progress bar color
         *
         * @param color Example："#FF0000"
         */
        public Builder useWebProgress(String color) {
            return useWebProgress(color, color, 3);
        }

        /**
         * Set the gradient color of the progress bar
         *
         * @param startColor start color
         * @param endColor end color
         * @param heightDp height of progress bar, unit dp
         */
        public Builder useWebProgress(int startColor, int endColor, int heightDp) {
            mProgressStartColor = startColor;
            mProgressEndColor = endColor;
            mProgressHeightDp = heightDp;
            return this;
        }

        public Builder useWebProgress(String startColor, String endColor, int heightDp) {
            mProgressStartColorString = startColor;
            mProgressEndColorString = endColor;
            mProgressHeightDp = heightDp;
            return this;
        }

        /**
         * @param customWebView Custom WebView
         */
        public Builder setCustomWebView(WebView customWebView) {
            mCustomWebView = customWebView;
            return this;
        }

        /**
         * @param errorLayoutId Error page layout, the title defaults to "Failed to open the page"
         */
        public Builder setErrorLayout(@LayoutRes int errorLayoutId) {
            mErrorLayoutId = errorLayoutId;
            return this;
        }

        /**
         * @param errorLayoutId error page layout
         * @param errorTitle error page title
         */
        public Builder setErrorLayout(@LayoutRes int errorLayoutId, String errorTitle) {
            mErrorLayoutId = errorLayoutId;
            mErrorTitle = errorTitle;
            return this;
        }

        /**
         * Add Js listener
         */
        public Builder addJavascriptInterface(String interfaceName, Object interfaceObj) {
            this.mInterfaceName = interfaceName;
            this.mInterfaceObj = interfaceObj;
            return this;
        }

        /**
         * @param onTitleProgressCallback Return Title and Progress
         */
        public Builder setOnTitleProgressCallback(OnTitleProgressCallback onTitleProgressCallback) {
            this.mOnTitleProgressCallback = onTitleProgressCallback;
            return this;
        }

        /**
         * Page load end monitoring and processing three-way jump links
         */
        public Builder setOnZWebClientCallback(OnZWebClientCallback onZWebClientCallback) {
            this.mOnZWebClientCallback = onZWebClientCallback;
            return this;
        }

        public ZWebView loadUrl(String url) {
            ZWebView zWebView = new ZWebView(this);
            zWebView.loadUrl(url);
            return zWebView;
        }
    }

}
