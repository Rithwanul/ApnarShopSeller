package com.bikroybaba.seller.ui.webview;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;


/**
 * Created by moktar on 2020/11/29.
 * js communication interface
 */
public class MyJavascriptInterface {

    private final Context context;

    public MyJavascriptInterface(Context context) {
        this.context = context;
    }

    /**
     * The front-end code is embedded in js:
     * The imageClick name should be the same as the js function method name
     *
     * @param src image link
     */
    @JavascriptInterface
    public void imageClick(String src) {
        Log.e("imageClick", "----Clicked the picture");
        Log.e("---src", src);
        WebTools.showToast(src);
    }

    /**
     * The front-end code is embedded in js
     * Traverse the <li> node
     *
     * @param type <li>The value of the type attribute under the node
     * @param item_pk item_pk attribute value
     */
    @JavascriptInterface
    public void textClick(String type, String item_pk) {
        if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(item_pk)) {
            Log.e("textClick", "----Text clicked");
            Log.e("type", type);
            Log.e("item_pk", item_pk);
            WebTools.showToast("type: " + type + ", item_pk:" + item_pk);
        }
    }

    /**
     * The js used by the webpage, the method has no parameters
     */
    @JavascriptInterface
    public void startFunction() {
        Log.e("startFunction", "----No parameters");
        WebTools.showToast("No-parameter method");
    }

    /**
     * The js used by the webpage has parameters in the method, and the parameter name is data
     *
     * @param data parameter name in web page js
     */
    @JavascriptInterface
    public void startFunction(String data) {
        Log.e("startFunction", "----Parametric method: " + data);
        WebTools.showToast("----Parametric method: " + data);
    }

    /**
     * Get webpage source code
     */
    @JavascriptInterface
    public void showSource(String html) {
        Log.e("showSourceCode", html);
    }
}
