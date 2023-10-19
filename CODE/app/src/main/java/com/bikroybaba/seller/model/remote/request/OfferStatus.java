package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferStatus {

    @SerializedName("offerId")
    @Expose
    private final String offerId;
    @SerializedName("action")
    @Expose
    private final String offerStatus;

    public OfferStatus(String offerId, String offerStatus) {
        this.offerId = offerId;
        this.offerStatus = offerStatus;
    }
}
