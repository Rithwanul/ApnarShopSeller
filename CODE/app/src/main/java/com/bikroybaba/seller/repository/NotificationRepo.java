package com.bikroybaba.seller.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bikroybaba.seller.http.ApiClient;
import com.bikroybaba.seller.http.ApiInterface;
import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.model.remote.request.UpdateNotificationRequest;
import com.bikroybaba.seller.model.remote.response.NotificationResponse;
import com.bikroybaba.seller.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationRepo {
    private final ApiInterface apiInterface;
    private final MutableLiveData<List<NotificationResponse>> notificationMutableLiveData;
    private final MutableLiveData<ApiResponse> apiResponseMutableLiveData;
    private final Utility utility;
    private final Gson gson;

    public NotificationRepo(Application application) {
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        notificationMutableLiveData = new MutableLiveData<>();
        apiResponseMutableLiveData = new MutableLiveData<>();
        utility = new Utility(application);
        gson = new Gson();
    }

    public MutableLiveData<List<NotificationResponse>> getNotification(){
        Call<ApiResponse> call =
                apiInterface.getNotification(utility.getAuthorizationKey(),utility.getUserId(),
                        utility.getFirebaseToken(),utility.getLangauge());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                    if (response.isSuccessful()){
                        ApiResponse apiResponse = response.body();
                        if (apiResponse != null && apiResponse.getCode() ==200){
                            Type listType = new TypeToken<List<NotificationResponse>>() {
                            }.getType();
                            List<NotificationResponse> notificationList =
                                    gson.fromJson(apiResponse.getData(), listType);
                            notificationMutableLiveData.postValue(notificationList);
                        }
                    }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

            }
        });

        return  notificationMutableLiveData;
    }

    public void updateNotification(UpdateNotificationRequest updateNotificationRequest){
        Call<ApiResponse> call =
                apiInterface.deleteNotification(utility.getAuthorizationKey(),utility.getUserId(),
                        utility.getFirebaseToken(),utility.getLangauge(),updateNotificationRequest);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    ApiResponse apiResponse = response.body();
                    apiResponseMutableLiveData.postValue(apiResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

            }
        });

    }

    public MutableLiveData<ApiResponse> getUpdatedNotificationResponse(){
        return apiResponseMutableLiveData;
    }
}
