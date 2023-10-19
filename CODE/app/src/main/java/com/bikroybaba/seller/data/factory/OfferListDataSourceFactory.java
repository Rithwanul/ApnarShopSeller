package com.bikroybaba.seller.data.factory;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.bikroybaba.seller.data.source.OfferListDataSource;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.remote.request.OfferListRequest;
import com.bikroybaba.seller.model.remote.response.OfferDetailsResponse;


public class OfferListDataSourceFactory extends DataSource.Factory {

    private final Context context;
    private final OfferListRequest offerListRequest;
    private final EmptyListInterface emptyListInterface;

    MutableLiveData<PageKeyedDataSource<Integer, OfferDetailsResponse>> offerDetailsMutableLiveData =
            new MutableLiveData<>();
    MutableLiveData<String> errorMessage= new MutableLiveData<>();
    public OfferListDataSourceFactory(Context context, OfferListRequest offerListRequest,
                                      EmptyListInterface emptyListInterface) {
        this.context = context;
        this.offerListRequest = offerListRequest;
        this.emptyListInterface = emptyListInterface;
    }

    @Override
    public DataSource create() {
        OfferListDataSource dataSource = new OfferListDataSource(context, offerListRequest,emptyListInterface);
        errorMessage= dataSource.getError();
        offerDetailsMutableLiveData.postValue(dataSource);
        return dataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, OfferDetailsResponse>> getItemLiveDataSource() {
        return offerDetailsMutableLiveData;
    }

    public MutableLiveData<String> getError(){

        return errorMessage;
    }
}
