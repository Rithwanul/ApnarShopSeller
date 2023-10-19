package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferProductListResponse {
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("title")
    @Expose
    private String productName;
    @SerializedName("image")
    @Expose
    private String productImage;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("status")
    @Expose
    private String status;

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getUnit() {
        return unit;
    }

    public String getStatus() {
        return status;
    }
}
