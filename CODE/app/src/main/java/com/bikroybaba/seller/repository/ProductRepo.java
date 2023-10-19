package com.bikroybaba.seller.repository;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bikroybaba.seller.http.ApiClient;
import com.bikroybaba.seller.http.ApiInterface;
import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.model.entity.Brand;
import com.bikroybaba.seller.model.remote.request.Category;
import com.bikroybaba.seller.model.remote.request.Product;
import com.bikroybaba.seller.model.remote.request.ProductQuantity;
import com.bikroybaba.seller.model.remote.request.ProductStatus;
import com.bikroybaba.seller.model.remote.request.ProductType;
import com.bikroybaba.seller.model.remote.response.ProductDetails;
import com.bikroybaba.seller.model.remote.response.Supplier;
import com.bikroybaba.seller.model.remote.response.UnitType;
import com.bikroybaba.seller.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepo {
    private final MutableLiveData<ProductDetails> productDetailsMutableLiveData;
    private final MutableLiveData<ApiResponse> apiResponseMutableLiveData;
    private final MutableLiveData<List<Category>> categoryMutableLiveData;
    private final MutableLiveData<List<Supplier>> supplierMutableLiveData;
    private final MutableLiveData<List<ProductType>> productTypeMutableLiveData;
    private final MutableLiveData<List<UnitType>> unitTypeMutableLiveData;
    private final MutableLiveData<List<Brand>> brandMutableLiveData;
    private final MutableLiveData<ApiResponse> updateProductStatusMutableLiveData;
    private final MutableLiveData<Boolean> handleError;
    private final MutableLiveData<ApiResponse> deleteProductAttribute;
    private final Utility utility;
    private final ApiInterface apiInterface;
    private final Gson gson;

    public ProductRepo(Application application) {
        productDetailsMutableLiveData = new MutableLiveData<>();
        unitTypeMutableLiveData = new MutableLiveData<>();
        apiResponseMutableLiveData = new MutableLiveData<>();
        categoryMutableLiveData = new MutableLiveData<>();
        supplierMutableLiveData = new MutableLiveData<>();
        productTypeMutableLiveData = new MutableLiveData<>();
        brandMutableLiveData = new MutableLiveData<>();
        updateProductStatusMutableLiveData = new MutableLiveData<>();
        deleteProductAttribute = new MutableLiveData<>();
        handleError = new MutableLiveData<>();
        utility = new Utility(application);
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        gson = new Gson();
    }

    public void sendProductDetailsRequest(Product product) {
        Call<ApiResponse> call = apiInterface.getProductDetails(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), product);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        productDetailsMutableLiveData
                                .postValue(gson.fromJson(apiResponse.getData(), ProductDetails.class));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<ProductDetails> getProductDetails() {
        return productDetailsMutableLiveData;
    }

    public void updateProductStatus(ProductStatus productStatus) {
        Call<ApiResponse> call = apiInterface.updateProductStatus(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), productStatus);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    updateProductStatusMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<ApiResponse> getProductStatusResponse() {

        return updateProductStatusMutableLiveData;
    }

    public void searchProductCategory(String title) {
        Call<ApiResponse> call = apiInterface.filterCategory(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), title);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type listType = new TypeToken<List<Category>>() {
                        }.getType();
                        List<Category> category = gson.fromJson(apiResponse.getData(), listType);
                        categoryMutableLiveData.postValue(category);
                        utility.logger("List" + category.toString());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<List<Category>> getFilteredCategory() {

        return categoryMutableLiveData;
    }

    public void searchSupplier(String title) {
        Call<ApiResponse> call = apiInterface.filterSupplier(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), title);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type listType = new TypeToken<List<Supplier>>() {
                        }.getType();
                        List<Supplier> supplierList = gson.fromJson(apiResponse.getData(), listType);
                        supplierMutableLiveData.postValue(supplierList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<List<Supplier>> getFilteredSupplierList() {

        return supplierMutableLiveData;
    }

    public void searchProductType(String title, Category category) {
        Call<ApiResponse> call = apiInterface.filterProductType(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), title, category);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type listType = new TypeToken<List<ProductType>>() {
                        }.getType();
                        List<ProductType> productTypeList =
                                gson.fromJson(apiResponse.getData(), listType);
                        productTypeMutableLiveData.postValue(productTypeList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<List<ProductType>> getFilteredProductTypeList() {

        return productTypeMutableLiveData;
    }

    public void searchBrand(String title, ProductType productType) {
        Call<ApiResponse> call = apiInterface.filterBrand(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), title, productType);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type listType = new TypeToken<List<Brand>>() {
                        }.getType();
                        List<Brand> brandList = gson.fromJson(apiResponse.getData(), listType);
                        brandMutableLiveData.postValue(brandList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<List<Brand>> getFilteredBrandList() {

        return brandMutableLiveData;
    }


    public MutableLiveData<List<UnitType>> getUnitList() {
        Call<ApiResponse> call = apiInterface.getUnitList(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type listType = new TypeToken<List<UnitType>>() {
                        }.getType();
                        List<UnitType> unitTypeList = gson.fromJson(apiResponse.getData(), listType);
                        unitTypeMutableLiveData.postValue(unitTypeList);

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

        return unitTypeMutableLiveData;
    }

    public MutableLiveData<ApiResponse> createProduct(RequestBody requestBody) {
        Call<ApiResponse> call = apiInterface.createProduct(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), requestBody);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    apiResponseMutableLiveData.postValue(apiResponse);
                } else {
                    handleError.postValue(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return apiResponseMutableLiveData;
    }

    public MutableLiveData<Boolean> getHandleError() {

        return handleError;
    }

    public void updateProduct(RequestBody requestBody) {
        Call<ApiResponse> call = apiInterface.updateProduct(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), requestBody);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    apiResponseMutableLiveData.postValue(apiResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public MutableLiveData<ApiResponse> getProductUpdateResponse() {

        return apiResponseMutableLiveData;
    }

    public MutableLiveData<ApiResponse> updateProductQuantity(ProductQuantity productQuantity) {
        Call<ApiResponse> call = apiInterface.updateProductQuantity(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), productQuantity);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    apiResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

        return apiResponseMutableLiveData;
    }

    public MutableLiveData<List<Category>> getProductCategory() {
        Call<ApiResponse> call = apiInterface.getProductCategory(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 200) {
                        Type listType = new TypeToken<List<Category>>() {
                        }.getType();
                        List<Category> categoryList = gson.fromJson(apiResponse.getData(), listType);
                        categoryMutableLiveData.postValue(categoryList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return categoryMutableLiveData;
    }

    public MutableLiveData<ApiResponse> deleteProductAttribute(long productAttributeId){
        Call<ApiResponse> call = apiInterface.deleteAttributeData(utility.getAuthorizationKey(),
                utility.getUserId(), utility.getFirebaseToken(), utility.getLangauge(), productAttributeId);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    ApiResponse body = response.body();
                    deleteProductAttribute.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d(TAG, "Error : " + t.getMessage());
            }
        });

        return deleteProductAttribute;
    }
}
