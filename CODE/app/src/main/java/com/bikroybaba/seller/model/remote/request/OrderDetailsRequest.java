package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderDetailsRequest {
    @SerializedName("orderId")
    @Expose
    private final String orderId;

    public OrderDetailsRequest(String orderId) {
        this.orderId = orderId;
    }
}
