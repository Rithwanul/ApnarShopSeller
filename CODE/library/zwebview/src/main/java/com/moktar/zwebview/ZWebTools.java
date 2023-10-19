package com.moktar.zwebview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

/**
 * @author moktar
 */
public class ZWebTools {

    /**
     * Mobile phones below Android 5.0 cannot directly open the link with the mp4 suffix
     *
     * @param url Video link
     */
    static String getVideoHtmlBody(String url) {
        return "<html>" +
                "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width\">" +
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
     * Default processing flow: other apps may be evoked in the web page
     */
    public static boolean handleThirdApp(Activity activity, String backUrl) {
        /**Skip directly at the beginning of http*/
        if (backUrl.startsWith("http")) {
            //There may be a prompt to download the Apk file
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

            // It will evoke the App in the phone. If you don’t want to be awakened, copy it and add a block.
            boolean isJump = true;
            if (backUrl.contains("tbopen:")// Taobao
                    || backUrl.contains("openapp.jdmobile:")// Jingdong
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

    private static void startActivity(Context context, String url) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Determine whether the network is connected
     */
    static boolean isNetworkConnected(Context context) {
        try {
            if (context != null) {
                @SuppressWarnings("static-access")
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();
                return info != null && info.isConnected();
            } else {
                /**If the context is empty, it returns false, indicating that the network is not connected*/
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static String getUrl(String url) {
        String urlResult = "";
        if (TextUtils.isEmpty(url)) {
            // Empty url
            return urlResult;

        } else if (!url.startsWith("http") && url.contains("http")) {
            // Http and not in the head
            urlResult = url.substring(url.indexOf("http"));

        } else if (url.startsWith("www")) {
            // Start with "www"
            urlResult = "http://" + url;

        } else if (!url.startsWith("http") && (url.contains(".me") || url.contains(".com") || url.contains(".cn"))) {
            // Does not start with "http" and has a suffix
            urlResult = "http://www." + url;

        } else if (!url.startsWith("http") && !url.contains("www")) {
            // When entering plain text
            urlResult = "http://m5.baidu.com/s?from=124n&word=" + url;
        }
        return urlResult;
    }
}
