package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Offers {
    @SerializedName("offerKeyword")
    @Expose
    private String offerKeyword;
    @SerializedName("minAmount")
    @Expose
    private String minAmount;
    @SerializedName("offerType")
    @Expose
    private String offerType;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("offerId")
    @Expose
    private String offerId;
    @SerializedName("freeProductId")
    @Expose
    private String freeProductId;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("offerCategory")
    @Expose
    private String offerCategory;

    public String getOfferKeyword() {
        return offerKeyword;
    }

    public void setOfferKeyword(String offerKeyword) {
        this.offerKeyword = offerKeyword;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getFreeProductId() {
        return freeProductId;
    }

    public void setFreeProductId(String freeProductId) {
        this.freeProductId = freeProductId;
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

    public String getOfferCategory() {
        return offerCategory;
    }

    public void setOfferCategory(String offerCategory) {
        this.offerCategory = offerCategory;
    }
}
