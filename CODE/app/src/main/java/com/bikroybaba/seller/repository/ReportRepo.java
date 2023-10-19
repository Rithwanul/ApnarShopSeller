package com.bikroybaba.seller.repository;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bikroybaba.seller.http.ApiClient;
import com.bikroybaba.seller.http.ApiInterface;
import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.model.remote.request.Category;
import com.bikroybaba.seller.model.remote.request.ProductType;
import com.bikroybaba.seller.model.remote.request.ReportRequest;
import com.bikroybaba.seller.model.remote.response.ProductName;
import com.bikroybaba.seller.model.remote.response.ReportResponse;
import com.bikroybaba.seller.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportRepo {

    private final MutableLiveData<List<ProductType>> productTypeMutableLiveData;
    private final MutableLiveData<List<ProductName>> productNameMutableLiveData;
    private final MutableLiveData<ReportResponse> reportResponseMutableLiveData;
    private final MutableLiveData<List<Category>> categoryMutableLiveData;

    private final Utility utility;
    private final Gson gson;
    private final ApiInterface apiInterface;

    public ReportRepo(Application application) {
        productTypeMutableLiveData = new MutableLiveData<>();
        productNameMutableLiveData = new MutableLiveData<>();
        reportResponseMutableLiveData = new MutableLiveData<>();
        categoryMutableLiveData = new MutableLiveData<>();
        utility = new Utility(application);
        gson = new Gson();
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    }

    public MutableLiveData<List<ProductType>> getProductTypes(Category category) {
        Call<ApiResponse> call = apiInterface.getProductType(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), category);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type listType = new TypeToken<List<ProductType>>() {
                        }.getType();
                        List<ProductType> productTypeList =
                                gson.fromJson(apiResponse.getData(), listType);
                        productTypeMutableLiveData.postValue(productTypeList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return productTypeMutableLiveData;
    }

    public MutableLiveData<List<ProductName>> getProduct(ProductType productType) {
        Call<ApiResponse> call = apiInterface.getProduct(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), productType);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type listType = new TypeToken<List<ProductName>>() {
                        }.getType();
                        List<ProductName> productTypeList =
                                gson.fromJson(apiResponse.getData(), listType);
                        productNameMutableLiveData.postValue(productTypeList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return productNameMutableLiveData;
    }

    public MutableLiveData<List<Category>> getProductCategory() {
        Call<ApiResponse> call = apiInterface.getProductCategory(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type listType = new TypeToken<List<Category>>() {
                        }.getType();
                        List<Category> categoryList = gson.fromJson(apiResponse.getData(), listType);
                        categoryMutableLiveData.postValue(categoryList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

        return categoryMutableLiveData;
    }

    public void getReport(ReportRequest reportRequest) {
        Call<ApiResponse> call = apiInterface.getReport(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), reportRequest);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        reportResponseMutableLiveData
                                .postValue(gson.fromJson(apiResponse.getData(), ReportResponse.class));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public MutableLiveData<ReportResponse> getReportResponse() {
        return reportResponseMutableLiveData;
    }
}
