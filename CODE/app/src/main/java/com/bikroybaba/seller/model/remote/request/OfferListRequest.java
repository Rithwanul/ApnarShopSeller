package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OfferListRequest {
    @SerializedName("timeline")
    @Expose
    private final String timeLine;
    @SerializedName("typeId")
    @Expose
    private final String offerTypeId;
    @SerializedName("searchKey")
    @Expose
    private  String searchKey;
    @SerializedName("shopId")
    @Expose
    private String shopId;

    public OfferListRequest(String timeLine, String offerTypeId) {
        this.timeLine = timeLine;
        this.offerTypeId = offerTypeId;
    }

    public OfferListRequest(String timeLine, String offerTypeId, String searchKey, String shopId) {
        this.timeLine = timeLine;
        this.offerTypeId = offerTypeId;
        this.searchKey = searchKey;
        this.shopId = shopId;
    }
}
