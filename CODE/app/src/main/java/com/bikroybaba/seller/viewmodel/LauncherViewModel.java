package com.bikroybaba.seller.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bikroybaba.seller.database.table.UserProfile;
import com.bikroybaba.seller.model.remote.response.GetConfig;
import com.bikroybaba.seller.repository.LauncherRepo;

public class LauncherViewModel extends AndroidViewModel {

    private final LauncherRepo launcherRepo;
    public LauncherViewModel(@NonNull Application application) {
        super(application);
        launcherRepo = new LauncherRepo(application);
    }

    public LiveData<GetConfig> getConfigLiveData(){

        return launcherRepo.getConfigLiveData();
    }

    public LiveData<UserProfile> getUserLiveData(){
        return launcherRepo.getUserdata();
    }
}
