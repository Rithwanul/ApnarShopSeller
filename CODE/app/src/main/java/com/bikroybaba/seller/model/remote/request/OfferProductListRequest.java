package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferProductListRequest {

    @SerializedName("offerId")
    @Expose
    private final String offerId;
    @SerializedName("filterBy")
    @Expose
    private final String filterBy;
    @SerializedName("searchKey")
    @Expose
    private final String searchKey;

    public OfferProductListRequest(String offerId, String filterBy,String searchKey) {
        this.offerId = offerId;
        this.filterBy = filterBy;
        this.searchKey = searchKey;
    }
}
