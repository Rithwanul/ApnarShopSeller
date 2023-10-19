package com.bikroybaba.seller.app;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.NonNull;

import com.moktar.zmvvm.base.base.BaseApplication;
import com.moktar.zmvvm.base.utils.LocaleHelper;

public class App extends BaseApplication {

    private static App app;
    private String TAG = "app";

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        setLanguageFromNewConfig(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    /**
     * also handle change  language if  device language changed
     */
    private void setLanguageFromNewConfig(Configuration newConfig) {
        LocaleHelper.onAttach(this);
        Log.d(TAG, "setLanguageFromNewConfig: " + newConfig.locale.getLanguage());
    }
}
