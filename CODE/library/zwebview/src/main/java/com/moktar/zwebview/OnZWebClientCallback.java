package com.moktar.zwebview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

/**
 * Created by moktar on 2021/02/23.
 */
public abstract class OnZWebClientCallback {

    public void onPageStarted(WebView view, String url, Bitmap favicon) {

    }

    public void onPageFinished(WebView view, String url) {

    }

    public boolean isOpenThirdApp(String url) {
        return !url.startsWith("http:") && !url.startsWith("https:");
    }

    /**
     * @return true Indicates that it was handled by itself
     */
    public boolean onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        return false;
    }
}
