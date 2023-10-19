package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferListResponse {
    @SerializedName("id")
    @Expose
    public String offerListId;
    @SerializedName("title")
    @Expose
    private final String offerListTitle;

    public OfferListResponse(String offerListId, String offerListTitle) {
        this.offerListId = offerListId;
        this.offerListTitle = offerListTitle;
    }

    public String getOfferListId() {
        return offerListId;
    }

    public String getOfferListTitle() {
        return offerListTitle;
    }
}
