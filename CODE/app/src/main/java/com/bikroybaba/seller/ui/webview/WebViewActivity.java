package com.bikroybaba.seller.ui.webview;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.ActivityWebViewBinding;
import com.bikroybaba.seller.util.StatusBarUtil;
import com.google.android.material.textview.MaterialTextView;
import com.moktar.zwebview.OnTitleProgressCallback;
import com.moktar.zwebview.OnZWebClientCallback;
import com.moktar.zwebview.ZWebTools;
import com.moktar.zwebview.ZWebView;

public class WebViewActivity extends AppCompatActivity {

    private final OnZWebClientCallback onZWebClientCallback = new OnZWebClientCallback() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.e("---onPageStarted", url);
        }

        @Override
        public boolean isOpenThirdApp(String url) {
            // Handling three-way links
            Log.e("---url", url);
            return ZWebTools.handleThirdApp(WebViewActivity.this, url);
        }

        @Override
        public boolean onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // If you handle it yourself, you need to return true
            return super.onReceivedSslError(view, handler, error);
        }
    };
    private String mUrl;
    private String mTitle;
    private ZWebView zWebView;
    private WebView webView;
    private MaterialTextView mTvTitle;
    private final OnTitleProgressCallback onTitleProgressCallback = new OnTitleProgressCallback() {
        @Override
        public void onReceivedTitle(String title) {
            Log.e("---title", title);
            mTvTitle.setText(title);
        }
    };
    private ActivityWebViewBinding bindingView;

    /**
     * open the Web page:
     *
     * @param mContext context
     * @param url The url of the web page to be loaded
     * @param title title
     */
    public static void loadUrl(Context mContext, String url, String title) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title == null ? "Loading..." : title);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingView = ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(bindingView.getRoot());
        getIntentData();
        initTitle();
        getDataFromBrowser(getIntent());
    }

    private void getIntentData() {
        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
    }

    private void initTitle() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 0);
        initToolBar();
        LinearLayout container = bindingView.llContainer;
        zWebView = ZWebView
                .with(this)
                .setWebParent(container, new LinearLayout.LayoutParams(-1, -1))
                .useWebProgress(ContextCompat.getColor(this, R.color.yellow_500))
                .setOnTitleProgressCallback(onTitleProgressCallback)
                .setOnZWebClientCallback(onZWebClientCallback)
                .addJavascriptInterface("injectedObject", new MyJavascriptInterface(this))
                .loadUrl(mUrl);
        webView = zWebView.getWebView();
    }

    private void initToolBar() {
        // The scrollable title is easy to use, no gradient effect, and there are shadows on both sides of the text
        mTvTitle = bindingView.tvTitle;
        Toolbar toolbar = bindingView.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //Remove the default Title display
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mTvTitle.postDelayed(() -> mTvTitle.setSelected(true), 1900);
        mTvTitle.setText(mTitle);
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

    /**
     * Callback after uploading the picture
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        zWebView.handleFileChooser(requestCode, resultCode, intent);
    }

    /**
     * There will only be one instance of the Activity that uses the singleTask startup mode in the system.
     * If this instance already exists, the intent will be passed to this Activity through onNewIntent.
     * Otherwise, a new Activity instance is created.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getDataFromBrowser(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        zWebView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        zWebView.onResume();
    }

    @Override
    public void onDestroy() {
        zWebView.onDestroy();
        super.onDestroy();
    }

    /**
     * The value passed as a third-party browser opened
     * Scheme: https
     * host: www.jianshu.com
     * path: /p/1cbaf784c29c
     * url = scheme + "://" + host + path;
     */
    private void getDataFromBrowser(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            try {
                String scheme = data.getScheme();
                String host = data.getHost();
                String path = data.getPath();
                String text = "Scheme: " + scheme + "\n" + "host: " + host + "\n" + "path: " + path;
                Log.e("data", text);
                String url = scheme + "://" + host + path;
                webView.loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}