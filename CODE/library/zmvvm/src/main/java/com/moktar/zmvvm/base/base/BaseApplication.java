package com.moktar.zmvvm.base.base;


import android.content.Context;

import androidx.multidex.MultiDexApplication;


public class BaseApplication extends MultiDexApplication {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
