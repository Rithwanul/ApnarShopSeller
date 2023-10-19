package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Offer {
    @SerializedName("offerId")
    @Expose
    private String offerId;
    @SerializedName("freeProductId")
    @Expose
    private String freeProductId;
    @SerializedName("title")
    @Expose
    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
