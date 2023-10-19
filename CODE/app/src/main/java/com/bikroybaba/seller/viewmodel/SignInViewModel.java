package com.bikroybaba.seller.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bikroybaba.seller.database.table.UserProfile;
import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.model.remote.request.LogIn;
import com.bikroybaba.seller.repository.SignInRepo;

public class SignInViewModel extends AndroidViewModel {

    private final SignInRepo signInRepo;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        signInRepo = new SignInRepo(application);
    }

    public void sendLogin(LogIn logIn) {
        signInRepo.sendLoginInfo(logIn);
    }

    public LiveData<ApiResponse> getLoginResponse() {
        return signInRepo.getLoginResponse();
    }

    public void forgetPassword(LogIn logIn) {
        signInRepo.forgetPassword(logIn);
    }

    public LiveData<ApiResponse> getForgetPasswordResponse() {
        return signInRepo.getForgetPasswordResponse();
    }
    public void insertUser(UserProfile userProfile) {
        signInRepo.insertUser(userProfile);
    }
}
