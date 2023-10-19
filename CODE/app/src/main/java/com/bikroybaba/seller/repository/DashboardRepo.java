package com.bikroybaba.seller.repository;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bikroybaba.seller.database.BBDatabase;
import com.bikroybaba.seller.database.Dao.UserProfileDao;
import com.bikroybaba.seller.database.table.UserProfile;
import com.bikroybaba.seller.http.ApiClient;
import com.bikroybaba.seller.http.ApiInterface;
import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.model.remote.response.ProductCount;
import com.bikroybaba.seller.util.Utility;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepo {

    private final MutableLiveData<ProductCount> productCountMutableLiveData;
    private final ApiInterface apiInterface;
    private final Utility utility;
    private final Gson gson;
    private final UserProfileDao userProfileDao;

    public DashboardRepo(Application application) {
        productCountMutableLiveData = new MutableLiveData<>();
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        userProfileDao = BBDatabase.getDatabase(application).user_dao();
        utility = new Utility(application);
        gson = new Gson();
    }

    public LiveData<ProductCount> getProductCount(){
        Call<ApiResponse> call = apiInterface.getDashboardInfo(utility.getAuthorizationKey(),
                utility.getUserId(),utility.getFirebaseToken(),utility.getLangauge());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                    if (response.isSuccessful()){
                        ApiResponse apiResponse = response.body();
                        if (apiResponse != null && apiResponse.getCode() ==200){
                           ProductCount productCount =
                                   gson.fromJson(apiResponse.getData(),ProductCount.class);
                           productCountMutableLiveData.postValue(productCount);
                        }
                    }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return productCountMutableLiveData;
    }
    public LiveData<UserProfile> getUserdata() {
        return userProfileDao.getUserProfile();
    }
}
