package com.bikroybaba.seller.data.factory;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.bikroybaba.seller.data.source.OrderDataSource;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.remote.request.OrderListRequest;
import com.bikroybaba.seller.model.remote.response.OrderListResponse;


public class OrderDataSourceFactory extends DataSource.Factory {

    private final Context context;
    private final OrderListRequest orderListRequest;
    EmptyListInterface emptyListInterface;

    MutableLiveData<PageKeyedDataSource<Integer, OrderListResponse>> orderMutableLiveData =
            new MutableLiveData<>();
    MutableLiveData<String> errorMessage= new MutableLiveData<>();

    public OrderDataSourceFactory(Context context, OrderListRequest orderListRequest,
                                  EmptyListInterface emptyListInterface) {
        this.context = context;
        this.orderListRequest = orderListRequest;
        this.emptyListInterface = emptyListInterface;
    }

    @Override
    public DataSource create() {
        OrderDataSource dataSource =
                new OrderDataSource(context, orderListRequest, emptyListInterface);
        errorMessage= dataSource.getError();
        orderMutableLiveData.postValue(dataSource);
        return dataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, OrderListResponse>> getItemLiveDataSource() {
        return orderMutableLiveData;
    }

    public MutableLiveData<String> getError(){

        return errorMessage;
    }
}
