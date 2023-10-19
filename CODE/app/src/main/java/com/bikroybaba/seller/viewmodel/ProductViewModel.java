package com.bikroybaba.seller.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.bikroybaba.seller.data.factory.ProductListDataSourceFactory;
import com.bikroybaba.seller.data.source.ProductListDataSource;
import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.entity.AllProduct;
import com.bikroybaba.seller.model.entity.Brand;
import com.bikroybaba.seller.model.remote.request.Category;
import com.bikroybaba.seller.model.remote.request.FilterProduct;
import com.bikroybaba.seller.model.remote.request.Product;
import com.bikroybaba.seller.model.remote.request.ProductQuantity;
import com.bikroybaba.seller.model.remote.request.ProductStatus;
import com.bikroybaba.seller.model.remote.request.ProductType;
import com.bikroybaba.seller.model.remote.response.AttributeResponse;
import com.bikroybaba.seller.model.remote.response.ProductDetails;
import com.bikroybaba.seller.model.remote.response.Supplier;
import com.bikroybaba.seller.model.remote.response.UnitType;
import com.bikroybaba.seller.repository.AttributeRepo;
import com.bikroybaba.seller.repository.ProductRepo;

import java.util.List;

import okhttp3.RequestBody;

public class ProductViewModel extends AndroidViewModel {

    private final ProductRepo productRepo;
    private final Application application;
    private MutableLiveData<String> error;
    private LiveData<PageKeyedDataSource<Integer, AllProduct>> productMutableLiveData;
    public LiveData<PageKeyedDataSource<Integer, AllProduct>> dataSourceLiveData;
    private final AttributeRepo attributeRepo;


    public ProductViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        productRepo = new ProductRepo(application);
        attributeRepo = new AttributeRepo(application);
    }

    public LiveData<AttributeResponse> getAttributeLivedata(String attributeName) {
        return attributeRepo.getAttributes(attributeName);
    }

    public LiveData<AttributeResponse> getAttributeDataNameLiveData(String attributeDataName, int attributeId){
        return attributeRepo.getAttributesDataName(attributeDataName, attributeId);
    }

    public void sendProductDetailsRequest(Product product) {
        productRepo.sendProductDetailsRequest(product);
    }

    public LiveData<ProductDetails> getProductDetails() {
        return productRepo.getProductDetails();
    }

    public void updateProductStatus(ProductStatus productStatus) {

        productRepo.updateProductStatus(productStatus);
    }

    public LiveData<ApiResponse> getProductStatusResponse() {

        return productRepo.getProductStatusResponse();
    }

    public void searchCategory(String title) {
        productRepo.searchProductCategory(title);
    }

    public LiveData<List<Category>> getFilteredCategory() {

        return productRepo.getFilteredCategory();
    }

    public void searchSupplier(String title) {
        productRepo.searchSupplier(title);
    }

    public LiveData<List<Supplier>> getFilteredSupplierList() {

        return productRepo.getFilteredSupplierList();
    }

    public void searchProductType(String title, Category category) {
        productRepo.searchProductType(title, category);
    }

    public LiveData<List<ProductType>> getFilteredProductTypeList() {
        return productRepo.getFilteredProductTypeList();
    }

    public MutableLiveData<List<UnitType>> getUnitType() {

        return productRepo.getUnitList();
    }

    public void searchBrand(String title, ProductType productType) {
        productRepo.searchBrand(title, productType);
    }

    public LiveData<List<Brand>> getFilteredBandList() {

        return productRepo.getFilteredBrandList();
    }

    public MutableLiveData<ApiResponse> createProduct(RequestBody requestBody) {

        return productRepo.createProduct(requestBody);
    }
    public MutableLiveData<Boolean> getHandleError(){
        return productRepo.getHandleError();
    }
    public void updateProduct(RequestBody requestBody) {

        productRepo.updateProduct(requestBody);
    }

    public MutableLiveData<ApiResponse> getProductUpdateResponse() {

        return productRepo.getProductUpdateResponse();
    }


    public LiveData<PagedList<AllProduct>> getProductList(FilterProduct filterProduct, EmptyListInterface emptyListInterface){
        ProductListDataSourceFactory factory = new ProductListDataSourceFactory(application,filterProduct,emptyListInterface);
        error = factory.getError();
        dataSourceLiveData = factory.getItemLiveDataSource();
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(ProductListDataSource.PAGE_SIZE).build();
        return (new LivePagedListBuilder(factory, pagedListConfig)).build();
    }


    public MutableLiveData<ApiResponse> updateProductQuantity(ProductQuantity productQuantity) {
        return productRepo.updateProductQuantity(productQuantity);
    }

    public MutableLiveData<List<Category>> getProductCategory() {
        return productRepo.getProductCategory();
    }
    public MutableLiveData<String> getError(){
        return error;
    }

    public LiveData<ApiResponse> deleteAttribute(long productAttributeId){
        return productRepo.deleteProductAttribute(productAttributeId);
    }
}
