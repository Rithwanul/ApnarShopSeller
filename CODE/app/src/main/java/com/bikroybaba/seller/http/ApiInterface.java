package com.bikroybaba.seller.http;

import com.bikroybaba.seller.model.remote.request.AddOfferProduct;
import com.bikroybaba.seller.model.remote.request.Category;
import com.bikroybaba.seller.model.remote.request.ChangePassword;
import com.bikroybaba.seller.model.remote.request.City;
import com.bikroybaba.seller.model.remote.request.District;
import com.bikroybaba.seller.model.remote.request.Division;
import com.bikroybaba.seller.model.remote.request.FilterProduct;
import com.bikroybaba.seller.model.remote.request.LogIn;
import com.bikroybaba.seller.model.remote.request.OfferCategoryRequest;
import com.bikroybaba.seller.model.remote.request.OfferCreate;
import com.bikroybaba.seller.model.remote.request.OfferDetailsRequest;
import com.bikroybaba.seller.model.remote.request.OfferListRequest;
import com.bikroybaba.seller.model.remote.request.OfferProductListRequest;
import com.bikroybaba.seller.model.remote.request.OfferStatus;
import com.bikroybaba.seller.model.remote.request.OfferUpdate;
import com.bikroybaba.seller.model.remote.request.OrderDetailsRequest;
import com.bikroybaba.seller.model.remote.request.OrderListRequest;
import com.bikroybaba.seller.model.remote.request.OrderStatusRequest;
import com.bikroybaba.seller.model.remote.request.Product;
import com.bikroybaba.seller.model.remote.request.ProductQuantity;
import com.bikroybaba.seller.model.remote.request.ProductStatus;
import com.bikroybaba.seller.model.remote.request.ProductType;
import com.bikroybaba.seller.model.remote.request.Registration;
import com.bikroybaba.seller.model.remote.request.ReportRequest;
import com.bikroybaba.seller.model.remote.request.Settings;
import com.bikroybaba.seller.model.remote.request.UpdateDeliveryArea;
import com.bikroybaba.seller.model.remote.request.UpdateNotificationRequest;
import com.bikroybaba.seller.model.remote.response.AttributeResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    // app configuration
    @POST("seller/config")
    Call<ApiResponse> getConfig(@Header("Authorization") String apiKey,
                                @Header("userId") String usersId,
                                @Header("token") String token,
                                @Header("Language") String language);

    // shop category
    @POST("seller/shop/category")
    Call<ApiResponse> getShopCategory(@Header("Authorization") String apiKey,
                                      @Header("userId") String usersId,
                                      @Header("token") String token,
                                      @Header("Language") String language);

    // division list
    @POST("general/address/division/list")
    Call<ApiResponse> getDivision(@Header("Authorization") String apiKey,
                                  @Header("userId") String usersId,
                                  @Header("token") String token,
                                  @Header("Language") String language);

    // district list
    @POST("general/address/district/list")
    Call<ApiResponse> getDistrict(@Header("Authorization") String apiKey,
                                  @Header("userId") String usersId,
                                  @Header("token") String token,
                                  @Header("Language") String language,
                                  @Body Division division);

    // city list
    @POST("general/address/city/list")
    Call<ApiResponse> getCity(@Header("Authorization") String apiKey,
                              @Header("userId") String usersId,
                              @Header("token") String token,
                              @Header("Language") String language,
                              @Body District district);

    // area list
    @POST("general/address/area/list")
    Call<ApiResponse> getArea(@Header("Authorization") String apiKey,
                              @Header("userId") String usersId,
                              @Header("token") String token,
                              @Header("Language") String language,
                              @Body City city);

    // registration
    @POST("seller/registration")
    Call<ApiResponse> registration(@Header("Authorization") String apiKey,
                                   @Header("userId") String usersId,
                                   @Header("token") String token,
                                   @Header("Language") String language,
                                   @Body Registration registration);

    //  sign in
    @POST("seller/login")
    Call<ApiResponse> login(@Header("Authorization") String apiKey,
                            @Header("userId") String usersId,
                            @Header("token") String token,
                            @Header("Language") String language,
                            @Body LogIn logIn);

    // dashboard
    @POST("seller/dashboard")
    Call<ApiResponse> getDashboardInfo(@Header("Authorization") String apiKey,
                                       @Header("userId") String usersId,
                                       @Header("token") String token,
                                       @Header("Language") String language);

    // rating
    @POST("seller/rating")
    Call<ApiResponse> getRating(@Header("Authorization") String apiKey,
                                @Header("userId") String usersId,
                                @Header("token") String token,
                                @Header("Language") String language);

    // settings
    @POST("seller/shop/settings")
    Call<ApiResponse> getSettings(@Header("Authorization") String apiKey,
                                  @Header("userId") String usersId,
                                  @Header("token") String token,
                                  @Header("Language") String language);

    // update settings
    @POST("seller/shop/settings/update")
    Call<ApiResponse> updateSettings(@Header("Authorization") String apiKey,
                                     @Header("userId") String usersId,
                                     @Header("token") String token,
                                     @Header("Language") String language,
                                     @Body Settings settings);

    // change password
    @POST("seller/password/change")
    Call<ApiResponse> changePassword(@Header("Authorization") String apiKey,
                                     @Header("userId") String usersId,
                                     @Header("token") String token,
                                     @Header("Language") String language,
                                     @Body ChangePassword changePassword);

    // deliver area
    @POST("seller/delivery/area")
    Call<ApiResponse> getDeliveryArea(@Header("Authorization") String apiKey,
                                      @Header("userId") String usersId,
                                      @Header("token") String token,
                                      @Header("Language") String language);

    // update deliver area
    @POST("seller/delivery/area/update")
    Call<ApiResponse> updateDeliveryArea(@Header("Authorization") String apiKey,
                                         @Header("userId") String usersId,
                                         @Header("token") String token,
                                         @Header("Language") String language,
                                         @Body UpdateDeliveryArea updateDeliveryArea);

    // product details
    @POST("seller/product")
    Call<ApiResponse> getProductDetails(@Header("Authorization") String apiKey,
                                        @Header("userId") String usersId,
                                        @Header("token") String token,
                                        @Header("Language") String language,
                                        @Body Product product);

    // product status
    @POST("seller/product/status/update")
    Call<ApiResponse> updateProductStatus(@Header("Authorization") String apiKey,
                                          @Header("userId") String usersId,
                                          @Header("token") String token,
                                          @Header("Language") String language,
                                          @Body ProductStatus productStatus);

    // update profile image
    @Multipart
    @POST("seller/profile/image/update")
    Call<ApiResponse> updateProfileImage(@Header("Authorization") String apiKey,
                                         @Header("userId") String usersId,
                                         @Header("token") String token,
                                         @Header("Language") String language,
                                         @Part MultipartBody.Part files);

    // filter product category
    @POST("seller/product/category/all")
    Call<ApiResponse> filterCategory(@Header("Authorization") String apiKey,
                                     @Header("userId") String usersId,
                                     @Header("token") String token,
                                     @Header("Language") String language,
                                     @Query("title") String title);

    // filter product category
    @POST("seller/product/supplier/all")
    Call<ApiResponse> filterSupplier(@Header("Authorization") String apiKey,
                                     @Header("userId") String usersId,
                                     @Header("token") String token,
                                     @Header("Language") String language,
                                     @Query("title") String title);

    // filter product type
    @POST("seller/product/type/all")
    Call<ApiResponse> filterProductType(@Header("Authorization") String apiKey,
                                        @Header("userId") String usersId,
                                        @Header("token") String token,
                                        @Header("Language") String language,
                                        @Query("title") String title,
                                        @Body Category category);

    // forget Password
    @POST("seller/password/forget")
    Call<ApiResponse> forgetPassword(@Header("Authorization") String apiKey,
                                     @Header("userId") String usersId,
                                     @Header("token") String token,
                                     @Header("Language") String language,
                                     @Body LogIn logIn);

    // unit list
    @POST("seller/unit/all")
    Call<ApiResponse> getUnitList(@Header("Authorization") String apiKey,
                                  @Header("userId") String usersId,
                                  @Header("token") String token,
                                  @Header("Language") String language);

    // filter product brand
    @POST("seller/product/brand/all")
    Call<ApiResponse> filterBrand(@Header("Authorization") String apiKey,
                                  @Header("userId") String usersId,
                                  @Header("token") String token,
                                  @Header("Language") String language,
                                  @Query("title") String title,
                                  @Body ProductType productType);

    // create product
    @POST("seller/product/create")
    Call<ApiResponse> createProduct(@Header("Authorization") String apiKey,
                                    @Header("userId") String usersId,
                                    @Header("token") String token,
                                    @Header("Language") String language,
                                    @Body RequestBody requestBody);

    // all product list
    @POST("seller/product/all")
    Call<ApiResponse> getAllProduct(@Header("Authorization") String apiKey,
                                    @Header("userId") String usersId,
                                    @Header("token") String token,
                                    @Header("Language") String language,
                                    @Body FilterProduct filterProduct,
                                    @Query("page") int page,
                                    @Query("size") int size);

    // update product quantity
    @POST("seller/product/quantity/update")
    Call<ApiResponse> updateProductQuantity(@Header("Authorization") String apiKey,
                                            @Header("userId") String usersId,
                                            @Header("token") String token,
                                            @Header("Language") String language,
                                            @Body ProductQuantity productQuantity);

    // product category for filtering
    @POST("seller/product/filter/category/all")
    Call<ApiResponse> getProductCategory(@Header("Authorization") String apiKey,
                                         @Header("userId") String usersId,
                                         @Header("token") String token,
                                         @Header("Language") String language);

    // update product
    @POST("seller/product/update")
    Call<ApiResponse> updateProduct(@Header("Authorization") String apiKey,
                                    @Header("userId") String usersId,
                                    @Header("token") String token,
                                    @Header("Language") String language,
                                    @Body RequestBody requestBody);

    // order list
    @POST("seller/order/all")
    Call<ApiResponse> getOrderList(@Header("Authorization") String apiKey,
                                   @Header("userId") String usersId,
                                   @Header("token") String token,
                                   @Header("Language") String language,
                                   @Body OrderListRequest orderListRequest,
                                   @Query("page") int page,
                                   @Query("size") int size);

    // order status change
    @POST("seller/order/status/update")
    Call<ApiResponse> updateOrderStatus(@Header("Authorization") String apiKey,
                                        @Header("userId") String usersId,
                                        @Header("token") String token,
                                        @Header("Language") String language,
                                        @Body OrderStatusRequest orderStatusRequest);

    // order details
    @POST("seller/order")
    Call<ApiResponse> getOrderDetails(@Header("Authorization") String apiKey,
                                      @Header("userId") String usersId,
                                      @Header("token") String token,
                                      @Header("Language") String language,
                                      @Body OrderDetailsRequest orderDetailsRequest);

    // offer type list
    @POST("seller/offer/type/all")
    Call<ApiResponse> getOfferType(@Header("Authorization") String apiKey,
                                   @Header("userId") String usersId,
                                   @Header("token") String token,
                                   @Header("Language") String language);

    // offer list
    @POST("seller/offer/all")
    Call<ApiResponse> getOfferList(@Header("Authorization") String apiKey,
                                   @Header("userId") String usersId,
                                   @Header("token") String token,
                                   @Header("Language") String language,
                                   @Body OfferListRequest offerListRequest,
                                   @Query("page") int page,
                                   @Query("size") int size);

    // offer category list
    @POST("seller/offer/category/all")
    Call<ApiResponse> getOfferCategory(@Header("Authorization") String apiKey,
                                       @Header("userId") String usersId,
                                       @Header("token") String token,
                                       @Header("Language") String language,
                                       @Body OfferCategoryRequest offerCategoryRequest);

    // offer details
    @POST("seller/offer")
    Call<ApiResponse> getOfferDetails(@Header("Authorization") String apiKey,
                                      @Header("userId") String usersId,
                                      @Header("token") String token,
                                      @Header("Language") String language,
                                      @Body OfferDetailsRequest offerDetailsRequest);

    // offer status
    @POST("seller/offer/status/update")
    Call<ApiResponse> updateOfferStatus(@Header("Authorization") String apiKey,
                                        @Header("userId") String usersId,
                                        @Header("token") String token,
                                        @Header("Language") String language,
                                        @Body OfferStatus offerStatus);

    // offer create
    @POST("seller/offer/create")
    Call<ApiResponse> createOffer(@Header("Authorization") String apiKey,
                                  @Header("userId") String usersId,
                                  @Header("token") String token,
                                  @Header("Language") String language,
                                  @Body OfferCreate offerCreate);

    // offer update
    @POST("seller/offer/update")
    Call<ApiResponse> updateOffer(@Header("Authorization") String apiKey,
                                  @Header("userId") String usersId,
                                  @Header("token") String token,
                                  @Header("Language") String language,
                                  @Body OfferUpdate offerUpdate);

    // offer product list
    @POST("seller/offer/product/all")
    Call<ApiResponse> getOfferProductList(@Header("Authorization") String apiKey,
                                          @Header("userId") String usersId,
                                          @Header("token") String token,
                                          @Header("Language") String language,
                                          @Body OfferProductListRequest offerProductListRequest,
                                          @Query("page") int page,
                                          @Query("size") int size);

    // add offer product
    @POST("seller/offer/product/add")
    Call<ApiResponse> addOfferProduct(@Header("Authorization") String apiKey,
                                      @Header("userId") String usersId,
                                      @Header("token") String token,
                                      @Header("Language") String language,
                                      @Body AddOfferProduct addOfferProduct);

    // notification
    @POST("seller/notification/all")
    Call<ApiResponse> getNotification(@Header("Authorization") String apiKey,
                                      @Header("userId") String usersId,
                                      @Header("token") String token,
                                      @Header("Language") String language);

    // delete notification
    @POST("seller/notification/update")
    Call<ApiResponse> deleteNotification(@Header("Authorization") String apiKey,
                                         @Header("userId") String usersId,
                                         @Header("token") String token,
                                         @Header("Language") String language,
                                         @Body UpdateNotificationRequest updateNotificationRequest);

    // product type for report
    @POST("seller/report/product/type/all")
    Call<ApiResponse> getProductType(@Header("Authorization") String apiKey,
                                     @Header("userId") String usersId,
                                     @Header("token") String token,
                                     @Header("Language") String language,
                                     @Body Category category);

    // product for report
    @POST("seller/report/product/all")
    Call<ApiResponse> getProduct(@Header("Authorization") String apiKey,
                                 @Header("userId") String usersId,
                                 @Header("token") String token,
                                 @Header("Language") String language,
                                 @Body ProductType productType);

    // report
    @POST("seller/report")
    Call<ApiResponse> getReport(@Header("Authorization") String apiKey,
                                @Header("userId") String usersId,
                                @Header("token") String token,
                                @Header("Language") String language,
                                @Body ReportRequest reportRequest);

    @GET("seller/attribute/all")
    Call<AttributeResponse> getAttributes(@Query("attributeName") String attributeName,
                                    @Header("Authorization") String apiKey,
                                    @Header("userId") String usersId,
                                    @Header("token") String token,
                                    @Header("Language") String language);

    @GET("seller/attribute/data/all")
    Call<AttributeResponse> getAllAttributeData(@Query("attributeDataName") String attributeDataName,
                                          @Query("attribute_id") int attributeId,
                                          @Header("Authorization") String apiKey,
                                          @Header("userId") String usersId,
                                          @Header("token") String token,
                                          @Header("Language") String language);

    @DELETE("seller/attribute/data/delete")
    Call<ApiResponse> deleteAttributeData(@Header("Authorization") String apiKey,
                                          @Header("userId") String usersId,
                                          @Header("token") String token,
                                          @Header("Language") String language,
                                          @Query("productAttributeId") long productAttributeId);

    }
