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
import com.bikroybaba.seller.model.remote.request.City;
import com.bikroybaba.seller.model.remote.request.District;
import com.bikroybaba.seller.model.remote.request.Division;
import com.bikroybaba.seller.model.remote.request.Registration;
import com.bikroybaba.seller.model.remote.response.ShopCategory;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpRepo {

    private final MutableLiveData<List<ShopCategory>> shopCategoryMutableLiveData;
    private final MutableLiveData<List<Division>> divisionMutableLiveData;
    private final MutableLiveData<List<District>> districtMutableLiveData;
    private final MutableLiveData<List<City>> cityMutableLiveData;
    private final MutableLiveData<List<Area>> areaMutableLiveData;
    private final ApiInterface apiInterface;
    private final Utility utility;
    private final Gson gson;
    private List<ShopCategory> shopCategoryList;
    private List<Division> divisionList;
    private List<District> districtList;
    private List<City> cityList;
    private List<Area> areaList;
    private final MutableLiveData<ApiResponse> registrationMutableLiveData;


    public SignUpRepo(Application application) {
        shopCategoryMutableLiveData = new MutableLiveData<>();
        divisionMutableLiveData = new MutableLiveData<>();
        districtMutableLiveData = new MutableLiveData<>();
        cityMutableLiveData = new MutableLiveData<>();
        areaMutableLiveData = new MutableLiveData<>();
        registrationMutableLiveData = new MutableLiveData<>();
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        utility = new Utility(application);
        gson = new Gson();
    }

    public LiveData<List<ShopCategory>> getShopCategoryList() {
        Call<ApiResponse> call = apiInterface.getShopCategory(utility.getAuthorizationKey(),
                KeyWord.USERID, KeyWord.TOKEN, utility.getLangauge());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type listType = new TypeToken<List<ShopCategory>>() {
                        }.getType();
                        shopCategoryList = gson.fromJson(apiResponse.getData(), listType);
                        shopCategoryMutableLiveData.postValue(shopCategoryList);
                    } else {
                        shopCategoryMutableLiveData.postValue(shopCategoryList);
                        utility.logger("Get Shop Category" + shopCategoryList.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
        return shopCategoryMutableLiveData;
    }

    public LiveData<List<Division>> getDivisionList() {
        Call<ApiResponse> call =
                apiInterface.getDivision(utility.getAuthorizationKey(), KeyWord.USERID,
                        KeyWord.TOKEN, utility.getLangauge());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                try {
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
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
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
                        utility.logger("Get Division" + districtList.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                utility.logger("Error" + t.toString());
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
                utility.logger("Error" + t.toString());
            }
        });

        return areaMutableLiveData;
    }

    public void sendRegistration(Registration registration) {

        utility.logger("Registration" + registration.toString());
        Call<ApiResponse> call = apiInterface.registration(utility.getAuthorizationKey(),
                KeyWord.USERID, KeyWord.TOKEN, utility.getLangauge(), registration);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    assert apiResponse != null;
                    utility.logger("Registration" + apiResponse.toString());
                    registrationMutableLiveData.postValue(apiResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public LiveData<ApiResponse> getRegistrationResponse() {
        return registrationMutableLiveData;
    }
}
