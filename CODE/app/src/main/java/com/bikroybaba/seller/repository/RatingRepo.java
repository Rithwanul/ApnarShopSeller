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
import com.bikroybaba.seller.model.remote.response.Rating;
import com.bikroybaba.seller.util.Utility;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingRepo {
    Application application;
    private final MutableLiveData<Rating> ratingMutableLiveData;
    private final Utility utility;
    private final ApiInterface apiInterface;
    private final Gson gson;

    public RatingRepo(Application application) {
        this.application = application;
        ratingMutableLiveData = new MutableLiveData<>();
        utility = new Utility(application);
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        gson = new Gson();
    }

    public LiveData<Rating> getRating() {
        Call<ApiResponse> call = apiInterface.getRating(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Rating rating = gson.fromJson(apiResponse.getData(), Rating.class);
                        ratingMutableLiveData.postValue(rating);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return ratingMutableLiveData;
    }
}
