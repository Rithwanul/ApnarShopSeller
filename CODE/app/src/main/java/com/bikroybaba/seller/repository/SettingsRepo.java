package com.bikroybaba.seller.repository;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bikroybaba.seller.http.ApiClient;
import com.bikroybaba.seller.http.ApiInterface;
import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.model.remote.request.ChangePassword;
import com.bikroybaba.seller.model.remote.request.Settings;
import com.bikroybaba.seller.util.Utility;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsRepo {
    private final MutableLiveData<Settings> settingsMutableLiveData;
    private final MutableLiveData<ApiResponse> apiResponseMutableLiveData;

    private final ApiInterface apiInterface;
    private final Utility utility;
    private final Gson gson;

    public SettingsRepo(Application application) {
        settingsMutableLiveData = new MutableLiveData<>();
        apiResponseMutableLiveData = new MutableLiveData<>();
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        utility = new Utility(application);
        gson = new Gson();
    }

    public LiveData<Settings> getSettings() {
        Call<ApiResponse> call = apiInterface.getSettings(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Settings settings = gson.fromJson(apiResponse.getData(), Settings.class);
                        settingsMutableLiveData.postValue(settings);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

        return settingsMutableLiveData;
    }

    public void changePassword(ChangePassword changePassword) {
        Call<ApiResponse> call = apiInterface.changePassword(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), changePassword);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    apiResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void updateSettings(Settings settings) {
        Call<ApiResponse> call = apiInterface.updateSettings(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), settings);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    apiResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<ApiResponse> getChangePasswordResponse() {

        return apiResponseMutableLiveData;
    }
}
