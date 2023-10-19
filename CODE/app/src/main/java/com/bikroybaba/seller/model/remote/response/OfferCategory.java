package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferCategory {

    @SerializedName("id")
    @Expose
    private String offerCategoryId;
    @SerializedName("title")
    @Expose
    private String offerCategoryTitle;

    public String getOfferCategoryId() {
        return offerCategoryId;
    }

    public String getOfferCategoryTitle() {
        return offerCategoryTitle;
    }
}
