package com.bikroybaba.seller.ui.webview;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bikroybaba.seller.BuildConfig;
import com.bikroybaba.seller.R;
import com.bikroybaba.seller.app.App;

/**
 * Created by moktar on 2020/2/24.
 */

public class WebTools {

    /**
     * Mobile phones below Android 5.0 cannot directly open the link with the mp4 suffix
     *
     * @param url video link
     */
    public static String getVideoHtmlBody(String title, String url) {
        return "<html>" +
                "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width\">" +
                "<title>" + title + "</title>" +
                "<style type=\"text/css\" abt=\"234\"></style>" +
                "</head>" +
                "<body>" +
                "<video controls=\"\" autoplay=\"\" name=\"media\" style=\"display:block;width:100%;position:absolute;left:0;top:20%;\">" +
                "<source src=\"" + url + "\" type=\"video/mp4\">" +
                "</video>" +
                "</body>" +
                "</html>";
    }


    /**
     * Realize text copy function
     *
     * @param content copied text
     */
    public static void copy(String content) {
        if (!TextUtils.isEmpty(content)) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                ClipboardManager clipboard = (ClipboardManager) App.getInstance()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(content);
            } else {
                ClipboardManager clipboard = (ClipboardManager) App.getInstance()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(content, content);
                clipboard.setPrimaryClip(clip);
            }
        }
    }

    /**
     * Use a browser to open the link
     */
    public static void openLink(Context context, String content) {
        if (!TextUtils.isEmpty(content) && content.startsWith("http")) {
            Uri issuesUrl = Uri.parse(content);
            Intent intent = new Intent(Intent.ACTION_VIEW, issuesUrl);
            context.startActivity(intent);
        }
    }

    /**
     * share
     */
    public static void share(Context context, String extraText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.action_share));
        intent.putExtra(Intent.EXTRA_TEXT, extraText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.action_share)));
    }

    /**
     * Find applications by package name, no permission required
     */
    public static boolean hasPackage(Context context, String packageName) {
        if (null == context || TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_GIDS);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // Throws an exception that cannot be found, indicating that the program has been uninstalled
            return false;
        }
    }

    /**
     * Default processing flow: other apps may be evoked on the web page
     */
    public static boolean handleThirdApp(Activity activity, String backUrl) {
        /**Skip directly at the beginning of http*/
        if (backUrl.startsWith("http")) {
            // There may be a prompt to download the Apk file
            if (backUrl.contains(".apk")) {
                startActivity(activity, backUrl);
                return true;
            }
            return false;
        }
        if (backUrl.contains("alipays")) {
            // Web page jump Alipay payment
            if (hasPackage(activity, "com.eg.android.AlipayGphone")) {
                startActivity(activity, backUrl);
            }

        } else if (backUrl.contains("weixin://wap/pay")) {
            // WeChat Pay
            if (hasPackage(activity, "com.tencent.mm")) {
                startActivity(activity, backUrl);
            }
        } else {

            // It will evoke the App in the phone, if you don’t want to be awakened, copy it and add a block.
            boolean isJump = true;
            if (backUrl.contains("tbopen:")// ⁇
                    || backUrl.contains("openapp.jdmobile:")// 3
                    || backUrl.contains("jdmobile:")//Jingdong
                    || backUrl.contains("zhihu:")// Know almost
                    || backUrl.contains("vipshop:")//
                    || backUrl.contains("youku:")//Youku
                    || backUrl.contains("uclink:")// UC
                    || backUrl.contains("ucbrowser:")// UC
                    || backUrl.contains("newsapp:")//
                    || backUrl.contains("sinaweibo:")// Sina Weibo
                    || backUrl.contains("suning:")//
                    || backUrl.contains("pinduoduo:")// Pinduoduo
                    || backUrl.contains("qtt:")//
                    || backUrl.contains("baiduboxapp:")// Baidu
                    || backUrl.contains("baiduhaokan:")// Baidu look
            ) {
                isJump = false;
            }
            if (isJump) {
                startActivity(activity, backUrl);
            }
        }
        return true;
    }

    private static void startActivity(Activity context, String url) {
        try {

            // 用于DeepLink测试
            if (url.startsWith("will://")) {
                Uri uri = Uri.parse(url);
                Log.e("---------scheme", uri.getScheme() + "；host: " + uri.getHost() + "；Id: " + uri.getPathSegments().get(0));
            }

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }


    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void showToast(String content) {
        if (!TextUtils.isEmpty(content)) {
            Toast.makeText(App.getInstance(), content, Toast.LENGTH_SHORT).show();
        }
    }
}
