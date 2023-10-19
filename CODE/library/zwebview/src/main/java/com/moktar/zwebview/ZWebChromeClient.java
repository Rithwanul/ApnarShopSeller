package com.moktar.zwebview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.lang.ref.WeakReference;


/**
 * Created by moktar on 2021/08/27.
 * - Play network video configuration
 * - Upload picture (compatible)
 */
public class ZWebChromeClient extends WebChromeClient {

    private static final int RESULT_CODE_FILE_CHOOSER = 1;
    private static final int RESULT_CODE_FILE_CHOOSER_FOR_ANDROID_5 = 2;
    private final ZWebView mZWebView;
    private WeakReference<Activity> mActivityWeakReference = null;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;
    private View mProgressVideo;
    private View mCustomView;
    private CustomViewCallback mCustomViewCallback;
    private ZFullscreenHolder videoFullView;
    private OnTitleProgressCallback onByWebChromeCallback;

    ZWebChromeClient(Activity activity, ZWebView zWebView) {
        mActivityWeakReference = new WeakReference<Activity>(activity);
        this.mZWebView = zWebView;
    }

    void setOnByWebChromeCallback(OnTitleProgressCallback onByWebChromeCallback) {
        this.onByWebChromeCallback = onByWebChromeCallback;
    }

    /**
     * The method that will be called in full screen when playing online video
     */
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        Activity mActivity = this.mActivityWeakReference.get();
        if (mActivity != null && !mActivity.isFinishing()) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mZWebView.getWebView().setVisibility(View.INVISIBLE);

            // If a view already exists, terminate immediately and create a new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
            videoFullView = new ZFullscreenHolder(mActivity);
            videoFullView.addView(view);
            decor.addView(videoFullView);

            mCustomView = view;
            mCustomViewCallback = callback;
            videoFullView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * It will be called when the video playback exits the full screen
     */
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onHideCustomView() {
        Activity mActivity = this.mActivityWeakReference.get();
        if (mActivity != null && !mActivity.isFinishing()) {
            // Not in full-screen playback
            if (mCustomView == null) {
                return;
            }
            // Restore to the previous screen state
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            mCustomView.setVisibility(View.GONE);
            if (videoFullView != null) {
                videoFullView.removeView(mCustomView);
                videoFullView.setVisibility(View.GONE);
            }
            mCustomView = null;
            mCustomViewCallback.onCustomViewHidden();
            mZWebView.getWebView().setVisibility(View.VISIBLE);
        }
    }

    /**
     * Loading when the video is loading
     */
    @Override
    public View getVideoLoadingProgressView() {
        if (mProgressVideo == null) {
            mProgressVideo = LayoutInflater.from(mZWebView.getWebView().getContext())
                    .inflate(R.layout.z_video_loading_progress, null);
        }
        return mProgressVideo;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        // progress bar
        if (mZWebView.getProgressBar() != null) {
            mZWebView.getProgressBar().setWebProgress(newProgress);
        }
        //When the error page is displayed, the web page is displayed only when the progress reaches 100
        if (mZWebView.getWebView() != null
                && mZWebView.getWebView().getVisibility() == View.INVISIBLE
                && (mZWebView.getErrorView() == null || mZWebView.getErrorView().getVisibility() == View.GONE)
                && newProgress == 100) {
            mZWebView.getWebView().setVisibility(View.VISIBLE);
        }
        if (onByWebChromeCallback != null) {
            onByWebChromeCallback.onProgressChanged(newProgress);
        }
    }

    /**
     * Determine whether it is full screen
     */
    boolean inCustomView() {
        return (mCustomView != null);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        // Set title
        if (onByWebChromeCallback != null) {
            if (mZWebView.getErrorView() != null && mZWebView.getErrorView().getVisibility() == View.VISIBLE) {
                onByWebChromeCallback.onReceivedTitle(TextUtils.isEmpty(mZWebView.getErrorTitle()) ? "Webpage cannot be opened" : mZWebView.getErrorTitle());
            } else {
                onByWebChromeCallback.onReceivedTitle(title);
            }
        }
    }

    //Extend the browser to upload files
    //3.0++Version
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        openFileChooserImpl(uploadMsg);
    }

    //3.0--Version
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooserImpl(uploadMsg);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooserImpl(uploadMsg);
    }

    // For Android > 5.0
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
        openFileChooserImplForAndroid5(uploadMsg);
        return true;
    }

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        Activity mActivity = this.mActivityWeakReference.get();
        if (mActivity != null && !mActivity.isFinishing()) {
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            mActivity.startActivityForResult(Intent.createChooser(intent, "File selection"), RESULT_CODE_FILE_CHOOSER);
        }
    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        Activity mActivity = this.mActivityWeakReference.get();
        if (mActivity != null && !mActivity.isFinishing()) {
            mUploadMessageForAndroid5 = uploadMsg;
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Picture selection");

            mActivity.startActivityForResult(chooserIntent, RESULT_CODE_FILE_CHOOSER_FOR_ANDROID_5);
        }
    }

    /**
     *Below 5.0, the callback after the image is successfully uploaded
     */
    private void uploadMessage(Intent intent, int resultCode) {
        if (null == mUploadMessage) {
            return;
        }
        Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;
    }

    /**
     * Above 5.0, callback after successful upload
     */
    private void uploadMessageForAndroid5(Intent intent, int resultCode) {
        if (null == mUploadMessageForAndroid5) {
            return;
        }
        Uri result = (intent == null || resultCode != Activity.RESULT_OK) ? null : intent.getData();
        if (result != null) {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
        } else {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
        }
        mUploadMessageForAndroid5 = null;
    }

    /**
     * Callback for Activity
     */
    public void handleFileChooser(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RESULT_CODE_FILE_CHOOSER) {
            uploadMessage(intent, resultCode);
        } else if (requestCode == RESULT_CODE_FILE_CHOOSER_FOR_ANDROID_5) {
            uploadMessageForAndroid5(intent, resultCode);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPermissionRequest(PermissionRequest request) {
        super.onPermissionRequest(request);
        //Some pages may crash
//        request.grant(request.getResources());
    }

    ZFullscreenHolder getVideoFullView() {
        return videoFullView;
    }

    @Nullable
    @Override
    public Bitmap getDefaultVideoPoster() {
        if (super.getDefaultVideoPoster() == null) {
            return BitmapFactory.decodeResource(mZWebView.getWebView().getResources(), R.drawable.z_icon_video);
        } else {
            return super.getDefaultVideoPoster();
        }
    }
}
