package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("productId")
    @Expose
    private final String productId;

    public Product(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
