package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportResponse {
    @SerializedName("totalRating")
    @Expose
    private String totalRating;
    @SerializedName("deliveredOrderPrice")
    @Expose
    private String deliveredOrderPrice;
    @SerializedName("totalOrderPrice")
    @Expose
    private String totalOrderPrice;
    @SerializedName("cancelledOrder")
    @Expose
    private String cancelledOrder;
    @SerializedName("deliveredOrder")
    @Expose
    private String deliveredOrder;
    @SerializedName("cancelledOrderedPrice")
    @Expose
    private String cancelledOrderedPrice;
    @SerializedName("productSold")
    @Expose
    private String productSold;
    @SerializedName("totalVisit")
    @Expose
    private String totalVisit;
    @SerializedName("totalOrder")
    @Expose
    private String totalOrder;
    @SerializedName("newProductCreatedPrice")
    @Expose
    private String newProductCreatedPrice;
    @SerializedName("newProductCreated")
    @Expose
    private String newProductCreated;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("productSoldPrice")
    @Expose
    private String productSoldPrice;
    @SerializedName("totalRatingAvg")
    @Expose
    private String totalRatingAvg;

    public String getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(String totalRating) {
        this.totalRating = totalRating;
    }

    public String getDeliveredOrderPrice() {
        return deliveredOrderPrice;
    }

    public void setDeliveredOrderPrice(String deliveredOrderPrice) {
        this.deliveredOrderPrice = deliveredOrderPrice;
    }

    public String getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(String totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public String getCancelledOrder() {
        return cancelledOrder;
    }

    public void setCancelledOrder(String cancelledOrder) {
        this.cancelledOrder = cancelledOrder;
    }

    public String getDeliveredOrder() {
        return deliveredOrder;
    }

    public void setDeliveredOrder(String deliveredOrder) {
        this.deliveredOrder = deliveredOrder;
    }

    public String getCancelledOrderedPrice() {
        return cancelledOrderedPrice;
    }

    public void setCancelledOrderedPrice(String cancelledOrderedPrice) {
        this.cancelledOrderedPrice = cancelledOrderedPrice;
    }

    public String getProductSold() {
        return productSold;
    }

    public void setProductSold(String productSold) {
        this.productSold = productSold;
    }

    public String getTotalVisit() {
        return totalVisit;
    }

    public void setTotalVisit(String totalVisit) {
        this.totalVisit = totalVisit;
    }

    public String getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(String totalOrder) {
        this.totalOrder = totalOrder;
    }

    public String getNewProductCreatedPrice() {
        return newProductCreatedPrice;
    }

    public void setNewProductCreatedPrice(String newProductCreatedPrice) {
        this.newProductCreatedPrice = newProductCreatedPrice;
    }

    public String getNewProductCreated() {
        return newProductCreated;
    }

    public void setNewProductCreated(String newProductCreated) {
        this.newProductCreated = newProductCreated;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getProductSoldPrice() {
        return productSoldPrice;
    }

    public void setProductSoldPrice(String productSoldPrice) {
        this.productSoldPrice = productSoldPrice;
    }

    public String getTotalRatingAvg() {
        return totalRatingAvg;
    }

    public void setTotalRatingAvg(String totalRatingAvg) {
        this.totalRatingAvg = totalRatingAvg;
    }
}
