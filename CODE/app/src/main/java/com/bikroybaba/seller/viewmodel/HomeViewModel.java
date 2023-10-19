package com.bikroybaba.seller.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.bikroybaba.seller.database.table.UserProfile;
import com.bikroybaba.seller.repository.HomeRepo;
import com.moktar.zmvvm.base.base.BaseViewModel;

public class HomeViewModel extends BaseViewModel {

    private final HomeRepo homeRepo;
    public HomeViewModel(@NonNull Application application) {
        super(application);
        homeRepo = new HomeRepo(application);
    }
    public LiveData<UserProfile> getUserLiveData(){
        return homeRepo.getUserdata();
    }

    public void deleteUser(){
        homeRepo.deleteUser();
    }
}