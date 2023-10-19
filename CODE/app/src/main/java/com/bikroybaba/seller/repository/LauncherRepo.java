package com.bikroybaba.seller.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bikroybaba.seller.database.BBDatabase;
import com.bikroybaba.seller.database.Dao.UserProfileDao;
import com.bikroybaba.seller.database.table.UserProfile;
import com.bikroybaba.seller.http.ApiClient;
import com.bikroybaba.seller.http.ApiInterface;
import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.model.remote.response.GetConfig;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LauncherRepo {

    private final MutableLiveData<GetConfig> configMutableLiveData;
    private final MutableLiveData<Boolean> progressbarMutableLiveData;
    private final ApiInterface apiInterface;
    private final Utility utility;
    private final Gson gson;
    private final UserProfileDao userProfileDao;
    private GetConfig config;

    public LauncherRepo(Application application) {
        configMutableLiveData = new MutableLiveData<>();
        progressbarMutableLiveData = new MutableLiveData<>();
        userProfileDao = BBDatabase.getDatabase(application).user_dao();
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        utility = new Utility(application);
        gson = new Gson();
    }

    public LiveData<GetConfig> getConfigLiveData() {
        progressbarMutableLiveData.postValue(true);
        Call<ApiResponse> call =
                apiInterface.getConfig(utility.getAuthorizationKey(), KeyWord.USERID, KeyWord.TOKEN,
                        utility.getLangauge());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse != null && apiResponse.getCode() == 200) {
                            config = gson.fromJson(apiResponse.getData().toString(), GetConfig.class);
                            configMutableLiveData.postValue(config);
                            progressbarMutableLiveData.postValue(false);

                        } else {
                            configMutableLiveData.postValue(config);
                        }
                    } else {
                        utility.logger("Config failed");
                    }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                progressbarMutableLiveData.postValue(false);
            }
        });
        return configMutableLiveData;
    }

    public LiveData<UserProfile> getUserdata() {
        return userProfileDao.getUserProfile();
    }

}
