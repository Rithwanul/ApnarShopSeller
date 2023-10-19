package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterProduct {

    @SerializedName("filterBy")
    @Expose
    private final String filterBy;
    @SerializedName("searchKey")
    @Expose
    private final String searchKey;


    public FilterProduct(String filterBy, String searchKey) {
        this.filterBy = filterBy;
        this.searchKey = searchKey;
    }

    public String getFilteredBy() {
        return filterBy;
    }
}
