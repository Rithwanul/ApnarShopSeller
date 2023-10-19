package com.bikroybaba.seller.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bikroybaba.seller.model.remote.request.Category;
import com.bikroybaba.seller.model.remote.request.ProductType;
import com.bikroybaba.seller.model.remote.request.ReportRequest;
import com.bikroybaba.seller.model.remote.response.ProductName;
import com.bikroybaba.seller.model.remote.response.ReportResponse;
import com.bikroybaba.seller.repository.ReportRepo;

import java.util.List;


public class ReportViewModel extends AndroidViewModel {

    private final ReportRepo reportRepo;

    public ReportViewModel(@NonNull Application application) {
        super(application);
        reportRepo = new ReportRepo(application);
    }

    public MutableLiveData<List<ProductType>> getProductTypes(Category category) {

        return reportRepo.getProductTypes(category);
    }

    public MutableLiveData<List<ProductName>> getProduct(ProductType productType) {

        return reportRepo.getProduct(productType);
    }

    public MutableLiveData<List<Category>> getProductCategory() {
        return reportRepo.getProductCategory();
    }

    public void getReport(ReportRequest reportRequest) {

        reportRepo.getReport(reportRequest);
    }

    public MutableLiveData<ReportResponse> getReportResponse() {

        return reportRepo.getReportResponse();
    }
}
