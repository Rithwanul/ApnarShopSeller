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
import com.bikroybaba.seller.model.remote.request.AddOfferProduct;
import com.bikroybaba.seller.model.remote.request.Category;
import com.bikroybaba.seller.model.remote.request.OfferCategoryRequest;
import com.bikroybaba.seller.model.remote.request.OfferCreate;
import com.bikroybaba.seller.model.remote.request.OfferDetailsRequest;
import com.bikroybaba.seller.model.remote.request.OfferListRequest;
import com.bikroybaba.seller.model.remote.request.OfferStatus;
import com.bikroybaba.seller.model.remote.request.OfferUpdate;
import com.bikroybaba.seller.model.remote.response.OfferCategory;
import com.bikroybaba.seller.model.remote.response.OfferDetailsResponse;
import com.bikroybaba.seller.model.remote.response.OfferListResponse;
import com.bikroybaba.seller.model.remote.response.OfferTypeResponse;
import com.bikroybaba.seller.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferRepo {

    private final ApiInterface apiInterface;
    private final MutableLiveData<List<OfferTypeResponse>> offerTypeMutableLiveData;
    private final MutableLiveData<List<OfferListResponse>> offerListMutableLiveData;
    private final MutableLiveData<List<OfferCategory>> offerCategoryMutableLiveData;
    private final MutableLiveData<OfferDetailsResponse> offerDetailsMutableLiveData;
    private final MutableLiveData<ApiResponse> apiResponseMutableLiveData;
    private final MutableLiveData<ApiResponse> updateOfferApiResponseMutableLiveData;
    private final MutableLiveData<ApiResponse> createOfferApiResponseMutableLiveData;
    private final MutableLiveData<ApiResponse> statusUpdateMutableLiveData;
    private final MutableLiveData<List<Category>> categoryMutableLiveData;

    private final Utility utility;
    private final Gson gson;
    private final UserProfileDao userProfileDao;

    public OfferRepo(Application application) {
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        userProfileDao = BBDatabase.getDatabase(application).user_dao();
        offerTypeMutableLiveData = new MutableLiveData<>();
        offerListMutableLiveData = new MutableLiveData<>();
        offerCategoryMutableLiveData = new MutableLiveData<>();
        offerDetailsMutableLiveData = new MutableLiveData<>();
        apiResponseMutableLiveData = new MutableLiveData<>();
        updateOfferApiResponseMutableLiveData = new MutableLiveData<>();
        createOfferApiResponseMutableLiveData = new MutableLiveData<>();
        statusUpdateMutableLiveData = new MutableLiveData<>();
        categoryMutableLiveData = new MutableLiveData<>();
        utility = new Utility(application);
        gson = new Gson();
    }

    public MutableLiveData<List<OfferTypeResponse>> getOfferTypeList() {
        Call<ApiResponse> call = apiInterface.getOfferType(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type list = new TypeToken<List<OfferTypeResponse>>() {
                        }.getType();
                        List<OfferTypeResponse> offerTypeResponseList =
                                gson.fromJson(apiResponse.getData(), list);
                        offerTypeMutableLiveData.postValue(offerTypeResponseList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return offerTypeMutableLiveData;
    }

    public MutableLiveData<List<OfferListResponse>> getOfferList(OfferListRequest offerListRequest) {
        Call<ApiResponse> call = apiInterface.getOfferList(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(),
                offerListRequest, 0, 10000);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type list = new TypeToken<List<OfferListResponse>>() {
                        }.getType();
                        List<OfferListResponse> offerListResponseList =
                                gson.fromJson(apiResponse.getData(), list);
                        offerListMutableLiveData.postValue(offerListResponseList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

            }
        });
        return offerListMutableLiveData;
    }

    public MutableLiveData<List<OfferCategory>> getOfferCategory(OfferCategoryRequest offerCategoryRequest) {

        Call<ApiResponse> call = apiInterface.getOfferCategory(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), offerCategoryRequest);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type list = new TypeToken<List<OfferCategory>>() {
                        }.getType();
                        List<OfferCategory> offerCategoryList =
                                gson.fromJson(apiResponse.getData(), list);
                        offerCategoryMutableLiveData.postValue(offerCategoryList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return offerCategoryMutableLiveData;
    }

    public void sendOfferDetailsRequest(OfferDetailsRequest offerDetailsRequest) {
        Call<ApiResponse> call = apiInterface.getOfferDetails(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), offerDetailsRequest);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        offerDetailsMutableLiveData.postValue(gson.fromJson(apiResponse.getData(),
                                OfferDetailsResponse.class));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public MutableLiveData<OfferDetailsResponse> getOfferDetailsResponse() {
        return offerDetailsMutableLiveData;
    }

    public void updateOfferStatus(OfferStatus offerStatus) {
        Call<ApiResponse> call = apiInterface.updateOfferStatus(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), offerStatus);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    statusUpdateMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void createOffer(OfferCreate offerCreate) {
        Call<ApiResponse> call = apiInterface.createOffer(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), offerCreate);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    createOfferApiResponseMutableLiveData.postValue(response.body());
                } else {
                    createOfferApiResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void updateOffer(OfferUpdate offerUpdate) {
        Call<ApiResponse> call = apiInterface.updateOffer(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), offerUpdate);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    updateOfferApiResponseMutableLiveData.postValue(response.body());
                } else {
                    updateOfferApiResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void addOfferProduct(AddOfferProduct offerProduct) {
        Call<ApiResponse> call = apiInterface.addOfferProduct(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), offerProduct);
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

    public MutableLiveData<ApiResponse> getAPIResponse() {
        return apiResponseMutableLiveData;
    }

    public MutableLiveData<ApiResponse> getUpdateResponse() {
        return updateOfferApiResponseMutableLiveData;
    }

    public MutableLiveData<ApiResponse> createOfferResponse() {
        return createOfferApiResponseMutableLiveData;
    }

    public MutableLiveData<ApiResponse> statusUpdateResponse() {
        return statusUpdateMutableLiveData;
    }

    public LiveData<UserProfile> getUserdata() {
        return userProfileDao.getUserProfile();
    }
}
