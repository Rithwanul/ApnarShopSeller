package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductStatus {
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("status")
    @Expose
    private String productStatus;

    public ProductStatus() {
    }

    public ProductStatus(String productId, String productStatus) {
        this.productId = productId;
        this.productStatus = productStatus;
    }
}
