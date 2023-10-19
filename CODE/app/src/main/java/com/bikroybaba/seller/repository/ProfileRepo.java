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
import com.bikroybaba.seller.util.Utility;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepo {

    private final Utility utility;
    private final ApiInterface apiInterface;
    private final MutableLiveData<ApiResponse> apiResponseMutableLiveData;

    public ProfileRepo(Application application) {
        utility = new Utility(application);
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        apiResponseMutableLiveData = new MutableLiveData<>();
    }

    public void updateSellerProfileImage(MultipartBody.Part image){
        utility.logger("Image"+image.toString());
        Call<ApiResponse> call = apiInterface.updateProfileImage(utility.getAuthorizationKey(),
                utility.getUserId(),utility.getFirebaseToken(),utility.getLangauge(),image);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                utility.logger("response"+response.toString());
                if (response.isSuccessful()){
                    apiResponseMutableLiveData.postValue(response.body());
                }else {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null) {
//                        utility.logger("Failed response"+ apiResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<ApiResponse> getProfileImageUploadResponse(){
        return apiResponseMutableLiveData;
    }
}
