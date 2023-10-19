package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferCategoryRequest {

    @SerializedName("offerTypeId")
    @Expose
    private final String offerTypeId;

    public OfferCategoryRequest(String offerTypeId) {
        this.offerTypeId = offerTypeId;
    }
}
