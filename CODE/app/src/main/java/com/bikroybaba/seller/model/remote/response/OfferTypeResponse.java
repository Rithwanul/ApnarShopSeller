package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferTypeResponse {

    @SerializedName("id")
    @Expose
    private final String offerTypeId;
    @SerializedName("title")
    @Expose
    private final String offerTypeTitle;

    public OfferTypeResponse(String offerTypeId, String offerTypeTitle) {
        this.offerTypeId = offerTypeId;
        this.offerTypeTitle = offerTypeTitle;
    }

    public String getOfferTypeId() {
        return offerTypeId;
    }

    public String getOfferTypeTitle() {
        return offerTypeTitle;
    }
}
