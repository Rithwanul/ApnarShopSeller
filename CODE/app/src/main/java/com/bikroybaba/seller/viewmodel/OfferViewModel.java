package com.bikroybaba.seller.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.bikroybaba.seller.data.factory.OfferListDataSourceFactory;
import com.bikroybaba.seller.data.factory.OfferProductListDataSourceFactory;
import com.bikroybaba.seller.data.source.OfferListDataSource;
import com.bikroybaba.seller.data.source.OrderDataSource;
import com.bikroybaba.seller.database.table.UserProfile;
import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.remote.request.AddOfferProduct;
import com.bikroybaba.seller.model.remote.request.Category;
import com.bikroybaba.seller.model.remote.request.OfferCategoryRequest;
import com.bikroybaba.seller.model.remote.request.OfferCreate;
import com.bikroybaba.seller.model.remote.request.OfferDetailsRequest;
import com.bikroybaba.seller.model.remote.request.OfferListRequest;
import com.bikroybaba.seller.model.remote.request.OfferProductListRequest;
import com.bikroybaba.seller.model.remote.request.OfferStatus;
import com.bikroybaba.seller.model.remote.request.OfferUpdate;
import com.bikroybaba.seller.model.remote.response.OfferCategory;
import com.bikroybaba.seller.model.remote.response.OfferDetailsResponse;
import com.bikroybaba.seller.model.remote.response.OfferListResponse;
import com.bikroybaba.seller.model.remote.response.OfferProductListResponse;
import com.bikroybaba.seller.model.remote.response.OfferTypeResponse;
import com.bikroybaba.seller.repository.OfferRepo;

import java.util.List;

public class OfferViewModel extends AndroidViewModel {

    private final OfferRepo offerRepo;
    private final Application application;
    public LiveData<PageKeyedDataSource<Integer, OfferProductListResponse>> dataSourceLiveData;
    public LiveData<PageKeyedDataSource<Integer, OfferDetailsResponse>> offerDetailsLiveData;
    private MutableLiveData<String> error;

    public OfferViewModel(@NonNull Application application) {
        super(application);
        offerRepo = new OfferRepo(application);
        this.application = application;
    }

    public MutableLiveData<List<OfferTypeResponse>> getOfferTypeList() {
        return offerRepo.getOfferTypeList();
    }

    public MutableLiveData<List<OfferListResponse>> getOfferList(OfferListRequest offerListRequest) {

        return offerRepo.getOfferList(offerListRequest);
    }

    public MutableLiveData<List<OfferCategory>> getOfferCategory(OfferCategoryRequest offerCategoryRequest) {

        return offerRepo.getOfferCategory(offerCategoryRequest);
    }

    public void sendOfferDetailsRequest(OfferDetailsRequest offerDetailsRequest) {
        offerRepo.sendOfferDetailsRequest(offerDetailsRequest);
    }

    public MutableLiveData<OfferDetailsResponse> getOfferDetailsResponse() {
        return offerRepo.getOfferDetailsResponse();
    }

    public void updateOfferStatus(OfferStatus offerStatus) {

        offerRepo.updateOfferStatus(offerStatus);
    }

    public void createOffer(OfferCreate offerCreate) {
        offerRepo.createOffer(offerCreate);
    }

    public void updateOffer(OfferUpdate offerUpdate) {
        offerRepo.updateOffer(offerUpdate);
    }

    public LiveData<PagedList<OfferProductListResponse>> getOfferProductList(
            OfferProductListRequest offerProductListRequest,
            EmptyListInterface emptyListInterface
    ) {
        OfferProductListDataSourceFactory factory =
                new OfferProductListDataSourceFactory(application, offerProductListRequest, emptyListInterface);
        error = factory.getError();
        dataSourceLiveData = factory.getItemLiveDataSource();
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(OrderDataSource.PAGE_SIZE).build();
        return (new LivePagedListBuilder(factory, pagedListConfig)).build();
    }

    public LiveData<PagedList<OfferDetailsResponse>> getOfferDetailsList(
            OfferListRequest offerListRequest, EmptyListInterface emptyListInterface
    ) {
        OfferListDataSourceFactory factory =
                new OfferListDataSourceFactory(application, offerListRequest, emptyListInterface);
        error = factory.getError();
        offerDetailsLiveData = factory.getItemLiveDataSource();
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(OfferListDataSource.PAGE_SIZE).build();
        return (new LivePagedListBuilder(factory, pagedListConfig)).build();
    }

    public void addOfferProduct(AddOfferProduct offerProduct) {
        offerRepo.addOfferProduct(offerProduct);
    }

    public MutableLiveData<ApiResponse> getAPIResponse() {
        return offerRepo.getAPIResponse();
    }

    public MutableLiveData<ApiResponse> getUpdateOfferResponse() {
        return offerRepo.getUpdateResponse();
    }

    public MutableLiveData<ApiResponse> createOfferResponse() {

        return offerRepo.createOfferResponse();
    }

    public MutableLiveData<ApiResponse> statusUpdateResponse() {

        return offerRepo.statusUpdateResponse();
    }

    public LiveData<UserProfile> getUserLivedata() {
        return offerRepo.getUserdata();
    }
    public MutableLiveData<List<Category>> getProductCategory() {
        return offerRepo.getProductCategory();
    }
}
