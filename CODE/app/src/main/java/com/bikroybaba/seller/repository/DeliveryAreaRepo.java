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
import com.bikroybaba.seller.model.entity.Area;
import com.bikroybaba.seller.model.entity.Delivery;
import com.bikroybaba.seller.model.remote.request.City;
import com.bikroybaba.seller.model.remote.request.District;
import com.bikroybaba.seller.model.remote.request.Division;
import com.bikroybaba.seller.model.remote.request.UpdateDeliveryArea;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryAreaRepo {

    private final MutableLiveData<List<Division>> divisionMutableLiveData;
    private final MutableLiveData<List<District>> districtMutableLiveData;
    private final MutableLiveData<List<City>> cityMutableLiveData;
    private final MutableLiveData<List<Area>> areaMutableLiveData;
    private final MutableLiveData<Delivery> getDeliveryAreaMutableLiveData;
    private final MutableLiveData<ApiResponse> apiResponseMutableLiveData;
    private final Utility utility;
    private final ApiInterface apiInterface;
    private final Gson gson;
    private final MutableLiveData<Boolean> error;
    private List<Division> divisionList;
    private List<District> districtList;
    private List<City> cityList;
    private List<Area> areaList;

    public DeliveryAreaRepo(Application application) {
        divisionMutableLiveData = new MutableLiveData<>();
        districtMutableLiveData = new MutableLiveData<>();
        cityMutableLiveData = new MutableLiveData<>();
        areaMutableLiveData = new MutableLiveData<>();
        getDeliveryAreaMutableLiveData = new MutableLiveData<>();
        apiResponseMutableLiveData = new MutableLiveData<>();
        error = new MutableLiveData<>();
        utility = new Utility(application);
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        gson = new Gson();
    }

    public LiveData<Delivery> getDeliveryArea() {
        Call<ApiResponse> call = apiInterface.getDeliveryArea(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse != null && apiResponse.getCode() == 200) {
                            Delivery getDeliveryArea =
                                    gson.fromJson(apiResponse.getData(), Delivery.class);
                            getDeliveryAreaMutableLiveData.postValue(getDeliveryArea);
                        }
                    }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return getDeliveryAreaMutableLiveData;
    }

    public void updateDeliveryArea(UpdateDeliveryArea updateDeliveryArea) {
        Call<ApiResponse> call = apiInterface.updateDeliveryArea(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), updateDeliveryArea);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    apiResponseMutableLiveData.postValue(response.body());
                } else {
                    error.postValue(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<ApiResponse> getUpdateDeliveryAreaResponse() {

        return apiResponseMutableLiveData;
    }

    public LiveData<Boolean> errorDeliveryArea() {

        return error;
    }

    public LiveData<List<Division>> getDivisionList() {
        Call<ApiResponse> call = apiInterface.getDivision(utility.getAuthorizationKey(),
                KeyWord.USERID, KeyWord.TOKEN, utility.getLangauge());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type listType = new TypeToken<List<Division>>() {
                        }.getType();
                        divisionList = gson.fromJson(apiResponse.getData(), listType);
                        divisionMutableLiveData.postValue(divisionList);
                    } else {
                        divisionMutableLiveData.postValue(divisionList);
                        utility.logger("Get Division" + divisionList.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
        return divisionMutableLiveData;
    }

    public LiveData<List<District>> getDistrictList(Division division) {
        utility.logger("district");
        Call<ApiResponse> call = apiInterface.getDistrict(utility.getAuthorizationKey(),
                KeyWord.USERID, KeyWord.TOKEN, utility.getLangauge(), division);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type listType = new TypeToken<List<District>>() {
                        }.getType();
                        districtList = gson.fromJson(apiResponse.getData(), listType);
                        districtMutableLiveData.postValue(districtList);
                    } else {
                        districtMutableLiveData.postValue(districtList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return districtMutableLiveData;
    }

    public LiveData<List<City>> getCityList(District district) {
        Call<ApiResponse> call = apiInterface.getCity(utility.getAuthorizationKey(),
                KeyWord.USERID, KeyWord.TOKEN, utility.getLangauge(), district);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type listType = new TypeToken<List<City>>() {
                        }.getType();
                        cityList = gson.fromJson(apiResponse.getData(), listType);
                        cityMutableLiveData.postValue(cityList);
                    } else {
                        cityMutableLiveData.postValue(cityList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                utility.logger("Error" + t.toString());
            }
        });
        return cityMutableLiveData;
    }

    public LiveData<List<Area>> getAreaList(City city) {
        Call<ApiResponse> call = apiInterface.getArea(utility.getAuthorizationKey(),
                KeyWord.USERID, KeyWord.TOKEN, utility.getLangauge(), city);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type listType = new TypeToken<List<Area>>() {
                        }.getType();
                        areaList = gson.fromJson(apiResponse.getData(), listType);
                        areaMutableLiveData.postValue(areaList);
                    } else {
                        areaMutableLiveData.postValue(areaList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return areaMutableLiveData;
    }
}
