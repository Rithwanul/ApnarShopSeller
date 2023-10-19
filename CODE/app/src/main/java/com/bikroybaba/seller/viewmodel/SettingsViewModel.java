package com.bikroybaba.seller.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.model.remote.request.ChangePassword;
import com.bikroybaba.seller.model.remote.request.Settings;
import com.bikroybaba.seller.repository.SettingsRepo;

public class SettingsViewModel extends AndroidViewModel {

    private final SettingsRepo settingsRepo;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        settingsRepo = new SettingsRepo(application);
    }

    public LiveData<Settings> getSettings(){

        return settingsRepo.getSettings();
    }

    public void changePassword(ChangePassword changePassword){
        settingsRepo.changePassword(changePassword);
    }

    public LiveData<ApiResponse> getChangePasswordResponse(){

        return settingsRepo.getChangePasswordResponse();
    }

    public void updateSettings(Settings settings){
        settingsRepo.updateSettings(settings);
    }
}
