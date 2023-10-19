package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddOfferProduct {
    @SerializedName("offerId")
    @Expose
    private final String offerId;
    @SerializedName("productId")
    @Expose
    private final String productId;
    @SerializedName("status")
    @Expose
    private final String status;

    public AddOfferProduct(String offerId, String productId, String status) {
        this.offerId = offerId;
        this.productId = productId;
        this.status = status;
    }
}
