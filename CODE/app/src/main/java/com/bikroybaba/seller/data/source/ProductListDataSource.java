package com.bikroybaba.seller.data.source;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.bikroybaba.seller.http.ApiClient;
import com.bikroybaba.seller.http.ApiInterface;
import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.entity.AllProduct;
import com.bikroybaba.seller.model.remote.request.FilterProduct;
import com.bikroybaba.seller.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductListDataSource extends PageKeyedDataSource<Integer, AllProduct> {

    public static final int PAGE_SIZE = 10;
    private static final int FIRST_PAGE = 0;
    private final ApiInterface apiInterface;
    private final Utility utility;
    private final FilterProduct filterProduct;
    private final Gson gson;
    private final Context context;
    private final MutableLiveData<String> errorMessage;
    private final EmptyListInterface emptyListInterface;


    public ProductListDataSource(Context context, FilterProduct filterProduct,
                                 EmptyListInterface emptyListInterface) {
        this.filterProduct = filterProduct;
        this.context = context;
        utility = new Utility(context);
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        gson = new Gson();
        errorMessage = new MutableLiveData<>();
        this.emptyListInterface = emptyListInterface;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull LoadInitialCallback<Integer, AllProduct> callback) {
        if (utility.isNetworkAvailable()) {
            Call<ApiResponse> call = apiInterface.getAllProduct(utility.getAuthorizationKey(),
                    utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(),
                    filterProduct, FIRST_PAGE, PAGE_SIZE);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call,
                                       @NonNull Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse != null) {
                            if (apiResponse.getCode() == 200) {
                                Type listType = new TypeToken<List<AllProduct>>() {
                                }.getType();
                                List<AllProduct> offerProductListResponseList =
                                        gson.fromJson(apiResponse.getData(), listType);
                                emptyListInterface.order(offerProductListResponseList.size());
                                callback.onResult(offerProductListResponseList, 0, FIRST_PAGE + 1);
                            } else {
                                errorMessage.postValue(apiResponse.getData().getAsString());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    errorMessage.postValue("Internal server error");
                }
            });
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<Integer, AllProduct> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
                          @NonNull LoadCallback<Integer, AllProduct> callback) {

        if (utility.isNetworkAvailable()) {
            Call<ApiResponse> call = apiInterface.getAllProduct(utility.getAuthorizationKey(),
                    utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), filterProduct, params.key, PAGE_SIZE);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call,
                                       @NonNull Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse != null) {
                            if (apiResponse.getCode() == 200) {
                                Type listType = new TypeToken<List<AllProduct>>() {
                                }.getType();
                                List<AllProduct> offerProductListResponseList =
                                        gson.fromJson(apiResponse.getData(), listType);
                                Log.d("InitLoad", "lastload: ");
                                callback.onResult(offerProductListResponseList, params.key + 1);
                            } else {
                                errorMessage.postValue(apiResponse.getData().getAsString());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    errorMessage.postValue("Internal server error");
                }
            });
        }
    }

    public MutableLiveData<String> getError() {

        return errorMessage;
    }
}
