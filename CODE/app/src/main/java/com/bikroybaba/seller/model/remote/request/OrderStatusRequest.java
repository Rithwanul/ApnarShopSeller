package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderStatusRequest {

    @SerializedName("orderId")
    @Expose
    private final String orderId;
    @SerializedName("status")
    @Expose
    private final String status;
    @SerializedName("reason")
    @Expose
    private String reason;

    public OrderStatusRequest(String orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }
}
