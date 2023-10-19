package com.bikroybaba.seller.data.factory;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.bikroybaba.seller.data.source.ProductListDataSource;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.entity.AllProduct;
import com.bikroybaba.seller.model.remote.request.FilterProduct;

public class ProductListDataSourceFactory extends DataSource.Factory {

    private final Context context;
    private final FilterProduct filterProduct;
    private final EmptyListInterface emptyListInterface;

    MutableLiveData<PageKeyedDataSource<Integer, AllProduct>> offerProductMutableLiveData =
            new MutableLiveData<>();
    MutableLiveData<String> errorMessage= new MutableLiveData<>();

    public ProductListDataSourceFactory(Context context, FilterProduct filterProduct,
                                        EmptyListInterface emptyListInterface) {
        this.context = context;
        this.filterProduct = filterProduct;
        this.emptyListInterface = emptyListInterface;
    }

    @Override
    public DataSource create() {
        ProductListDataSource dataSource =
                new ProductListDataSource(context, filterProduct,emptyListInterface);
        errorMessage= dataSource.getError();
        offerProductMutableLiveData.postValue(dataSource);
        return dataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, AllProduct>> getItemLiveDataSource() {
        return offerProductMutableLiveData;
    }

    public MutableLiveData<String> getError(){

        return errorMessage;
    }
}
