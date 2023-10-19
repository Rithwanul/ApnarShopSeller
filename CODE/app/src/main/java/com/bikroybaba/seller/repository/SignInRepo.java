package com.bikroybaba.seller.repository;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.os.AsyncTask;
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
import com.bikroybaba.seller.model.remote.request.LogIn;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInRepo {

    private final ApiInterface apiInterface;
    private final MutableLiveData<ApiResponse> profileMutableLiveData;
    private final MutableLiveData<ApiResponse> apiResponseMutableLiveData;
    private final Utility utility;
    private final UserProfileDao userProfileDao;


    public SignInRepo(Application application) {
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        userProfileDao = BBDatabase.getDatabase(application).user_dao();
        profileMutableLiveData = new MutableLiveData<>();
        apiResponseMutableLiveData = new MutableLiveData<>();
        utility = new Utility(application);
    }

    public void sendLoginInfo(LogIn logIn) {
        Call<ApiResponse> call = apiInterface.login(utility.getAuthorizationKey(),
                KeyWord.USERID, KeyWord.TOKEN, utility.getLangauge(), logIn);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    profileMutableLiveData.postValue(apiResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public LiveData<ApiResponse> getLoginResponse() {

        return profileMutableLiveData;
    }

    public void forgetPassword(LogIn logIn) {
        Call<ApiResponse> call = apiInterface.forgetPassword(utility.getAuthorizationKey(),
                KeyWord.USERID, utility.getFirebaseToken(), utility.getLangauge(), logIn);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    apiResponseMutableLiveData.postValue(apiResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public LiveData<ApiResponse> getForgetPasswordResponse() {

        return apiResponseMutableLiveData;
    }

    public void insertUser(UserProfile userProfile) {
        new InsertAsyncTask(userProfileDao).execute(userProfile);
    }

    private static class InsertAsyncTask extends AsyncTask<UserProfile, Void, Void> {

        private final UserProfileDao mAsyncTaskDao;

        private InsertAsyncTask(UserProfileDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserProfile... params) {
            mAsyncTaskDao.insertUser(params[0]);
            return null;
        }
    }
}

