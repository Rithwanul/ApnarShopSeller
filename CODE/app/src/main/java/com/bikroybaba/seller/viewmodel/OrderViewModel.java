package com.bikroybaba.seller.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.bikroybaba.seller.data.factory.OrderDataSourceFactory;
import com.bikroybaba.seller.data.source.OrderDataSource;
import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.remote.request.OrderDetailsRequest;
import com.bikroybaba.seller.model.remote.request.OrderListRequest;
import com.bikroybaba.seller.model.remote.request.OrderStatusRequest;
import com.bikroybaba.seller.model.remote.response.OrderDetailsResponse;
import com.bikroybaba.seller.model.remote.response.OrderListResponse;
import com.bikroybaba.seller.repository.OrderRepo;

/**
 * Created by Anik Roy on 17,November,2020
 */

public class OrderViewModel extends AndroidViewModel {

    private final OrderRepo orderRepo;
    private final Application application;
    public LiveData<PageKeyedDataSource<Integer, OrderListResponse>> dataSourceLiveData;
    private MutableLiveData<String> error;


    public OrderViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        orderRepo = new OrderRepo(application);
    }

    public LiveData<PagedList<OrderListResponse>> getOrderList(OrderListRequest orderListRequest, EmptyListInterface emptyListInterface){
        OrderDataSourceFactory factory = new OrderDataSourceFactory(application,orderListRequest, emptyListInterface);
        error = factory.getError();
        dataSourceLiveData = factory.getItemLiveDataSource();
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(OrderDataSource.PAGE_SIZE).build();
        return (new LivePagedListBuilder(factory, pagedListConfig)).build();
    }


    public MutableLiveData<String> getOrderListError(){
        return error;
    }


    public void updateOrderStatus(OrderStatusRequest orderStatusRequest) {

        orderRepo.updateOrderStatus(orderStatusRequest);
    }

    public MutableLiveData<ApiResponse> getOrderStatusResponse() {
        return orderRepo.getOrderStatusResponse();
    }
    public MutableLiveData<OrderDetailsResponse> getOrderDetails(OrderDetailsRequest orderDetailsRequest){
        return orderRepo.getOrderDetails(orderDetailsRequest);
    }
}
