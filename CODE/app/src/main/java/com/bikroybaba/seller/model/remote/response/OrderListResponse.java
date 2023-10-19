package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderListResponse {

    @SerializedName("id")
    @Expose
    private String orderId;
    @SerializedName("name")
    @Expose
    private String buyerName;
    @SerializedName("buyerProfilePicture")
    @Expose
    private String buyerProfileImage;
    @SerializedName("phone")
    @Expose
    private String buyerPhone;
    @SerializedName("status")
    @Expose
    private String orderStatus;
    @SerializedName("transactionId")
    @Expose
    private String orderTransactionId;
    @SerializedName("reason")
    @Expose
    private String orderReason;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerProfileImage() {
        return buyerProfileImage;
    }

    public void setBuyerProfileImage(String buyerProfileImage) {
        this.buyerProfileImage = buyerProfileImage;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderTransactionId() {
        return orderTransactionId;
    }

    public void setOrderTransactionId(String orderTransactionId) {
        this.orderTransactionId = orderTransactionId;
    }

    public String getOrderReason() {
        return orderReason;
    }

    public void setOrderReason(String orderReason) {
        this.orderReason = orderReason;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    @Override
    public String toString() {
        return "OrderListResponse{" +
                "orderId='" + orderId + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", buyerProfileImage='" + buyerProfileImage + '\'' +
                ", buyerPhone='" + buyerPhone + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderTransactionId='" + orderTransactionId + '\'' +
                ", orderReason='" + orderReason + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
