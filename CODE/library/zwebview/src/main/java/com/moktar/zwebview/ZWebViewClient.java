package com.moktar.zwebview;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import java.lang.ref.WeakReference;

/**
 * Created by moktar on 2021/04/12
 * Monitor web links:
 *-According to the logo: call, text, email
 *-Display of progress bar
 *-Add javascript monitor
 *-Arouse JD, Alipay, WeChat native App
 */
public class ZWebViewClient extends WebViewClient {

    private final ZWebView mZWebView;
    private WeakReference<Activity> mActivityWeakReference = null;
    private OnZWebClientCallback onZWebClientCallback;

    ZWebViewClient(Activity activity, ZWebView zWebView) {
        mActivityWeakReference = new WeakReference<Activity>(activity);
        this.mZWebView = zWebView;
    }

    void setOnByWebClientCallback(OnZWebClientCallback onZWebClientCallback) {
        this.onZWebClientCallback = onZWebClientCallback;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (onZWebClientCallback != null) {
            return onZWebClientCallback.isOpenThirdApp(url);
        } else {
            Activity mActivity = this.mActivityWeakReference.get();
            if (mActivity != null && !mActivity.isFinishing()) {
                return ZWebTools.handleThirdApp(mActivity, url);
            } else {
                return !url.startsWith("http:") && !url.startsWith("https:");
            }
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (onZWebClientCallback != null) {
            return onZWebClientCallback.isOpenThirdApp(url);
        } else {
            Activity mActivity = this.mActivityWeakReference.get();
            if (mActivity != null && !mActivity.isFinishing()) {
                return ZWebTools.handleThirdApp(mActivity, url);
            } else {
                return !url.startsWith("http:") && !url.startsWith("https:");
            }
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (onZWebClientCallback != null) {
            onZWebClientCallback.onPageStarted(view, url, favicon);
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // After the html is loaded, add a click js function that monitors the picture
        Activity mActivity = this.mActivityWeakReference.get();
        if (mActivity != null && !mActivity.isFinishing()
                && !ZWebTools.isNetworkConnected(mActivity) && mZWebView.getProgressBar() != null) {
            mZWebView.getProgressBar().hide();
        }
        if (onZWebClientCallback != null) {
            onZWebClientCallback.onPageFinished(view, url);
        }
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        // Execute below 6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return;
        }
        mZWebView.showErrorView();
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        //This method only appeared in android 6.0. Adding a normal page may cause an error page
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            int statusCode = errorResponse.getStatusCode();
//            if (404 == statusCode || 500 == statusCode) {
//                mZWebView.showErrorView();
//            }
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        if (request.isForMainFrame()) {
            // Is it created for the main frame
            mZWebView.showErrorView();
        }
    }

    /**
     * Solve the problem of google play launching WebViewClient.onReceivedSslError
     */
    @Override
    public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
        if (onZWebClientCallback == null || !onZWebClientCallback.onReceivedSslError(view, handler, error)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage("SSL authentication failed，Whether to continue to visit？");
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> handler.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            onZWebClientCallback.onReceivedSslError(view, handler, error);
        }
    }

    /**
     * The video is played in full screen. Press to return to the page to be enlarged.
     */
    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
        if (newScale - oldScale > 7) {
            //Abnormally enlarged, retracted。
            view.setInitialScale((int) (oldScale / newScale * 100));
        }
    }

}
