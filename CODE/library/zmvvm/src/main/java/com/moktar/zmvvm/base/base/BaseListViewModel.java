package com.moktar.zmvvm.base.base;

import android.app.Application;

import androidx.annotation.NonNull;

public class BaseListViewModel extends BaseViewModel {

    public int mPage = 0;

    public BaseListViewModel(@NonNull Application application) {
        super(application);
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int mPage) {
        this.mPage = mPage;
    }
}
