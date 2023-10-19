package com.bikroybaba.seller.repository;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bikroybaba.seller.http.ApiClient;
import com.bikroybaba.seller.http.ApiInterface;
import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.model.remote.request.OrderDetailsRequest;
import com.bikroybaba.seller.model.remote.request.OrderStatusRequest;
import com.bikroybaba.seller.model.remote.response.OrderDetailsResponse;
import com.bikroybaba.seller.util.Utility;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepo {
    private final MutableLiveData<OrderDetailsResponse> orderDetailsMutableLiveData;
    private final MutableLiveData<ApiResponse> apiResponseMutableLiveData;
    private final Utility utility;
    private final ApiInterface apiInterface;
    private final Gson gson;

    public OrderRepo(Context context) {
        apiResponseMutableLiveData = new MutableLiveData<>();
        orderDetailsMutableLiveData = new MutableLiveData<>();
        utility = new Utility(context);
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        gson = new Gson();
    }


    public void updateOrderStatus(OrderStatusRequest orderStatusRequest) {
        Call<ApiResponse> call = apiInterface.updateOrderStatus(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), orderStatusRequest);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse>
                                           call, @NonNull Response<ApiResponse> response) {
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

    public MutableLiveData<ApiResponse> getOrderStatusResponse() {

        return apiResponseMutableLiveData;
    }

    public MutableLiveData<OrderDetailsResponse> getOrderDetails(OrderDetailsRequest orderDetailsRequest) {
        Call<ApiResponse> call = apiInterface.getOrderDetails(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), orderDetailsRequest);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        orderDetailsMutableLiveData
                                .postValue(gson.fromJson(apiResponse.getData(), OrderDetailsResponse.class));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return orderDetailsMutableLiveData;
    }
}
