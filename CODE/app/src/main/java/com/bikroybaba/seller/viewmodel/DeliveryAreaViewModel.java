package com.bikroybaba.seller.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.model.entity.Area;
import com.bikroybaba.seller.model.entity.Delivery;
import com.bikroybaba.seller.model.remote.request.City;
import com.bikroybaba.seller.model.remote.request.District;
import com.bikroybaba.seller.model.remote.request.Division;
import com.bikroybaba.seller.model.remote.request.UpdateDeliveryArea;
import com.bikroybaba.seller.repository.DeliveryAreaRepo;

import java.util.List;

public class DeliveryAreaViewModel extends AndroidViewModel {

    private final DeliveryAreaRepo deliveryAreaRepo;

    public DeliveryAreaViewModel(@NonNull Application application) {
        super(application);
        deliveryAreaRepo = new DeliveryAreaRepo(application);
    }

    public LiveData<Delivery> getDeliveryArea() {

        return deliveryAreaRepo.getDeliveryArea();
    }

    public void updateDeliveryArea(UpdateDeliveryArea updateDeliveryArea) {
        deliveryAreaRepo.updateDeliveryArea(updateDeliveryArea);
    }

    public LiveData<ApiResponse> getUpdateDeliveryAreaResponse() {
        return deliveryAreaRepo.getUpdateDeliveryAreaResponse();
    }

    public LiveData<Boolean> error() {
        return deliveryAreaRepo.errorDeliveryArea();
    }

    public LiveData<List<Division>> getDivisionList() {

        return deliveryAreaRepo.getDivisionList();
    }

    public LiveData<List<District>> getDistrictList(Division division) {

        return deliveryAreaRepo.getDistrictList(division);
    }

    public LiveData<List<City>> getCityList(District district) {
        return deliveryAreaRepo.getCityList(district);
    }

    public LiveData<List<Area>> getAreaList(City city) {
        return deliveryAreaRepo.getAreaList(city);
    }

}
