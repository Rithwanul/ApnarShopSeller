package com.bikroybaba.seller.data.factory;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.bikroybaba.seller.data.source.OfferProductListDataSource;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.remote.request.OfferProductListRequest;
import com.bikroybaba.seller.model.remote.response.OfferProductListResponse;

public class OfferProductListDataSourceFactory extends DataSource.Factory {

    private final Context context;
    private final OfferProductListRequest offerProductListRequest;
    private final EmptyListInterface emptyListInterface;

    MutableLiveData<String> errorMessage = new MutableLiveData<>();
    MutableLiveData<PageKeyedDataSource<Integer, OfferProductListResponse>> offerProductMutableLiveData =
            new MutableLiveData<>();
    public OfferProductListDataSourceFactory(Context context,
                                             OfferProductListRequest offerProductListRequest,
                                             EmptyListInterface emptyListInterface) {
        this.context = context;
        this.offerProductListRequest = offerProductListRequest;
        this.emptyListInterface = emptyListInterface;
    }

    @Override
    public DataSource create() {
        OfferProductListDataSource dataSource =
                new OfferProductListDataSource(context, offerProductListRequest, emptyListInterface);
        errorMessage = dataSource.getError();
        offerProductMutableLiveData.postValue(dataSource);
        return dataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, OfferProductListResponse>> getItemLiveDataSource() {
        return offerProductMutableLiveData;
    }

    public MutableLiveData<String> getError() {

        return errorMessage;
    }
}
