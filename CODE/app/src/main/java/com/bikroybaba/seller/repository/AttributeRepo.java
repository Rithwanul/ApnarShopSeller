package com.bikroybaba.seller.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bikroybaba.seller.http.ApiClient;
import com.bikroybaba.seller.http.ApiInterface;
import com.bikroybaba.seller.model.remote.response.AttributeResponse;
import com.bikroybaba.seller.util.Utility;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttributeRepo {

    private final MutableLiveData<AttributeResponse> attributeMutableLiveData;
    private final MutableLiveData<AttributeResponse> attributeDataNameMutableLiveData;
    private final Utility utility;
    private final Gson gson;
    private final ApiInterface apiInterface;

    public AttributeRepo(Application application) {
        attributeMutableLiveData = new MutableLiveData<>();
        attributeDataNameMutableLiveData = new MutableLiveData<>();
        utility = new Utility(application);
        gson = new Gson();
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    }

    public LiveData<AttributeResponse> getAttributes(String attributeName){

        Call<AttributeResponse> call = apiInterface.getAttributes(attributeName, utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge());

        call.enqueue(new Callback<AttributeResponse>() {
            @Override
            public void onResponse(Call<AttributeResponse> call, Response<AttributeResponse> response) {
                if (response.isSuccessful()){
                    attributeMutableLiveData.postValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<AttributeResponse> call, Throwable t) {
                Log.d("Data", "Error: -> " + t.getMessage());
            }
        });

        return attributeMutableLiveData;
    }

    public LiveData<AttributeResponse> getAttributesDataName(String attributeDataName, int attributeId){

        Call<AttributeResponse> call = apiInterface.getAllAttributeData(attributeDataName, attributeId, utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge());

        call.enqueue(new Callback<AttributeResponse>() {
            @Override
            public void onResponse(Call<AttributeResponse> call, Response<AttributeResponse> response) {
                Log.d("Data", "onResponse: -> " + response.isSuccessful());
                if (response.isSuccessful()){
                    attributeDataNameMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AttributeResponse> call, Throwable t) {
                Log.d("Data", "Error: -> " + t.getMessage());
            }
        });

        return attributeDataNameMutableLiveData;
    }

    public MutableLiveData<AttributeResponse> getAttributeMutableLiveData() {
        return attributeMutableLiveData;
    }

    public MutableLiveData<AttributeResponse> getAttributeDataNameMutableLiveData() {
        return attributeDataNameMutableLiveData;
    }
}
