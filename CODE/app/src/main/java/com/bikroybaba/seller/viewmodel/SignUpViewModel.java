package com.bikroybaba.seller.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.model.entity.Area;
import com.bikroybaba.seller.model.remote.request.City;
import com.bikroybaba.seller.model.remote.request.District;
import com.bikroybaba.seller.model.remote.request.Division;
import com.bikroybaba.seller.model.remote.request.Registration;
import com.bikroybaba.seller.model.remote.response.ShopCategory;
import com.bikroybaba.seller.repository.SignUpRepo;

import java.util.List;

public class SignUpViewModel extends AndroidViewModel {

    private final SignUpRepo signUpRepo;

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        signUpRepo = new SignUpRepo(application);
    }

    public LiveData<List<ShopCategory>> getShopCategoryList(){

        return signUpRepo.getShopCategoryList();
    }

    public LiveData<List<Division>> getDivisionList(){

        return signUpRepo.getDivisionList();
    }
    public LiveData<List<District>> getDistrictList(Division division){

        return signUpRepo.getDistrictList(division);
    }
    public LiveData<List<City>> getCityList(District district){
        return signUpRepo.getCityList(district);
    }
    public LiveData<List<Area>> getAreaList(City city){
        return signUpRepo.getAreaList(city);
    }

    public void sendRegistration(Registration registration){
        signUpRepo.sendRegistration(registration);
    }

    public LiveData<ApiResponse> getRegistrationResponse(){
        return signUpRepo.getRegistrationResponse();
    }
}
