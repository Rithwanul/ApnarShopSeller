package com.bikroybaba.seller.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.bikroybaba.seller.database.table.UserProfile;
import com.bikroybaba.seller.model.remote.response.AttributeResponse;
import com.bikroybaba.seller.model.remote.response.ProductCount;
import com.bikroybaba.seller.repository.AttributeRepo;
import com.bikroybaba.seller.repository.DashboardRepo;
import com.moktar.zmvvm.base.base.BaseViewModel;

public class DashboardViewModel extends BaseViewModel {

    private final DashboardRepo dashboardRepo;
    private final AttributeRepo attributeRepo;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        dashboardRepo= new DashboardRepo(application);
        attributeRepo = new AttributeRepo(application);
    }

    public LiveData<ProductCount> getProductCount() {
        return dashboardRepo.getProductCount();
    }

    public LiveData<UserProfile> getUserLivedata() {
        return dashboardRepo.getUserdata();
    }

    public LiveData<AttributeResponse> getAttributeLivedata(String attributeName) {
        return attributeRepo.getAttributes(attributeName);
    }

    public LiveData<AttributeResponse> getAttributeDataNameLiveData(String attributeDataName, int attributeId){
        return attributeRepo.getAttributesDataName(attributeDataName, attributeId);
    }
}