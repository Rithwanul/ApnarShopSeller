package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferCreate {

    @SerializedName("typeId")
    @Expose
    private final String offerTypeId;
    @SerializedName("categoryId")
    @Expose
    private final String offerCategoryId;
    @SerializedName("title")
    @Expose
    private final String offerName;
    @SerializedName("amount")
    @Expose
    private final String amount;
    @SerializedName("minAmount")
    @Expose
    private final String minAmount;
    @SerializedName("maxAmount")
    @Expose
    private final String maxDiscount;
    @SerializedName("keyword")
    @Expose
    private final String generatedCode;
    @SerializedName("startTime")
    @Expose
    private final String startTime;
    @SerializedName("endTime")
    @Expose
    private final String endTime;



    public OfferCreate(String offerTypeId, String offerCategoryId, String offerName, String amount, String minAmount, String maxDiscount, String generatedCode, String startTime, String endTime) {
        this.offerTypeId = offerTypeId;
        this.offerCategoryId = offerCategoryId;
        this.offerName = offerName;
        this.amount = amount;
        this.minAmount = minAmount;
        this.maxDiscount = maxDiscount;
        this.generatedCode = generatedCode;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "OfferCreate{" +
                "offerTypeId='" + offerTypeId + '\'' +
                ", offerCategoryId='" + offerCategoryId + '\'' +
                ", offerName='" + offerName + '\'' +
                ", amount='" + amount + '\'' +
                ", minAmount='" + minAmount + '\'' +
                ", maxDiscount='" + maxDiscount + '\'' +
                ", generatedCode='" + generatedCode + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
