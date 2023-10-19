package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductQuantity {

    @SerializedName("productId")
    @Expose
    private final String productId;

    @SerializedName("quantity")
    @Expose
    private final String productQuantity;

    public ProductQuantity(String productId, String productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductQuantity() {
        return productQuantity;
    }
}
