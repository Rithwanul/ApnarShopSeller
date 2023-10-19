package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductName {

    @SerializedName("id")
    @Expose
    private String productId;
    @SerializedName("title")
    @Expose
    private String productName;

    public ProductName() {
    }

    public ProductName(String productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }
}
