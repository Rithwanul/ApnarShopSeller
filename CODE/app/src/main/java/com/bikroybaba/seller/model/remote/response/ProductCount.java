package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCount {

    @SerializedName("limitedProduct")
    @Expose
    private String limitedProduct;
    @SerializedName("activeProduct")
    @Expose
    private String activeProduct;
    @SerializedName("newOrder")
    @Expose
    private String newOrder;

    public String getLimitedProduct() {
        return limitedProduct;
    }

    public String getActiveProduct() {
        return activeProduct;
    }

    public String getNewOrder() {
        return newOrder;
    }

    @Override
    public String toString() {
        return "ProductCount{" +
                "limitedProduct='" + limitedProduct + '\'' +
                ", activeProduct='" + activeProduct + '\'' +
                ", newOrder='" + newOrder + '\'' +
                '}';
    }
}
