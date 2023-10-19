package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferDetailsRequest {
    @SerializedName("offerId")
    @Expose
    private final String offerListId;

    public OfferDetailsRequest(String offerListId) {
        this.offerListId = offerListId;
    }
}
