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
import com.bikroybaba.seller.model.remote.request.OfferListRequest;
import com.bikroybaba.seller.model.remote.response.OfferDetailsResponse;
import com.bikroybaba.seller.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferListDataSource extends PageKeyedDataSource<Integer, OfferDetailsResponse> {

    public static final int PAGE_SIZE = 10;
    private static final int FIRST_PAGE = 0;
    private final ApiInterface apiInterface;
    private final Utility utility;
    private final OfferListRequest offerListRequest;
    private final Gson gson;
    private final Context context;
    private final MutableLiveData<String> errorMessage;
    private final EmptyListInterface emptyListInterface;


    public OfferListDataSource(Context context, OfferListRequest offerListRequest,
                               EmptyListInterface emptyListInterface) {
        this.offerListRequest = offerListRequest;
        this.context = context;
        utility = new Utility(context);
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        gson = new Gson();
        errorMessage = new MutableLiveData<>();
        this.emptyListInterface = emptyListInterface;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull LoadInitialCallback<Integer, OfferDetailsResponse> callback) {
        if (utility.isNetworkAvailable()) {
            Call<ApiResponse> call = apiInterface.getOfferList(utility.getAuthorizationKey(),
                    utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(),
                    offerListRequest, FIRST_PAGE, PAGE_SIZE);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call,
                                       @NonNull Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse != null) {
                            if (apiResponse.getCode() == 200) {
                                Type listType = new TypeToken<List<OfferDetailsResponse>>() {
                                }.getType();
                                List<OfferDetailsResponse> offerDetailsResponseList =
                                        gson.fromJson(apiResponse.getData(), listType);
                                emptyListInterface.order(offerDetailsResponseList.size());
                                callback.onResult(offerDetailsResponseList, 0, FIRST_PAGE + 1);
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
                           @NonNull LoadCallback<Integer, OfferDetailsResponse> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
                          @NonNull LoadCallback<Integer, OfferDetailsResponse> callback) {
        if (utility.isNetworkAvailable()) {
            Call<ApiResponse> call = apiInterface.getOfferList(utility.getAuthorizationKey(),
                    utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(),
                    offerListRequest, params.key, PAGE_SIZE);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call,
                                       @NonNull Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse != null) {
                            if (apiResponse.getCode() == 200) {
                                Type listType = new TypeToken<List<OfferDetailsResponse>>() {
                                }.getType();
                                List<OfferDetailsResponse> offerDetailsResponseList =
                                        gson.fromJson(apiResponse.getData(), listType);
                                Log.d("InitLoad", "lastload: ");
                                callback.onResult(offerDetailsResponseList, params.key + 1);
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
